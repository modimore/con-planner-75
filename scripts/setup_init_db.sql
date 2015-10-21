CREATE DATABASE convention;
CREATE ROLE application WITH LOGIN PASSWORD 'hello';
ALTER DATABASE convention OWNER TO application;
