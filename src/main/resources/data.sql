BEGIN;

INSERT INTO users(enabled, encrypted_password, user_name) VALUES
                                                              (true, '$2a$10$BXH1wlAJPIMXvjnJTBoRuea4CvZwSs8/Zqz4bDRZBDJ6hxvXoHlqq', 'a'),
                                                              (true, '$2a$10$BXH1wlAJPIMXvjnJTBoRuea4CvZwSs8/Zqz4bDRZBDJ6hxvXoHlqq', 'admin');

INSERT INTO roles(role_id, role_name, user_id) VALUES
                                                   (101, 'USER', 1),
                                                   (102, 'ADMIN', 2);

-- Adding companies
INSERT INTO employers (name, address, created_by, last_modified_by) VALUES ('Company A', 'Address A', 1, 1);
INSERT INTO employers (name, address, created_by, last_modified_by) VALUES ('Company B', 'Address B', 1, 2);
INSERT INTO employers (name, address, created_by, last_modified_by) VALUES ('Company C', 'Address C', 2, 2);

-- Adding customers
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('John Doe', 'john@example.com', 30, '123456789', 'password1', 1, 1);
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('Jane Smith', 'jane@example.com', 25, '987654321', 'password2', 1, 2);
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('Michael Johnson', 'michael@example.com', 35, '456123789', 'password3', 2, 2);
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('Sarah Brown', 'sarah@example.com', 28, '654789321', 'password4', 1, 1);
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('Chris Wilson', 'chris@example.com', 40, '123789654', 'password5', 1, 2);
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('Emma Lee', 'emma@example.com', 32, '789321654', 'password6', 2, 2);
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('David Taylor', 'david@example.com', 22, '321654987', 'password7', 1, 1);
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('Olivia Anderson', 'olivia@example.com', 29, '987321654', 'password8', 1, 2);
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('William Martinez', 'william@example.com', 27, '456987321', 'password9', 2, 2);
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('Sophia Hernandez', 'sophia@example.com', 33, '321987456', 'password10', 1, 1);
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('Daniel Thompson', 'daniel@example.com', 31, '654321987', 'password11', 1, 2);
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('Ava Gonzalez', 'ava@example.com', 38, '789654321', 'password12', 2, 2);
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('James Perez', 'james@example.com', 26, '456321987', 'password13', 1, 1);
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('Isabella Roberts', 'isabella@example.com', 24, '987654123', 'password14', 1, 2);
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('Joseph Walker', 'joseph@example.com', 36, '654123987', 'password15', 2, 2);
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('Mia Hall', 'mia@example.com', 23, '321987654', 'password16', 1, 1);
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('Alexander Young', 'alexander@example.com', 39, '987654321', 'password17', 1, 2);
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('Charlotte King', 'charlotte@example.com', 37, '456789123', 'password18', 2, 2);
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('Ethan Scott', 'ethan@example.com', 34, '789123654', 'password19', 1, 1);
INSERT INTO customers (name, email, age, phone_number, password, created_by, last_modified_by) VALUES ('Emily Adams', 'emily@example.com', 21, '123654987', 'password20', 1, 2);

