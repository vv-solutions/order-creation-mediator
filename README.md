# order-creation-mediator

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## About

This project is used as a mediator, between the order-msvc, kitchen-msvc, notification-msvc and delivery-msvc to ensure that events are distributed to all the services in the correct formats.

The order creation mediator is responsible for managing two events, the order created event and the ticket response event.

Order creation event: 
![order-creation.png](images%2Forder-creation.png)

Ticket response event:
![ticket-response-event.png](images%2Fticket-response-event.png)



![hexagonal-architecture.png](images%2Fhexagonal-architecture.png)