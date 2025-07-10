# Database Query Tool (Datasets API)

A Spring Boot REST API service for managing arbitrary JSON datasets with PostgreSQL persistence.

## Requirements  
- **Java 21** or higher  
- Maven (or use the included Maven wrapper)  
- **PostgreSQL 16** running on `localhost:5432`  
- Database named `datasets` 

## Setup  

1. **Clone the repository**  
   ```bash
   git clone https://github.com/SakirHussain/database-query-tool.git
   cd database-query-tool/datasets-api
   ```

2. **Configure PostgreSQL**  
   - Ensure PostgreSQL is running.  
   - Create the database:
     ```sql
     CREATE DATABASE datasets;
     ```
   - Default connection:  
     ```
     URL:      jdbc:postgresql://localhost:5432/datasets  
     Username: postgres  
     Password:  
     ```

## Building & Running  

### Quick Start  
```bash
./mvnw spring-boot:run
```
The application will start on `http://localhost:8080`.

### Build & Test  
```bash
# Compile
./mvnw clean compile

# Run tests
./mvnw test
```

## API Endpoints  

All endpoints are prefixed with `/api/dataset/{datasetName}`.

| Endpoint                                | Method | Description                                                     |
|-----------------------------------------|--------|-----------------------------------------------------------------|
| `/api/dataset/{name}/record`            | POST   | Insert a new JSON record into the named dataset.               |
| `/api/dataset/{name}/query`             | GET    | Query records with optional grouping, sorting, and order.       |
| `/actuator/health`                      | GET    | Health check.                                                   |

### Insert Record  
**Request**  
- URL: `/api/{dataset-name}/{name}/record`  
- Headers: `Content-Type: application/json`  
- Body:
  ```json
  {
    "payload": { /* arbitrary JSON object */ }
  }
  ```
- Response:  
  ```json
  {
    "recordId": 1,
    "dataset": "myDataset",
    "message": "Record inserted successfully"
  }
  ```

### Query Records 
**Request**  
- URL: `/api/{dataset-name}/{name}/query`  
- Query Parameters (all optional):  
  - `groupBy`: field name to group results by  
  - `sortBy`: field name to sort by  
  - `order`: `asc` or `desc` (defaults to `asc` if `sortBy` is present)
    
- Example 1 - no params:  
  ```
  GET /api/dataset/myDataset/query
  ```
  - Response:
    ```json
    {
      "sortedRecords": [
          {
              "age": 30,
              "city": "New York",
              "name": "John"
          },
          ...
    }
    ```
    
- Example 2 - groupBy:  
```
GET api/dataset/my-dataset/query?groupBy=age
```
  - Response:
    ```json
    {
      "groupedRecords": {
          "22": [
              {
                  "age": 22,
                  "city": "India",
                  "name": "Name1"
              },
              {
                  "age": 22,
                  "city": "New York",
                  "name": "Person3"
              }
          ],
          ...
    ```
    
- Example 3 - sortBy:  
```
GET api/dataset/my-dataset/query?sortBy=age&order=desc
```
  - Response:
    ```json
    {
      "sortedRecords": [
          {
              "age": 35,
              "city": "India",
              "name": "Sam"
          },
          {
              "age": 30,
              "city": "New York",
              "name": "John"
          },
          ...
    ```
    
- Example 4 - groupby & sortBy:  
```
GET api/dataset/my-dataset/query?groupBy=city&sortBy=age&order=desc
```
  - Response:
    ```json
    {
      "groupedRecords": {
          "New York": [
              {
                  "age": 30,
                  "city": "New York",
                  "name": "John"
              },
              {
                  "age": 22,
                  "city": "New York",
                  "name": "Person3"
              }
          ],
          "India": [
              {
                  ...
    ```


## Tech Stack  
- Java 21, Spring Boot 3.5.3, Spring Web, Spring Data JPA, Spring Validation, Spring Actuator  
- PostgreSQL 16  
- SpringDoc OpenAPI for API documentation
  
## Project Structure  
```
datasets-api/
├── src/
│   ├── main/
│   │   ├── java/com/assignment/datasets/
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── dto/              # Request/response DTOs
│   │   │   ├── exception/        # Custom exceptions
│   │   │   ├── model/            # JPA entities
│   │   │   ├── repository/       # Spring Data JPA repositories
│   │   │   ├── service/          # Business logic
│   │   │   └── DatasetsApiApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                     # Unit and integration tests
├── mvnw, mvnw.cmd                # Maven wrapper scripts
├── pom.xml                       # Maven configuration
└── README.md                     # This documentation
```
