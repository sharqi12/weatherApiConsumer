# Weather Api Consumer

### Quick info
The application serves the user historical (last 7 days) meteorological data from the public API (https://open-meteo.com). 
The server issues an endpoint that accepts longitude and latitude params, and the information returned includes the average amount of rainfall and snowfall, and the time of sunrise and sunset. 
The application saves data about request in the H2 database each time when the user calls the endpoint. 
The user is able to view the data stored in the database.

### Endpoints
* GET localhost:8080/weather/archive with params latitude and longitude - returns historical weather data
* GET localhost:8080/getAllRequestsData - returns data from H2 Database
