-- 使用 j2ee 数据库
use j2ee;


-- 创建 administrators 表
create table if not exists `administrators`(
    -- 教职工编号
    `id` int(11) not null,
    `name` varchar(100) not null,
    `email` varchar(100) not null unique,
    `password` varchar(100) not null,
    `register_time` datetime not null,
    `last_login_time` datetime not null,
    -- 头像id，扩展功能，在改工程未显现
    `iconID` int(11) not null default 0,
    primary key(`id`)
) ENGINE=InnoDB default charset=utf8;