# Online Shop

Here is a description of how to run this solution. NB: requires these command line tools installed: docker, docker-compose and mvn

To build entire solution with all microservices:
```
mvn package -Dskiptests
```

There is scripts to run and setup entire environment from command line. This is creating and using docker containers.
* Linux/MacOS ``sh build.sh``
* Windows ``build.bat``

To run locally on your own machine remember to use active profile `local`.