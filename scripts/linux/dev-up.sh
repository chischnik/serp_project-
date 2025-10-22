#!/usr/bin/env bash
set -e
( cd docker && docker compose up -d )
( cd backend && mvn -q spring-boot:run ) &
( cd frontend/serp-web && npm install && npm start ) &
wait
