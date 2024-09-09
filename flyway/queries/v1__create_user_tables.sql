CREATE TABLE roles (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50),
    last_login TIMESTAMP,
    joined_at TIMESTAMP,
    updated_at TIMESTAMP,
    recovery_email VARCHAR(255),
    last_old_password VARCHAR(255),
    employee_id BIGINT,
    client_id BIGINT
);

INSERT INTO roles (role_name) VALUES ('common_user');
INSERT INTO roles (role_name) VALUES ('premium_user');
INSERT INTO roles (role_name) VALUES ('storage_manager');
INSERT INTO roles (role_name) VALUES ('cashier');
INSERT INTO roles (role_name) VALUES ('admin');
