##  Notes
- vitals-service validates readings by type:
  BP requires systolic and diastolic
  HR requires hr
  SPO2 requires spo2
-  Duplicate readings are ignored (based on readingId)
-  Valid readings are forwarded to Alerts Service (http://localhost:8082/evaluate)
-  No authentication or error mapping is implemented yet — kept simple for demo.

###  Swagger UI (I added swagger api for convenience, for vitals service)
http://localhost:8081/swagger-ui/index.html

###  Post results via curl or using swagger api
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
