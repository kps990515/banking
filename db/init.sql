CREATE DATABASE IF NOT EXISTS banking;
USE banking;

ALTER USER 'root'@'%' IDENTIFIED BY 'root1234!!';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

-- USER 테이블 생성
CREATE TABLE IF NOT EXISTS user (
    id VARCHAR(45) NOT NULL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Transfer 테이블 생성
CREATE TABLE IF NOT EXISTS transfer (
    id VARCHAR(45) NOT NULL PRIMARY KEY,
    tx_id VARCHAR(45) NOT NULL,
    from_account_number VARCHAR(45) NOT NULL,
    to_account_number VARCHAR(45) NOT NULL,
    amount DECIMAL(19,2) NOT NULL DEFAULT '0.00',
    status VARCHAR(10) NOT NULL,
    message VARCHAR(255) DEFAULT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Account 테이블 생성
CREATE TABLE IF NOT EXISTS account (
    id VARCHAR(45) NOT NULL PRIMARY KEY,
    user_id VARCHAR(45) NOT NULL,
    account_number VARCHAR(20) NOT NULL,
    account_bank VARCHAR(45) NOT NULL,
    balance DECIMAL(19, 0) DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 초기 데이터 삽입
INSERT INTO user (id, username, email, phone, created_at, updated_at)
VALUES ('1', 'test1', 'test1@test.com', '010-1111-1111', NOW(), NOW());

INSERT INTO account (id, user_id, account_number, account_bank, balance, created_at, updated_at) VALUES
('1', '1', '1111111111', 'RYAN_BANK', 10000, NOW(), NOW()),
('2', '1', '2222222222', 'CHUNSIK_BANK', 0, NOW(), NOW());


