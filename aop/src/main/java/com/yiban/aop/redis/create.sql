CREATE TABLE `t_user`(
`id` int unsigned NOT NULL AUTO_INCREMENT,
`username` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
`password` varchar(50) NOT NULL DEFAULT '' COMMENT '密码',
`sex` tinyint NOT NULL DEFAULT '0' COMMENT '性别 0=女1=男',
`deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志，默认0不删除，1删除',
`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';