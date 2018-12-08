# Stylist Booking Application Model

*Swagger for EndPoint/Model/Dto Documentation*

[Swagger link](http://localhost:8080/swagger-ui.html)
http://localhost:8080/swagger-ui.html

Sample Queries, also Postman with request body JSON(application/json) for POST queries

```curl --header "Content-Type: application/json"  --request POST  --data '{"customerName":"JohnDoe"}'  http://localhost:8080/api/customer```

```curl -i -H "Accept: application/json" "localhost:8080/api/customer"```

```curl --header "Content-Type: application/json"  --request POST  --data '{"stylistName":"Kelvin"}'  http://localhost:8080/api/stylist```

```curl -i -H "Accept: application/json" "localhost:8080/api/stylist"```


```curl --header "Content-Type: application/json"  --request POST  --data '[[2018,12,9],[2018,12,10],[2018,12,11],[2018,12,12],[2018,12,13]]'  http://localhost:8080/api/initializeCalendar```

```curl  --header "Content-Type: application/json"  --request POST  --data '{"customerId":1,"timeSlotPK":{"slotIndex":0,"date":[2018,12,8]}}' http://localhost:8080/api/makeAppointment```

# Instructions
- For building:
```mvn clean package``` || ```mvnw clean package``` (can be with ```-DskipTests```)

    Expected Output: "...Building jar: \booking\target\booking-0.0.1-SNAPSHOT.jar ..."
- For Running: 
```java -jar target/booking-0.0.1-SNAPSHOT.jar```

