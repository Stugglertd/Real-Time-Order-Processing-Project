# Real-Time-Order-Processing-Project readme

Developed and implemented a real-time order processing system that allows customers to place orders, with asynchronous processing using Kafka as the messaging system.

-The Order Service handles customer orders and publishes them to Kafka topics for further processing. 

-The Inventory Service subscribes to the order topic to reserve inventory, while the Order Service listens to the inventory topic for updates on the success or failure of the reservation.

-Implemented Redis for time-based caching to enhance retrieval speed and improve application performance.


Tech Stack Used : Java 8, Spring Boot, Kafka, Redis, Docker(for redis Server), MySQL, PostMan.


Sample Api's

For Order Service:

1) POST -> http://localhost:8081/order
   {
		"customerId": 10,
		"products": [
		  {
			"productId": 120,
			"quantity": 6
		  }
		],
		"address": {
		  "street": "Linking Road",
		  "city": "Wardha",
		  "zipcode": "400050"
		}
    }
	Response =>
	{
		"orderId": 12,
		"status": "PENDING"
	}

2) GET -> http://localhost:8081/order/10

	Response =>
	{
		"orderId": 10,
		"status": "RESERVED"
	}
	
3) GET -> http://localhost:8081/order/cities

    Response => 
	[
		"Pune",
		"Mumbai",
		"Bangalore",
		"Kolkata",
		"Chennai",
		"Nagpur",
		"Hyderabad",
		"Delhi",
		"Wardha"
    ]	
	
	
For Inventory Service

1) PUT -> http://localhost:8082/inventory

	{
		"productId": 123,
		"quantity": 5
	}
	
    Response => Added/Updated product successfully...
			
2) GET -> http://localhost:8082/inventory/getOutOfStockProducts
	
	Response =>
	[
		"129",
		"125",
		"123",
		"121"
	]


Kafka Server Running On Port = localhost:9092

Redis Server Running On Port = 6379
