-- 使用 j2ee 数据库
use j2ee;


-- 创建 vote 表
create table if not exists `vote`(
    -- 教师编号
    `id` int(11) not null,
    `name` varchar(255) not null,
    `subject` varchar(255) not null,
    `sex` varchar(255) not null,
    `age` int(5) not null,
    -- 票数
    `poll` int(11) not null default 0,
    primary key(`id`)
) ENGINE=InnoDB default charset=utf8;


-- 更新 vote 表
-- 一次更新多条数据
update vote set poll = case id
    when 200234145 then 1
    when 200392677 then 2
    when 200452170 then 3
    end
where id in (200234145, 200392677, 200452170);  -- 这一行可以没有，不知道性能是否会有影响