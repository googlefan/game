CREATE TABLE `user_connect` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `site_id` bigint(20) NOT NULL,
  `site_email` varchar(125) NOT NULL,
  `site` int(11) NOT NULL,
  `connect_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_site_site_id` (`site`,`site_id`),
  KEY `idx_site_site_email` (`site`,`site_email`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
