CREATE TABLE `pet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `category` varchar(100) NOT NULL,
  `token` varchar(100) NOT NULL,
  `device` varchar(100) NOT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `age` double DEFAULT NULL,
  `weight` double DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`device`),
  KEY `ix_token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

