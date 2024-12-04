# Foo Bar Kata Application

## Build The Project

In order to build the project, the following command must be executed :

`````shell
mvn clean install
`````

## Run Locally
Run as a Springboot application.

You can test the application API using : http://localhost:7003/swagger-ui/index.html#

## Deploy
In order to deploy our application: 
### With Docker : 
Run this command to build the image and run it on docker
`````shell
mvn spring-boot:build-image
`````


## Test the application
You can test the rest API with swagger UI:
http://localhost:7003/swagger-ui/index.html#

Or run it as a scheduled batch (configure a cron in the application yml)
