-- Run with this command from the same directory as this script
--  sudo su postgres -c "psql -f setup_init_db.sql"
CREATE DATABASE convention;
CREATE ROLE application WITH LOGIN PASSWORD 'hello';
ALTER DATABASE convention OWNER TO application;
