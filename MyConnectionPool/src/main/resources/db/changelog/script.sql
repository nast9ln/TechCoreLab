CREATE TABLE Role (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL
);

CREATE TABLE Person (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        registration_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        login VARCHAR(255) NOT NULL UNIQUE,
                        role BIGINT NOT NULL,
                        FOREIGN KEY (role) REFERENCES Role(id)
);

INSERT INTO Role (name) VALUES ('USER'), ('ADMIN');
