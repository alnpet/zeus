CREATE TABLE `pet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `category` varchar(100) NOT NULL,
  `token` varchar(100) NOT NULL,
  `device` varchar(100) NOT NULL,
  `gender` varchar(20) DEFAULT NULL,
  `age` double DEFAULT NULL,
  `weight` double DEFAULT NULL,
  `nickname` varchar(100) DEFAULT NULL COMMENT "owner's nick name",
  `phone` varchar(100) DEFAULT NULL COMMENT "owner's phone number",
  `email` varchar(100) DEFAULT NULL COMMENT "owner's email address",
  `creation_date` datetime NOT NULL,
  `last_modified_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_device`(`device`),
  KEY `idx_token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `activity_in_hour` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pet_id` int(11) NOT NULL,
  `hour` datetime NOT NULL,
  `feeling` int(11) NOT NULL COMMENT "feeling index",
  `health` int(11) NOT NULL COMMENT "health index",
  `food` int(11) NOT NULL COMMENT "food in gram",
  `play` int(11) NOT NULL COMMENT "play time in pts",
  `active` int(11) NOT NULL COMMENT "active time in pts",
  `rest` int(11) NOT NULL COMMENT "rest time in pts",
  `creation_date` datetime NOT NULL,
  `last_modified_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY idx_pet_hour(`pet_id`, `hour`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `activity_in_day` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pet_id` int(11) NOT NULL,
  `day` datetime NOT NULL,
  `feeling` int(11) NOT NULL COMMENT "feeling index",
  `health` int(11) NOT NULL COMMENT "health index",
  `food` int(11) NOT NULL COMMENT "food in gram",
  `play` int(11) NOT NULL COMMENT "play time in pts",
  `active` int(11) NOT NULL COMMENT "active time in pts",
  `rest` int(11) NOT NULL COMMENT "rest time in pts",
  `food_expected` int(11) NOT NULL COMMENT "expected food in gram",
  `sport_expected` int(11) NOT NULL COMMENT "expected sport in pts",
  `creation_date` datetime NOT NULL,
  `last_modified_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY idx_pet_day(`pet_id`, `day`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(100) NOT NULL,
  `type` int(11) NOT NULL COMMENT "1:small, 2:medium, 3:big",
  `english_name` varchar(100) NOT NULL,
  `chinese_name` varchar(100) NOT NULL,
  `status` int(11) NOT NULL COMMENT "1:active, 9:suspended",
  `creation_date` datetime NOT NULL,
  `last_modified_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY idx_code(`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

