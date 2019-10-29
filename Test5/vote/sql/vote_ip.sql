-- 使用 j2ee 数据库
use j2ee;

-- 创建 vote_ip 表
create table if not exists `vote_ip`(
    -- 投票者ip
    `ip` varchar(255) not null,
    `last_vote_time` datetime not null,
    primary key(`ip`)
) ENGINE=InnoDB default charset=utf8;