--
-- Author: Jamius Siam
-- Since: 22/05/2026
--
CREATE TABLE account
(
    id         UUID PRIMARY KEY DEFAULT uuidv7(),
    name       varchar(255) NOT NULL,
    username   varchar(255) NOT NULL,
    email      varchar(255) NOT NULL,
    password   varchar(255) NOT NULL,
    version    BIGINT    DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE blacklisted_auth_token
(
    id         UUID PRIMARY KEY DEFAULT uuidv7(),
    token      varchar(255) NOT NULL,
    version    BIGINT    DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
