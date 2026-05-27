--
-- Author: Jamius Siam
-- Since: 27/05/2026
--
CREATE TABLE setting
(
    id         UUID PRIMARY KEY DEFAULT uuidv7(),
    account_id UUID REFERENCES account (id) NOT NULL,
    settings   JSONB                        NOT NULL,
    version    BIGINT           DEFAULT 0,
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);
