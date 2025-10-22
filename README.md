SERP Starter (Monolith) - Java 17 / Node 22 / Postgres

Schnellstart:
1) DB:
   cd docker
   docker compose up -d
   cd ..\scripts\windows
   powershell -ExecutionPolicy Bypass -File .\init-db.ps1

2) Backend:
   cd backend
   mvn spring-boot:run

3) Frontend:
   cd frontend\serp-web
   npm install
   npm start

Backend: http://localhost:8080
Frontend: http://localhost:5173
Auth: POST /auth/register, POST /auth/login
API: /api/customers, /api/consultants, /api/projects, /api/entries
