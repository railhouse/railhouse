--
-- Author: Jamius Siam
-- Since: 22/05/2026
--
CREATE USER flightdrift WITH PASSWORD 'flightdrift';

ALTER DATABASE flightdrift OWNER TO flightdrift;

GRANT ALL ON SCHEMA public TO flightdrift;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO flightdrift;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO flightdrift;
