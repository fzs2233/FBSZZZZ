-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS fbszzzz DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE fbszzzz;

-- 创建角色表
CREATE TABLE IF NOT EXISTS role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL UNIQUE COMMENT '角色名称',
    description VARCHAR(200) COMMENT '角色描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 插入默认角色
INSERT INTO role (role_name, description) VALUES ('ADMIN', '管理员，可以发起提案和管理系统');
INSERT INTO role (role_name, description) VALUES ('MEMBER', '普通成员，可以参与投票');

-- 创建成员表
CREATE TABLE IF NOT EXISTS member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '成员ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    role_id BIGINT DEFAULT 2 COMMENT '角色ID，默认为普通成员',
    register_time DATETIME COMMENT '注册时间',
    status TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-正常）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_phone (phone),
    INDEX idx_email (email),
    CONSTRAINT fk_member_role FOREIGN KEY (role_id) REFERENCES role(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成员表';

-- 创建提案表
CREATE TABLE IF NOT EXISTS proposal (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '提案ID',
    title VARCHAR(200) NOT NULL COMMENT '提案标题',
    description TEXT COMMENT '提案描述',
    proposer_id BIGINT NOT NULL COMMENT '提案人ID',
    vote_type TINYINT NOT NULL DEFAULT 1 COMMENT '投票类型：1-单选，2-多选',
    max_choices INT DEFAULT 1 COMMENT '多选时最多可选数量',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-草稿，1-进行中，2-已结束',
    start_time DATETIME COMMENT '投票开始时间',
    end_time DATETIME COMMENT '投票结束时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_proposer (proposer_id),
    INDEX idx_status (status),
    CONSTRAINT fk_proposal_proposer FOREIGN KEY (proposer_id) REFERENCES member(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='提案表';

-- 创建投票选项表
CREATE TABLE IF NOT EXISTS vote_option (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '选项ID',
    proposal_id BIGINT NOT NULL COMMENT '所属提案ID',
    option_text VARCHAR(500) NOT NULL COMMENT '选项文本',
    option_order INT NOT NULL DEFAULT 0 COMMENT '选项排序',
    vote_count INT NOT NULL DEFAULT 0 COMMENT '得票数',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT fk_option_proposal FOREIGN KEY (proposal_id) REFERENCES proposal(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投票选项表';

-- 创建投票记录表
CREATE TABLE IF NOT EXISTS vote_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    proposal_id BIGINT NOT NULL COMMENT '提案ID',
    member_id BIGINT NOT NULL COMMENT '投票人ID',
    option_id BIGINT NOT NULL COMMENT '选项ID',
    vote_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '投票时间',
    INDEX idx_proposal_member (proposal_id, member_id),
    CONSTRAINT fk_record_proposal FOREIGN KEY (proposal_id) REFERENCES proposal(id),
    CONSTRAINT fk_record_member FOREIGN KEY (member_id) REFERENCES member(id),
    CONSTRAINT fk_record_option FOREIGN KEY (option_id) REFERENCES vote_option(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投票记录表';
