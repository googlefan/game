CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `account` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `token` varchar(65) DEFAULT NULL,
  `auth` int(11) NOT NULL DEFAULT '0',
  `state` int(11) NOT NULL,
  `reg_time` datetime DEFAULT NULL,
  `reg_ip` varchar(45) DEFAULT NULL,
  `site_connected` int(11) NOT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `last_login_ip` varchar(45) DEFAULT NULL,
  `is_direct` tinyint(1) NOT NULL DEFAULT '1',
  `login_num` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
