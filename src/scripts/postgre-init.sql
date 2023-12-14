-- 1. Pobierz i zainstaluj PostgreSQL i pgAdmin (w pgAdmin bedziesz wpisywal i testowal instrukcje SQL-owe).
-- 2. Uruchom pgAdmin.
-- 3. Kliknij Databases prawym klawiszem myszy i wybierz New Database.
-- 4. Nadaj bazie nazwe waytogo, pozostaw pozostale pola na wartosciach domyslnych.
-- 5. Nowa baza danych pojawi sie na liscie, ale w pgAdmin moze miec czerwony znak X, co oznacza, ze narzedzie nie jest z nia polaczone.
--  Kliknij ja dwukrotnie, aby sie polaczyc. Czerwony X zniknie i zobaczysz opcje.
-- 6. daj prawy na baze i create query czy jakos tak
-- 7. Wyswietli sie edytor zapytan.
-- 8. wrzuc to co jest w sql-init.sql i odpal
-- 9. pamietajcie aby przy odpalaniu apki wejsc w edit configuration i tam dodac do ActiveProfiels tak: 'postgres'
-- 10. odpal waytogo zobaczymy czy dziala i potem testy



--REASSIGN OWNED BY waytogo TO postgres;  -- or some other trusted role
--DROP OWNED BY waytogo;
DROP USER IF EXISTS waytogo;

CREATE USER  waytogo WITH PASSWORD 'waytogo';

ALTER ROLE waytogo SET client_encoding TO 'utf8';
ALTER ROLE waytogo SET default_transaction_isolation TO 'read committed';
ALTER ROLE waytogo SET timezone TO 'UTC';

GRANT ALL PRIVILEGES ON DATABASE waytogo TO waytogo;
GRANT ALL PRIVILEGES ON SCHEMA public TO waytogo;
