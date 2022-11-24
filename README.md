# Online Shop

Here is a description on how to run this solution. NB: requires these command line tools installed: docker, docker-compose and mvn

There is a convenience script for building and setting up the entire environment from command line. This is creating and using docker containers.
* Linux/MacOS:  ``sh build.sh``
* Windows: ``sh build.sh`` in Git bash

Now to start everything:
```
docker-compose --profile online-shop up
```

You should wait a couple minutes to let the services get fully up and discovered by the discovery-service.

To build entire solution with all microservices:
```
mvn package -Dskiptests
```

To run all tests:
```
mvn clean verify
```


If you want to run locally on your own machine, remember to use active profile `local`. You can then start db and RabbitMQ with `docker-compose --profile local up`

You can now see the running services at [Eureka dashboard](http://localhost:8761).

May be a good idea to remove cookies from localhost or run in an incognito window to minimize bad cookies being sent from earlier requests.