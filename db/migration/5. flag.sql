--
-- Author: Jamius Siam
-- Since: 27/05/2026
--
CREATE TABLE flag
(
    id         UUID PRIMARY KEY DEFAULT uuidv7(),
    key        VARCHAR(255) NOT NULL,
    value      TEXT         NOT NULL,
    version    BIGINT           DEFAULT 0,
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE flag OWNER TO flightdrift;
