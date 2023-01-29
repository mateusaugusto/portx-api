# Portx API Coding Challenge 

## Author
* Mateus Augusto Oliveira

## Tech Stack
* Java 17
* Spring Boot
* Lombok
* H2
* Kafka (Confluent)

## Decisions

I've used H2 database in memory and Confluent kafka for simple solution

## Endpoints

POST http://localhost:8086/payments -> {body}

GET http://localhost:8086/payments/search?status=CREATED

PUT http://localhost:8086/payments/{id}

DELETE http://localhost:8086/payments/{id}

## How to

Run tests: mvn clean test

Compile: mvn clean compile

Local install: mvn clean install

## Build and run Docker image

docker-compose upd

## Curl endpoints

-- Post Create Payment json

curl --location --request POST 'http://localhost:8086/payments' \
--header 'idempotency-Key: 1063ef6e-267b-48fc-b874-dcf1e861a49e' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=2276F21424EE7F8D7B4DF74414D51940' \
--data-raw '{
"currency": "USD",
"amount": 15,
"originator": {
"name": "originator"
},
"beneficiary": {
"name": "beneficiary"
},
"receiver": {
"type": "receiver",
"number": 123
},
"sender": {
"type": "sender",
"number": 123
}
}'

Search payments by status

curl --location --request GET 'http://localhost:8086/payments/search?status=CREATED' \
--header 'Cookie: JSESSIONID=2276F21424EE7F8D7B4DF74414D51940'

### Questions

Our sales team tells us that some of the customers will be sending around 1 million payments per
day, mostly during business hours, and that they will perform around 10 million queries per day.
Which steps will you take to make sure the application can handle this load?

1: Caching: Caching frequently accessed data;

2 Load balancing / Scaling horizontally: Distributing the incoming traffic across multiple servers; (kubernetes, AWS ECS);

3: Asynchronous processing: Implementing threads.

4: Using a NoSQL databases like MongoDB or Cassandra.

How does your system guarantee that when accepting a payment the payment is saved locally
and the message is sent to the topic? What would happen if a container running the application is
restarted after saving the payment to the DB and before sending the message to the topic?

1: Inconsistency: The data in the database and the data in the Kafka topic may become inconsistent.

2: Processing delays: The application may experience delays in processing requests while the container restarts and the application.

Strategy to handle this scenarios:

1: Implementing a transaction mechanism: To ensure that data is saved to the database and sent to the Kafka topic in a single atomic operation.

2: Using a persistence layer: To ensure that the data is saved to a persistent storage before the message is sent to the kafka topic, so that the data is not lost if the container restarts.

3: Implementing Kafka Producer Retries to handle the erros;

