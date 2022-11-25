# Online Shop

Here is a description on how to run this solution. NB: requires these command line tools installed: docker, docker-compose and mvn

There is a convenience script for building and setting up the entire environment from command line. This is creating and using docker containers.
* Linux/MacOS:  ``sh build.sh``
* Windows: ``sh build.sh`` in Git bash

Now to start everything:

```
docker-compose --profile online-shop up
```

Add `-d` for detached mode.
I recommend running without detached mode for the best log output.
You should wait a couple minutes to let the services get fully up and discovered by the discovery-service.

To build entire solution with all microservices:
```
mvn package -Dskiptests
```

To run all tests:
```
mvn clean verify
```

If you want to run locally on your own machine, remember to use active profile `local`. You can then start db and RabbitMQ with 

`docker-compose --profile local up`

You can now see the running services at [Eureka Dashboard](http://localhost:8761) as well.

Maybe a good idea to remove cookies from localhost or run in an incognito window to minimize bad cookies being sent from earlier requests.

For this exam I have purposely left out users and authorization, as this was not described as wanted functionality in the assignment, and I wanted to focus on the functionality requested.\
In a real life scenario/production env I would like to implement an authentication/authorization layer on top of the gateway or a separate service, so that unauthorized users would not have access to any of the underlying micro-services.\
For this submission we will be operating in a "logged in" state as a regular user/shopper.