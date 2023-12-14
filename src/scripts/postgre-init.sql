--REASSIGN OWNED BY waytogo TO postgres;  -- or some other trusted role
--DROP OWNED BY waytogo;
DROP USER IF EXISTS waytogo;

CREATE USER  waytogo WITH PASSWORD 'waytogo';

ALTER ROLE waytogo SET client_encoding TO 'utf8';
ALTER ROLE waytogo SET default_transaction_isolation TO 'read committed';
ALTER ROLE waytogo SET timezone TO 'UTC';

GRANT ALL PRIVILEGES ON DATABASE waytogo TO waytogo;
GRANT ALL PRIVILEGES ON SCHEMA public TO waytogo;
