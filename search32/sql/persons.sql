-- 使用 j2ee 数据库
use j2ee;

-- 创建 persons 表
create table if not exists `persons`(
    -- 学号或教职工号
    `id` int(11) not null,
    `name` varchar(100) not null,
    `phone` varchar(100) not null unique,
    `qq` varchar(100) unique,
    `email` varchar(100) unique,
    primary key (`id`)
) ENGINE=InnoDB default charset=utf8;