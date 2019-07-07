# Dumping structure for table wf.sys_user
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE IF NOT EXISTS `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='系统用户';

# Dumping data for table wf.sys_user: 2 rows
DELETE FROM `sys_user`;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` (`user_id`, `username`, `password`, `email`, `mobile`, `status`, `create_user_id`, `create_time`) VALUES (1, 'admin', '92925488b28ab12584ac8fcaa8a27a0f497b2c62940c8f4fbc8ef19ebc87c43e', 'integration@fintecher.cn', '13612345678', 1, NULL, '2016-11-11 11:11:11'), (3, 'root', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'root@121.com', '13800000000', 1, 1, '2017-08-21 11:38:55');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;


# Dumping structure for table wf.tb_token
DROP TABLE IF EXISTS `tb_token`;
CREATE TABLE IF NOT EXISTS `tb_token` (
  `user_id` bigint(20) NOT NULL,
  `token` varchar(100) NOT NULL COMMENT 'token',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户Token';

# Dumping data for table wf.tb_token: 0 rows
DELETE FROM `tb_token`;
/*!40000 ALTER TABLE `tb_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_token` ENABLE KEYS */;