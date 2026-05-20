-- =============================================
-- СТВОРЕННЯ ТАБЛИЦЬ
-- =============================================

CREATE TABLE job_titles
(
    id   INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT job_titles_name_unique UNIQUE (name)
);

CREATE TABLE assignment_statuses
(
    id   INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT assignment_statuses_name_unique UNIQUE (name)
);

CREATE TABLE departments
(
    id              INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    department_name VARCHAR(255) NOT NULL,
    manager_id      INTEGER,
    CONSTRAINT departments_name_unique UNIQUE (department_name)
);

CREATE TABLE employees
(
    id                INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name        VARCHAR(100) NOT NULL,
    last_name         VARCHAR(100) NOT NULL,
    middle_name       VARCHAR(100),
    department_id     INTEGER      NOT NULL,
    job_title_id      INTEGER      NOT NULL,
    employment_status VARCHAR(10)  NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT employees_department_fk
        FOREIGN KEY (department_id) REFERENCES departments (id),
    CONSTRAINT employees_job_title_fk
        FOREIGN KEY (job_title_id) REFERENCES job_titles (id),
    CONSTRAINT employees_employment_status_check
        CHECK (employment_status IN ('ACTIVE', 'FIRED'))
);

ALTER TABLE departments
    ADD CONSTRAINT departments_manager_fk
        FOREIGN KEY (manager_id) REFERENCES employees (id)
            ON DELETE SET NULL;

CREATE TABLE accounts
(
    id            INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username      VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(20)  NOT NULL,
    employee_id   INTEGER      NOT NULL,
    CONSTRAINT accounts_username_unique UNIQUE (username),
    CONSTRAINT accounts_employee_unique UNIQUE (employee_id),
    CONSTRAINT accounts_employee_fk
        FOREIGN KEY (employee_id) REFERENCES employees (id),
    CONSTRAINT accounts_role_check
        CHECK (role IN ('ROLE_EMPLOYEE', 'ROLE_MANAGER', 'ROLE_ADMIN'))
);

CREATE TABLE assignments
(
    id                     INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    serial_number          INTEGER      NOT NULL,
    title                  VARCHAR(255) NOT NULL,
    content                TEXT         NOT NULL,
    issue_date             DATE         NOT NULL,
    due_date               DATE         NOT NULL,
    actual_completion_date DATE,
    created_at             TIMESTAMP    NOT NULL DEFAULT now(),
    issuer_id              INTEGER      NOT NULL,
    executor_id            INTEGER      NOT NULL,
    assignment_status_id   INTEGER      NOT NULL,
    CONSTRAINT assignments_serial_unique UNIQUE (serial_number),
    CONSTRAINT assignments_issuer_fk
        FOREIGN KEY (issuer_id) REFERENCES employees (id),
    CONSTRAINT assignments_executor_fk
        FOREIGN KEY (executor_id) REFERENCES employees (id),
    CONSTRAINT assignments_status_fk
        FOREIGN KEY (assignment_status_id) REFERENCES assignment_statuses (id),
    CONSTRAINT assignments_dates_check
        CHECK (actual_completion_date IS NULL
            OR actual_completion_date >= issue_date)
);

-- =============================================
-- ПОЧАТКОВІ ДАНІ
-- =============================================

-- Статуси доручень
INSERT INTO assignment_statuses (name)
VALUES ('IN PROGRESS'),
       ('POSTPONED'),
       ('EXTENDED'),
       ('COMPLETED'),
       ('CANCELLED');

-- Посада початкового адміністратора
INSERT INTO job_titles (name)
VALUES ('Керівник організації');

-- Підрозділ початкового адміністратора
INSERT INTO departments (department_name, manager_id)
VALUES ('Адміністрація', NULL);

-- Співробітник початкового адміністратора
INSERT INTO employees (first_name, last_name, middle_name, department_id, job_title_id, employment_status)
VALUES ('Григорій',
        'Шевченко',
        'Григорович',
        (SELECT id FROM departments WHERE department_name = 'Адміністрація'),
        (SELECT id FROM job_titles WHERE name = 'Керівник організації'),
        'ACTIVE');

-- Обліковий запис початкового адміністратора
-- password_hash = BCrypt('admin')
INSERT INTO accounts (username, password_hash, role, employee_id)
VALUES ('admin',
        '$2a$10$OCrace12OKcJjU.FxUQ6xOZ3W7dZoqytaSlP.PcjklavmkCE3Cff2',
        'ROLE_ADMIN',
        (SELECT id FROM employees WHERE first_name = 'Григорій' AND last_name = 'Шевченко'));

-- Призначити адміністратора керівником підрозділу
UPDATE departments
SET manager_id = (SELECT id FROM employees WHERE first_name = 'Григорій' AND last_name = 'Шевченко')
WHERE department_name = 'Адміністрація';