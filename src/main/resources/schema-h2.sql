DROP TABLE IF EXISTS customer_in_employers;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS employers;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id INT AUTO_INCREMENT  PRIMARY KEY,
                       user_name VARCHAR(36) NOT NULL,
                       encrypted_password VARCHAR(128) NOT NULL,
                       creation_date TIMESTAMP  NULL,
                       last_modified_date TIMESTAMP  NULL,
                       enabled boolean NOT NULL
);

CREATE TABLE roles (
                       role_id INT AUTO_INCREMENT  PRIMARY KEY,
                       role_name VARCHAR(30) NOT NULL,
                       id INT NOT NULL,
                       FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

-- Creating table for the Customer entity
CREATE TABLE customers (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           email VARCHAR(255) NOT NULL UNIQUE,
                           age INTEGER NOT NULL,
                           phone_number VARCHAR(255) NOT NULL,
                           password VARCHAR(255) NOT NULL,
                           created_by INT,
                           last_modified_by INT,
                           creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE,
                           FOREIGN KEY (last_modified_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Creating table for the Account entity
CREATE TABLE accounts (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          number VARCHAR(36) NOT NULL UNIQUE,
                          currency VARCHAR(3) NOT NULL,
                          balance DOUBLE PRECISION NOT NULL,
                          customer_id BIGINT NOT NULL,
                          created_by INT,
                          last_modified_by INT,
                          creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (customer_id) REFERENCES customers(id),
                          FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE,
                          FOREIGN KEY (last_modified_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Creating table for the Employer entity
CREATE TABLE employers (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           address VARCHAR(255) NOT NULL UNIQUE,
                           created_by INT,
                           last_modified_by INT,
                           creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE,
                           FOREIGN KEY (last_modified_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Creating many-to-many relationship table between Customers and Employers
CREATE TABLE customer_in_employers (
                                       customer_id BIGINT NOT NULL,
                                       employer_id BIGINT NOT NULL,
                                       PRIMARY KEY (customer_id, employer_id),
                                       FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
                                       FOREIGN KEY (employer_id) REFERENCES employers(id) ON DELETE CASCADE
);
