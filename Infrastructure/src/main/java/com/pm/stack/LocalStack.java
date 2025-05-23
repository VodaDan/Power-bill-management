package com.pm.stack;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ecs.*;
import software.amazon.awscdk.services.ecs.Protocol;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.logs.LogGroup;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.amazon.awscdk.services.msk.CfnCluster;
import software.amazon.awscdk.services.rds.*;
import software.amazon.awscdk.services.route53.CfnHealthCheck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LocalStack extends Stack {

    private final Vpc vpc;
    private final Cluster ecsCluster;

    public LocalStack(final App scope, final String id, final StackProps props) {
        super(scope,id,props);

        // Create Vpc
        this.vpc = createVpc();

        // Create Database instances
        DatabaseInstance customerServiceDb = createDatabase("PB-CustomerServiceDB","pb-customer-service-db");
        DatabaseInstance billsServiceDb = createDatabase("PB-BillsServiceDB","pb-bills-service-db");
        DatabaseInstance authServiceDb = createDatabase("PB-AuthServiceDB","pb-auth-service-db");
        DatabaseInstance analyticsServiceDb = createDatabase("PB-AnalyticsServiceDB","pb-analytics-service-db");

        // Create Database Healthchecks
        CfnHealthCheck customerDbHealthCheck = createDbHealthCheck(customerServiceDb,"CustomerServiceDbHealthCheck");
        CfnHealthCheck billsDbHealthCheck = createDbHealthCheck(billsServiceDb,"BillsServiceDbHealthCheck");
        CfnHealthCheck authDbHealthCheck = createDbHealthCheck(authServiceDb,"AuthServiceDbHealthCheck");
        CfnHealthCheck analyticsDbHealthCheck = createDbHealthCheck(analyticsServiceDb,"AnalyticsServiceDbHealthCheck");

        // Create Cfn Cluster
        CfnCluster mskCluster = createMskCluster();

        // Create Ecs Cluster
        this.ecsCluster = createEcsCluster();

        // Create Fargate Service
        FargateService customerService = createFargateService("CustomerService",
                "pb-customer-service",
                List.of(4000),
                customerServiceDb,Map.of(
                "BILLING_SERVICE_ADDRESS", "host.docker.internal",
                "BILLING_SERVICE_GRPC_PORT", "9001"
                ));

        FargateService billsService = createFargateService("BillsService","pb-bills-service",List.of(4001),billsServiceDb,null);

        FargateService authService = createFargateService("AuthService",
                "pb-auth-service",
                List.of(4002),
                authServiceDb,
                Map.of("JWT_SECRET", "XUpOzxntvH2vRW+lzOYKXJgB/mwVcb6ni9S7Qz3xL6M=")
                );

        FargateService analyticsService = createFargateService("AnalyticsService",
                "pb-analytics-service",
                List.of(4003),
                analyticsServiceDb,
                null);

        // Add dependency
        customerService.getNode().addDependency(customerServiceDb);
        customerService.getNode().addDependency(customerDbHealthCheck);
        customerService.getNode().addDependency(mskCluster);

        billsService.getNode().addDependency(billsServiceDb);
        billsService.getNode().addDependency(billsDbHealthCheck);
        billsService.getNode().addDependency(mskCluster);

        analyticsService.getNode().addDependency(analyticsServiceDb);
        analyticsService.getNode().addDependency(analyticsDbHealthCheck);
        analyticsService.getNode().addDependency(mskCluster);

        authService.getNode().addDependency(authServiceDb);
        authService.getNode().addDependency(authDbHealthCheck);

        // Create API gateway service
        createApiGatewayService();
    };

    public Vpc createVpc() {
        return Vpc.Builder
                .create(this, "PowerBillManagementVPC")
                .vpcName("PowerBillManagementVPC")
                .maxAzs(2)
                .build();
    }

    private DatabaseInstance createDatabase (String id, String dbName) {
        return DatabaseInstance.Builder
                .create(this, id)
                .engine(DatabaseInstanceEngine.postgres(
                        PostgresInstanceEngineProps.builder()
                                .version(PostgresEngineVersion.VER_17_2)
                                .build()))
                .vpc(vpc)
                .instanceType(InstanceType.of(InstanceClass.BURSTABLE2, InstanceSize.MICRO))
                .allocatedStorage(20)
                .credentials(Credentials.fromGeneratedSecret("admin_user"))
                .databaseName(dbName)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();
    }

    private CfnHealthCheck createDbHealthCheck (DatabaseInstance db, String id){
        return CfnHealthCheck.Builder.create(this, id)
                .healthCheckConfig(CfnHealthCheck.HealthCheckConfigProperty.builder()
                        .type("TCP")
                        .port(Token.asNumber(db.getDbInstanceEndpointPort()))
                        .ipAddress(db.getDbInstanceEndpointAddress())
                        .requestInterval(30)
                        .failureThreshold(3)
                        .build())
                .build();
    }

    private CfnCluster createMskCluster () {
        return CfnCluster.Builder
                .create(this,"MskCluster")
                .clusterName("kafka-cluster")
                .kafkaVersion("2.8.0")
                .numberOfBrokerNodes(1)
                .brokerNodeGroupInfo(CfnCluster.BrokerNodeGroupInfoProperty.builder()
                        .instanceType("kafka.m5.large")
                        .clientSubnets(vpc.getPrivateSubnets().stream()
                                .map(ISubnet::getSubnetId)
                                .collect(Collectors.toList()))
                        .brokerAzDistribution("DEFAULT")
                        .build())
                .build();
    }

    private Cluster createEcsCluster() {
        return Cluster.Builder.create(this,"PowerBillManagementCluster")
                .vpc(vpc)
                .defaultCloudMapNamespace(CloudMapNamespaceOptions.builder()
                        .name("power-bill-management.local")
                        .build())
                .build();
    }

    private FargateService createFargateService (String id,
                                                 String imageName,
                                                 List<Integer> ports,
                                                 DatabaseInstance db,
                                                 Map<String, String> additionalEnvVars) {

        FargateTaskDefinition taskDefinition =
                FargateTaskDefinition.Builder.create(this, id+ " Task")
                        .cpu(256)
                        .memoryLimitMiB(512)
                        .build();

        ContainerDefinitionOptions.Builder containerOptions =
                ContainerDefinitionOptions.builder()
                        .image(ContainerImage.fromRegistry(imageName))
                        .portMappings(ports.stream()
                                .map(port -> PortMapping.builder()
                                        .containerPort(port)
                                        .hostPort(port)
                                        .protocol(Protocol.TCP)
                                        .build())
                                .toList())
                        .logging(LogDriver.awsLogs(AwsLogDriverProps.builder()
                                        .logGroup(LogGroup.Builder.create(this, id + " LogGroup")
                                                .logGroupName("/ecs/" + imageName)
                                                .removalPolicy(RemovalPolicy.DESTROY)
                                                .retention(RetentionDays.ONE_DAY)
                                                .build())
                                        .streamPrefix(imageName)
                                .build()));
        Map<String,String> envVars = new HashMap<>();

        envVars.put("SPRING_KAFKA_BOOTSTRAP_SERVERS", "localhost.localstack.cloud:4510, localhost.localstack.cloud:4511, localhost.localstack.cloud:4512");

        if(additionalEnvVars != null) {
            envVars.putAll(additionalEnvVars);
        }

        if(db != null){
            envVars.put("SPRING_DATASOURCE_URL", "jdbc:postgresql://%s:%s/%s-db".formatted(
                    db.getDbInstanceEndpointAddress(),
                    db.getDbInstanceEndpointPort(),
                    imageName
            ));
            envVars.put("SPRING_DATASOURCE_USERNAME", "admin_user");
            envVars.put("SPRING_DATASOURCE_PASSWORD",
                    db.getSecret().secretValueFromJson("password").toString());
            envVars.put("SPRING_JPA_HIBERNATE_DDL_AUTO", "update");
            envVars.put("SPRING_SQL_INIT_MODE", "always");
            envVars.put("SPRING_DATASOURCE_HIKARI_INITIALIZATION_FAIL_TIMEOUT", "60000");
        }

        containerOptions.environment(envVars);
        taskDefinition.addContainer(imageName + "Container", containerOptions.build());

        return FargateService.Builder.create(this, id)
                .cluster(ecsCluster)
                .taskDefinition(taskDefinition)
                .assignPublicIp(false)
                .serviceName(imageName)
                .build();


    }

    private void createApiGatewayService () {
        FargateTaskDefinition taskDefinition = FargateTaskDefinition.Builder.create(this, "APIGatewayTaskDefinition")
                .cpu(256)
                .memoryLimitMiB(512)
                .build();

        ContainerDefinitionOptions containerOptions = ContainerDefinitionOptions.builder()
                .image(ContainerImage.fromRegistry("api-gateway"))
                .environment(Map.of(
                        "SPRING_PROFILE_ACTIVE", "prod",
                        "AUTH_SERVICE_URL","http://host.docker.internal:4005"
                ))
                .portMappings(List.of(4005).stream()
                        .map(port -> PortMapping.builder()
                                .containerPort(port)
                                .hostPort(port)
                                .protocol(Protocol.TCP)
                                .build())
                        .toList())
                .logging(LogDriver.awsLogs(AwsLogDriverProps.builder()
                                .logGroup(LogGroup.Builder.create(this,"APIGatewayLogGroup")
                                        .logGroupName("/ecs/api-gateway")
                                        .removalPolicy(RemovalPolicy.DESTROY)
                                        .retention(RetentionDays.ONE_DAY)
                                        .build())
                                .streamPrefix("api-gateway")
                        .build()))
                .build();

        taskDefinition.addContainer("APIGatewayContainer", containerOptions);

        ApplicationLoadBalancedFargateService apiGateway = ApplicationLoadBalancedFargateService.Builder.create(this, "APIGatewayService")
                .cluster(ecsCluster)
                .serviceName("api-gateway")
                .taskDefinition(taskDefinition)
                .desiredCount(1)
                .healthCheckGracePeriod(Duration.seconds(60))
                .build();

    }

    public static void main(final String[] args) {
        App app = new App(AppProps.builder().outdir("./cdk.out").build());
        StackProps props = StackProps.builder()
                .synthesizer(new BootstraplessSynthesizer())
                .build();
        new LocalStack(app,"localstack", props);
        app.synth();
        System.out.println("App synthesizing in progress.");
    }

}
