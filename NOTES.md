## 1. Notes
- vitals-service validates readings by type:
  BP requires systolic and diastolic,
  HR requires hr,
  SPO2 requires spo2
-  Duplicate readings are ignored (based on readingId)
-  Valid readings are forwarded to Alerts Service (http://localhost:8082/evaluate)
-  No authentication or error mapping is implemented yet — kept simple for demo.
-  Patient Vitals are stored in ** H2 Database ** along with Concurrent Hash


### 2. Swagger UI (added swagger api for convenience)
- http://localhost:8081/swagger-ui/index.html (vital-service)
- http://localhost:8082/swagger-ui/index.html (alert-service)

### 3. Post results via curl or using swagger api
- for more data @see src/main/resources/mockdata.json

curl -X 'POST' \
'http://localhost:8081/readings' \
-H 'accept: /' \
-H 'Content-Type: application/json' \
-d '{
"readingId": "11111111-1111-1111-1111-111111111112",
"patientId": "p-001",
"type": "BP",
"systolic": 150,
"diastolic": 95,
"capturedAt": "2025-08-01T12:00:00Z"
}'



### 4. Alerts Generated using sample data from src/main/resources/mockdata.json
- retrieve alterts via curl or use swagger api
- curl "http://localhost:8082/alerts?patientId=p-001"

![img.png](img.png)


### 5. No alert for following Data:
{
"readingId": "55555555-5555-5555-5555-555555555555",
"patientId": "p-001",
"type": "BP",
"systolic": 128,
"diastolic": 82,"capturedAt": "2025-08-01T12:20:00Z"
}

 you can find log entry related to this  in alter-service log
![img_1.png](img_1.png)


### 6. Implemented Validations & readable error messages
- Used @RestControllerAdvice(@see GlobalErrorController.java in vital-service project)
- Provided support for Jakarta Bean Validation (e.g., @Valid, @NotNull) using Hibernate Validator 
  through dependency spring-boot-starter-validation
- Following two images shows
- 1. Validation(@NotNull, @NotBlank etc) for required fields like patientId, readingId and type
- 2. Validation for mismatched type by throwing IllegalArgumentException and its Handler (types allowed are BP,HR &bSPO2)

![img_2.png](img_2.png)
![img_3.png](img_3.png)

## 7. Supported In Memory Database H2 (for vital-service) along with ConcurrentHashMap 
patients vitals are persisted concurrent Hash as well as persisted in table called readings(@see schema.sql)
in H2 database (for now in memory only but with little config change it can be persisted on file)
-- following vitals come from the reading table

curl -X 'GET' \
'http://localhost:8081/readings' \
-H 'accept: */*'

![img_4.png](img_4.png)










