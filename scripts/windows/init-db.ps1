\
param(
  [string]$Host = "localhost",
  [int]$Port = 5432,
  [string]$Db = "serp",
  [string]$User = "serp",
  [string]$Pw = "serp_pw"
)
$ErrorActionPreference = "Stop"
$env:PGPASSWORD = $Pw
$schemaSql = @"
CREATE SCHEMA IF NOT EXISTS auth;
CREATE SCHEMA IF NOT EXISTS customer;
CREATE SCHEMA IF NOT EXISTS consultant;
CREATE SCHEMA IF NOT EXISTS project;
CREATE SCHEMA IF NOT EXISTS entry;

CREATE TABLE IF NOT EXISTS auth.users (
  id UUID PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS customer.customers (
  id UUID PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  email VARCHAR(200) UNIQUE,
  created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS consultant.consultants (
  id UUID PRIMARY KEY,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  email VARCHAR(200) UNIQUE,
  created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS project.projects (
  id UUID PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  description TEXT,
  start_date DATE,
  end_date DATE
);

CREATE TABLE IF NOT EXISTS entry.entries (
  id UUID PRIMARY KEY,
  project_id UUID,
  consultant_id UUID,
  work_date DATE,
  hours NUMERIC(5,2) CHECK (hours >= 0),
  notes TEXT
);

CREATE INDEX IF NOT EXISTS idx_entries_work_date ON entry.entries (work_date);
CREATE INDEX IF NOT EXISTS idx_entries_project ON entry.entries (project_id);
CREATE INDEX IF NOT EXISTS idx_entries_consultant ON entry.entries (consultant_id);
"@
Write-Host ">> Erstelle Schemas & Tabellen..."
$cmd = "psql -h $Host -p $Port -U $User -d $Db -v ON_ERROR_STOP=1 -c ""$schemaSql"""
cmd /c $cmd
Write-Host ">> Fertig."
