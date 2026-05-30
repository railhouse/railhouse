--
-- Author: Jamius Siam
-- Since: 27/05/2026
--
CREATE TABLE project
(
    id              UUID PRIMARY KEY DEFAULT uuidv7(),
    name            VARCHAR(50)  NOT NULL,
    code            VARCHAR(10)  NOT NULL,
    icon_url        VARCHAR(1024),
    organization_id UUID REFERENCES organization (id) NOT NULL,
    version         BIGINT    DEFAULT 0,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE project OWNER TO flightdrift;

CREATE INDEX idx_project_organization_id ON project (organization_id);
