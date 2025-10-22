Start-Process powershell -ArgumentList "-NoExit -Command cd backend; mvn spring-boot:run"
Start-Process powershell -ArgumentList "-NoExit -Command cd frontend/serp-web; npm install; npm start"
