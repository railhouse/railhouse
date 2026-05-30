--
-- Author: Jamius Siam
-- Since: 27/05/2026
--
CREATE TABLE organization
(
    id         UUID PRIMARY KEY DEFAULT uuidv7(),
    name       VARCHAR(50)  NOT NULL,
    url        VARCHAR(255),
    icon_url   VARCHAR(1024),
    version    BIGINT    DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE organization OWNER TO flightdrift;

CREATE TABLE account_organization_mapping
(
    id              UUID PRIMARY KEY DEFAULT uuidv7(),
    account_id      UUID REFERENCES account (id),
    organization_id UUID REFERENCES organization (id),
    role            VARCHAR(255) NOT NULL,
    version         BIGINT    DEFAULT 0,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE account_organization_mapping OWNER TO flightdrift;
