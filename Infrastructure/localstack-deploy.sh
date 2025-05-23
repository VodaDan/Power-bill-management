#!/bin/bash

set -e

export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_REGION=us-east-1
export AWS_DEFAULT_REGION=us-east-1

TEMPLATE_S3_KEY=template.json
BUCKET_NAME=cdk-deployments
STACK_NAME=power-bill-management
TEMPLATE_URL=http://localhost:4566/${BUCKET_NAME}/${TEMPLATE_S3_KEY}

echo "Creating S3 bucket $BUCKET_NAME (if not exists)"
aws --endpoint-url=http://localhost:4566 s3 mb s3://$BUCKET_NAME || true

echo "Uploading template to S3"
aws --endpoint-url=http://localhost:4566 s3 cp ./cdk.out/localstack.template.json s3://$BUCKET_NAME/$TEMPLATE_S3_KEY

echo "Deleting existing CloudFormation stack if it exists"
aws --endpoint-url=http://localhost:4566 cloudformation delete-stack \
    --stack-name $STACK_NAME || true

echo "Waiting for stack deletion to complete"
aws --endpoint-url=http://localhost:4566 cloudformation wait stack-delete-complete \
    --stack-name $STACK_NAME || true

echo "Creating new CloudFormation stack"
aws --endpoint-url=http://localhost:4566 cloudformation create-stack \
    --stack-name $STACK_NAME \
    --template-url $TEMPLATE_URL \
    --capabilities CAPABILITY_IAM

echo "Waiting for stack creation to complete"
aws --endpoint-url=http://localhost:4566 cloudformation wait stack-create-complete \
    --stack-name $STACK_NAME

echo "Fetching ELB DNS name (if any)"
aws --endpoint-url=http://localhost:4566 elbv2 describe-load-balancers \
    --query "LoadBalancers[0].DNSName" --output text
