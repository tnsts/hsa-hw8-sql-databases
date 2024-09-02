CREATE TABLE IF NOT EXISTS users
(
    id           SERIAL,
    username     VARCHAR(24) NOT NULL,
    email        VARCHAR(64) NOT NULL,
    country_code VARCHAR(4),
    birthday     DATE        NOT NULL,
    created_at   TIMESTAMP   NOT NULL DEFAULT now(),
    PRIMARY KEY (id)
);