-- Tworzenie bazy danych
--CREATE DATABASE waytogo;

-- Tworzenie użytkownika
CREATE USER IF EXISTS waytogo WITH PASSWORD 'waytogo';

-- Ustawienia użytkownika
ALTER ROLE waytogo SET client_encoding TO 'utf8';
ALTER ROLE waytogo SET default_transaction_isolation TO 'read committed';
ALTER ROLE waytogo SET timezone TO 'UTC';

-- Nadanie wszystkich uprawnień użytkownikowi do nowej bazy danych
GRANT ALL PRIVILEGES ON DATABASE waytogo TO waytogo;
GRANT ALL PRIVILEGES ON SCHEMA public TO waytogo;
