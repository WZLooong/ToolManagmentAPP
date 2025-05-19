-- 用户表 (users)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(20) UNIQUE,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    name VARCHAR(255),
    employee_id VARCHAR(20) UNIQUE,
    team VARCHAR(255),
    role ENUM('机务人员', '工具管理员', '部门管理人员'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 工具表 (tools)
CREATE TABLE tools (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    model VARCHAR(255),
    specification VARCHAR(255),
    quantity INT,
    manufacturer VARCHAR(255),
    purchase_date DATE,
    storage_location VARCHAR(255),
    barcode VARCHAR(255) UNIQUE,
    status ENUM('可用', '借出', '维修中', '报废'),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 借还记录表 (borrow_return_records)
CREATE TABLE borrow_return_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tool_id INT,
    borrower_id INT,
    borrow_time DATETIME,
    expected_return_time DATETIME,
    actual_return_time DATETIME,
    borrow_reason TEXT,
    approval_status ENUM('待审批', '已同意', '已拒绝'),
    rejection_reason TEXT,
    FOREIGN KEY (tool_id) REFERENCES tools(id),
    FOREIGN KEY (borrower_id) REFERENCES users(id)
);

-- 工具交接表 (tool_transfers)
CREATE TABLE tool_transfers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tool_id INT,
    old_borrower_id INT,
    new_borrower_id INT,
    transfer_time DATETIME,
    transfer_reason TEXT,
    location POINT NOT NULL,
    SPATIAL INDEX(location),
    FOREIGN KEY (tool_id) REFERENCES tools(id),
    FOREIGN KEY (old_borrower_id) REFERENCES users(id),
    FOREIGN KEY (new_borrower_id) REFERENCES users(id)
);

-- 提醒记录表 (reminders)
CREATE TABLE reminders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    tool_id INT,
    reminder_type ENUM('借用到期提醒', '超期未还提醒', '交接提醒'),
    message TEXT,
    sent_time DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (tool_id) REFERENCES tools(id)
);