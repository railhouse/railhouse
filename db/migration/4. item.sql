--
-- Author: Jamius Siam
-- Since: 27/05/2026
--
CREATE TABLE item
(
    id          UUID PRIMARY KEY DEFAULT uuidv7(),
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(2048),
    start_date  DATE,
    end_date    DATE,
    project_id  UUID REFERENCES project (id) NOT NULL,
    version     BIGINT    DEFAULT 0,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT item_date_range_check CHECK (
        start_date IS NULL OR end_date IS NULL OR end_date >= start_date
    )
);

CREATE INDEX idx_item_project_id ON item (project_id);
