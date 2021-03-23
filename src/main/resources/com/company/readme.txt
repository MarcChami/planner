
Project Setup

mvn io.quarkus:quarkus-maven-plugin:1.4.1.Final:create \
    -DprojectGroupId=com.company \
    -DprojectArtifactId=airline-bpmn-optaplanner \
    -Dextensions="resteasy, resteasy-jackson, optaplanner, \
                  optaplanner-jackson, kogito-quarkus, smallrye-openapi"
                  
               
https://www.optaplanner.org/blog/2020/05/05/WorkflowProcessesWithAIScheduling.html
https://kiegroup.github.io/kogito-online/#/editor/bpmn


Creating the Java Interfaces

In our BPMN, we referenced Java classes and services we still need to create. In particular:

    We referenced com.company.Flight which is used when creating, modifying and getting the seat assignments of a flight.

    We referenced com.company.Passenger which is used when we add a passenger to a flight.

    We referenced the operation addPassengerToFlight of com.company.FlightService when we add a passenger to the flight.

    We referenced the operation createSeatAssignments of com.company.FlightService when we create the seat assignments.

We’ll be using the following domain model for our classes:


Test appli

Now we are ready to test out the Flight Application we just created! Run the following command in the terminal to start the server:

./mvnw quarkus:dev

You can see the generated API methods by copying http://localhost:8080/openapi to The Swagger Editor.
http://localhost:8088/swagger-ui/

https://editor.swagger.io/

J'utilise Insomnia API pour faire les appels

Let’s try creating a flight. Run the following command :
curl -X POST "localhost:8080/flightProcess" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{\"flight\":{\"id\":\"string\",\"passengerList\":[],\"rowCount\":4,\"columnCount\":4}}"


Let add a few passengers to our flight (replace ${id} with the id you got from the previous command).

curl -X POST "localhost:8080/flightProcess/${id}/passengerRequestTicket" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{\"name\":\"Amy Cole\",\"preferredSeatType\":\"WINDOW\"}"
curl -X POST "localhost:8080/flightProcess/${id}/passengerRequestTicket" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{\"name\":\"John Smith\",\"preferredSeatType\":\"AISLE\"}"
curl -X POST "localhost:8080/flightProcess/${id}/passengerRequestTicket" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{\"name\":\"Bad Guy\"}"

Now let look at our tasks:

curl -X GET "localhost:8080/flightProcess/${id}/tasks" -H  "accept: application/json"


Let looks at one of our passengers (replace ${taskId} with the id of the "approvePassengerForFlight" you want to inspect):

 curl -X GET "localhost:8080/flightProcess/${id}/approvePassengerForFlight/${taskId}" -H  "accept: application/json"
 
 
 Let approve "Amy Cole" and "John Smith":

curl -X POST "localhost:8080/flightProcess/${id}/approvePassengerForFlight/${taskId}" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{\"isPassengerApprovedBySecurity\":true}"
(run the command twice; one for the "approvePassengerForFlight" task for Amy Cole, and one for the "approvePassengerForFlight" task for John Smith).

And let deny "Bad Guy":

curl -X POST "localhost:8080/flightProcess/${id}/approvePassengerForFlight/${taskId}" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{\"isPassengerApprovedBySecurity\":false}"

(replace ${taskId} with Bad Guy’s "approvePassengerForFlight" task id)

Finally, let create the seat assignments:

curl -X POST "localhost:8080/flightProcess/${id}/finalizePassengerList/${taskId}" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{}"
(Use "finalizePassengerList" task’s id here)

This request will take several seconds to return as it waits for OptaPlanner to optimize the flight seating. It will return a JSON containing the process variables, including the optimized seat assignments in the "flight" field:

{
  "id":"61210009-8f75-4bf7-9221-d5c92d7d1be3",
  "flight":{
    "id":"string",
    "score":{
      "initScore":0,
      "hardScore":0,
      "softScore":0,
      "feasible":true,
      "solutionInitialized":true
    },
    "passengerList":[
      {
        "name":"Amy Cole",
        "seat":{"seatIdentifier":"1A","type":"WINDOW"},
        "preferredSeatType":"WINDOW"
      },
      {
        "name":"John Smith",
        "seat":{"seatIdentifier":"1B","type":"AISLE"},
        "preferredSeatType":"AISLE"
      }
    ],
    "rowCount":4,
    "columnCount":4
  }
}
In the above example, "Amy Cole" is assigned to seat "1A" and "John Smith" is assigned to seat "1B".

What Next?
Now we have a fully functional REST service, we can easily extend it by:

Creating a UI frontend to the service

Signaling the BPMN process when OptaPlanner is finished solving instead of waiting for solving to finish, and poll the latest solutions client-side

Send Kafka messages whenever OptaPlanner finds a new solution and consume them in the BPMN process

You can find an extended version of the flight example created in this blog at the Kogito Examples repository, which has a full UI and poll for the latest solution from the solver instead of waiting for solving to finish

https://github.com/kiegroup/kogito-examples/tree/master/process-optaplanner-quarkus




