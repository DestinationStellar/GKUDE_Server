DROP TABLE IF EXISTS user;
CREATE TABLE `user`
(
    `id`       bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username` varchar(20) NOT NULL UNIQUE COMMENT '姓名',
    `password` varchar(20) NOT NULL COMMENT '密码',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;