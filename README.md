Working version of what we talked about in class.

There is a discovery-service, which is a Eureka server running on port 8761, and you can look at the dashboard.  

There are three other services.  These are all Eureka clients.

gateway-service: This is using the Eureka service to provide mapping.  Note that the lb://pirate-service is using the lb:// prefix, which means "load balanced", and is using the name the service is registered under in the Eureka service.

ship-service: A standard REST api, accessible via the gateway, and posting/retrieving messages from amqp queues
pirate-service: A standard REST api, accessible via the gateway.  Posts/retrieves messages from an amqp queue, but also has a RestTemplate call to the ship-service, using Eureka.

Additionally, I have provided a docker-compose.yml that sets up the RabbitMQ message broker container, and also uses config files to create some queues when the service is started.  The RabbitMQ dashboard runs on 15672, the queues run on 5672