-- Adding accounts for customers
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('USD', 1000.00, (SELECT id FROM customers WHERE name = 'John Doe'), 1, 1);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('EUR', 500.00, (SELECT id FROM customers WHERE name = 'John Doe'), 1, 2);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('USD', 2000.00, (SELECT id FROM customers WHERE name = 'Jane Smith'), 2, 2);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('EUR', 1500.00, (SELECT id FROM customers WHERE name = 'Michael Johnson'), 1, 1);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('USD', 1000.00, (SELECT id FROM customers WHERE name = 'Sarah Brown'), 1, 2);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('EUR', 500.00, (SELECT id FROM customers WHERE name = 'Chris Wilson'), 2, 2);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('USD', 2000.00, (SELECT id FROM customers WHERE name = 'Emma Lee'), 1, 1);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('EUR', 1500.00, (SELECT id FROM customers WHERE name = 'David Taylor'), 1, 2);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('USD', 1000.00, (SELECT id FROM customers WHERE name = 'Olivia Anderson'), 2, 2);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('EUR', 500.00, (SELECT id FROM customers WHERE name = 'William Martinez'), 1, 1);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('USD', 2000.00, (SELECT id FROM customers WHERE name = 'Sophia Hernandez'), 1, 2);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('EUR', 1500.00, (SELECT id FROM customers WHERE name = 'Daniel Thompson'), 2, 2);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('USD', 1000.00, (SELECT id FROM customers WHERE name = 'Ava Gonzalez'), 1, 1);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('EUR', 500.00, (SELECT id FROM customers WHERE name = 'James Perez'), 1, 2);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('USD', 2000.00, (SELECT id FROM customers WHERE name = 'Isabella Roberts'), 2, 2);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('EUR', 1500.00, (SELECT id FROM customers WHERE name = 'Joseph Walker'), 1, 1);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('USD', 1000.00, (SELECT id FROM customers WHERE name = 'Mia Hall'), 1, 2);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('EUR', 500.00, (SELECT id FROM customers WHERE name = 'Alexander Young'), 2, 2);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('USD', 2000.00, (SELECT id FROM customers WHERE name = 'Charlotte King'), 1, 1);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('EUR', 1500.00, (SELECT id FROM customers WHERE name = 'Ethan Scott'), 1, 2);
INSERT INTO accounts (currency, balance, customer_id, created_by, last_modified_by) VALUES ('USD', 1000.00, (SELECT id FROM customers WHERE name = 'Emily Adams'), 2, 2);

-- Establishing relationships between customers and companies
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'John Doe'), (SELECT id FROM employers WHERE name = 'Company A'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'John Doe'), (SELECT id FROM employers WHERE name = 'Company B'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'Jane Smith'), (SELECT id FROM employers WHERE name = 'Company B'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'Michael Johnson'), (SELECT id FROM employers WHERE name = 'Company C'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'Sarah Brown'), (SELECT id FROM employers WHERE name = 'Company A'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'Chris Wilson'), (SELECT id FROM employers WHERE name = 'Company B'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'Emma Lee'), (SELECT id FROM employers WHERE name = 'Company C'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'David Taylor'), (SELECT id FROM employers WHERE name = 'Company A'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'Olivia Anderson'), (SELECT id FROM employers WHERE name = 'Company B'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'William Martinez'), (SELECT id FROM employers WHERE name = 'Company C'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'Sophia Hernandez'), (SELECT id FROM employers WHERE name = 'Company A'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'Daniel Thompson'), (SELECT id FROM employers WHERE name = 'Company B'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'Ava Gonzalez'), (SELECT id FROM employers WHERE name = 'Company C'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'James Perez'), (SELECT id FROM employers WHERE name = 'Company A'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'Isabella Roberts'), (SELECT id FROM employers WHERE name = 'Company B'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'Joseph Walker'), (SELECT id FROM employers WHERE name = 'Company C'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'Mia Hall'), (SELECT id FROM employers WHERE name = 'Company A'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'Alexander Young'), (SELECT id FROM employers WHERE name = 'Company B'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'Charlotte King'), (SELECT id FROM employers WHERE name = 'Company C'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'Ethan Scott'), (SELECT id FROM employers WHERE name = 'Company A'));
INSERT INTO customer_in_employers (customer_id, employer_id) VALUES ((SELECT id FROM customers WHERE name = 'Emily Adams'), (SELECT id FROM employers WHERE name = 'Company B'));

-- Create indexes
CREATE INDEX idx_accounts_customer_id ON accounts(customer_id);
CREATE INDEX idx_customer_in_employers_customer_id ON customer_in_employers(customer_id);
CREATE INDEX idx_customer_in_employers_employer_id ON customer_in_employers(employer_id);

COMMIT;
