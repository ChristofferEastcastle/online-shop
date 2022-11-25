#!/usr/bin/env sh

set -xe

mvn package -DskipTests
docker pull quay.io/testcontainers/ryuk:0.2.3
SERVICE=discovery-service
docker build -t $SERVICE -f $SERVICE/Dockerfile $SERVICE
SERVICE=gateway-service
docker build -t $SERVICE -f $SERVICE/Dockerfile $SERVICE
SERVICE=order-service
docker build -t $SERVICE -f $SERVICE/Dockerfile $SERVICE
SERVICE=payment-service
docker build -t $SERVICE -f $SERVICE/Dockerfile $SERVICE
SERVICE=shipping-service
docker build -t $SERVICE -f $SERVICE/Dockerfile $SERVICE