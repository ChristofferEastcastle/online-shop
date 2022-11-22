mvn package
SERVICE=discovery-service
docker build -t $SERVICE -f $SERVICE/Dockerfile $SERVICE
SERVICE=gateway-service
docker build -t $SERVICE -f $SERVICE/Dockerfile $SERVICE
SERVICE=order-service
docker build -t $SERVICE -f $SERVICE/Dockerfile $SERVICE
SERVICE=payment-service
docker build -t $SERVICE -f $SERVICE/Dockerfile $SERVICE

docker-compose --profile online-shop up