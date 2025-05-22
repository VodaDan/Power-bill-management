package com.pm.stack;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.rds.*;

public class LocalStack extends Stack {

    private final Vpc vpc;

    public LocalStack(final App scope, final String id, final StackProps props) {
        super(scope,id,props);

        // Create Vpc
        this.vpc = createVpc();

        DatabaseInstance customerServiceDb = createDatabase("PB-CustomerServiceDB","pb-customer-service-db");
        DatabaseInstance billsServiceDb = createDatabase("PB-BillsServiceDB","pb-bills-service-db");

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

    public static void main(final String[] args) {
        App app = new App(AppProps.builder().outdir("./cdk.out").build());
        StackProps props = StackProps.builder()
                .synthesizer(new BootstraplessSynthesizer())
                .build();
    }

}
