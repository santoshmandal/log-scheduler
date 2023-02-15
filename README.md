## How to build
Go to log-scheduler-service and run the maven command

    mvn clean install
    To run locally
    java -jar target/log-scheduler*.jar

## Docker Image Build

    docker build --tag=log-scheduler:latest .

## Docker Image Run

    docker run --network="host" log-scheduler:latest
    
    note: --network=host is required when connecting to loggegator is running in same host 
