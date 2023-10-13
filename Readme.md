1. Normal Data PUT Request:
Test Data:

```json
{"id":"IDS60901","name":"Adelaide (West Terrace / ngayirdapira","state":"SA","time_zone":"CST","lat":"-34.9","lon":"138.6","local_date_time":"15/04:00pm","local_date_time_full":"20230715160000","air_temp":"13.3","apparent_t":"9.5","cloud":"Partly cloudy","dewpt":"5.7","press":"1023.9","rel_hum":"60","wind_dir":"S","wind_spd_kmh":"15","wind_spd_kt":"8"}
```

Expected Result: Should return "201 - HTTP_CREATED" and save the data.

2. PUT Request - Update Data:
Test Data:

```json
{"id":"IDS60901","name":"Adelaide (West Terrace / ngayirdapira","state":"SA","time_zone":"CST","lat":"-34.9","lon":"138.6","local_date_time":"15/04:00pm","local_date_time_full":"20230715160000","air_temp":"14.5","apparent_t":"10.0","cloud":"Clear","dewpt":"4.5","press":"1024.5","rel_hum":"65","wind_dir":"N","wind_spd_kmh":"18","wind_spd_kt":"9"}
```

Expected Result: Should return "200 - weatherdata update" and update the data.

3. GET Request - Retrieve Data:
Test Data: GET request to AggregationServer.

Expected Result: Should return the data with ID "IDS60901".

4. GET Request - No Data:
Test Data: GET request when there is no data on the server.

Expected Result: Should return "205 - no data".

5. GET Request - Data Outdated:
Test Data: GET request when there is existing data but it is outdated.

Expected Result: Should return "202 - Data outdated".


6. Invalid Request:
Test Data: Send an invalid request to AggregationServer.

Expected Result: Should return "400 - invalid request".
