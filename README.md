# Online Shop

Here is a description on how to run this solution. NB: requires these command line tools installed: docker, docker-compose and mvn

To build entire solution with all microservices:
```
mvn package -Dskiptests
```

There is scripts to run and setup entire environment from command line. This is creating and using docker containers. You should wait a couple minutes to let the services get fully up and discovered by the discovery-service.
* Linux/MacOS ``sh build.sh``
* Windows`sh build.sh` in Git bash

If you want to run locally on your own machine, remember to use active profile `local`.

You can now see the running services at [Eureka dashboard](http://localhost:8761).

May be a good idea to remove cookies from localhost or run in an incognito window to minimize bad cookies being sent from earlier requests.