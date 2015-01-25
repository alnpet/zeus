CREATE TABLE `pet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `category` varchar(100) NOT NULL,
  `token` varchar(100) NOT NULL,
  `device` varchar(100) DEFAULT NULL,
  `gender` varchar(20) DEFAULT NULL,
  `age` double DEFAULT NULL,
  `weight` double DEFAULT NULL,
  `nickname` varchar(100) DEFAULT NULL COMMENT "owner's nick name",
  `phone` varchar(100) DEFAULT NULL COMMENT "owner's phone number",
  `email` varchar(100) DEFAULT NULL COMMENT "owner's email address",
  `creation_date` datetime NOT NULL,
  `last_modified_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_device`(`device`),
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
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT "No.",
  `name` varchar(100) NOT NULL COMMENT "Name",
  `type` int(11) NOT NULL COMMENT "种类. 1:small, 2:medium, 3:big",
  `male_weight_min` double NOT NULL COMMENT "成年公犬体重下限（Kg）",
  `male_weight_max` double NOT NULL COMMENT "成年公犬体重上限（Kg）",
  `male_weight_avg` double NOT NULL COMMENT "成年公犬体重平均（Kg）",
  `male_kid_weight` double NOT NULL COMMENT "公幼犬体重（CM）",
  `male_height` double NOT NULL COMMENT "成年公犬身高（CM）",
  `male_energy` double NOT NULL COMMENT "成年公犬需要卡路里（Kcal）",
  `male_feed` double NOT NULL COMMENT "成年公犬喂食量（g)",
  `female_weight_min` double NOT NULL COMMENT "成年母犬体重下限（Kg）",
  `female_weight_max` double NOT NULL COMMENT "成年母犬体重上限（Kg）",
  `female_weight_avg` double NOT NULL COMMENT "成年母犬体重平均（Kg）",
  `female_kid_weight` double NOT NULL COMMENT "公幼犬体重（CM）",
  `female_height` double NOT NULL COMMENT "成年公犬身高（CM）",
  `female_energy` double NOT NULL COMMENT "成年公犬需要卡路里（Kcal）",
  `female_feed` double NOT NULL COMMENT "成年公犬喂食量（g)",
  `month_to_growup` int(11) NOT NULL COMMENT "成年月数(月）",
  `status` int(11) NOT NULL COMMENT "1:active, 2:inactive",
  `creation_date` datetime NOT NULL,
  `last_modified_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY idx_code(`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pet_id` int(11) NOT NULL,
  `key` varchar(100) NOT NULL,
  `value` varchar(100) NOT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_pet_key` (`pet_id`, `key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
