CREATE TABLE IF NOT EXISTS `items` (
  `owner_id` VARCHAR(36),
  `object_id` INT NOT NULL DEFAULT 0,
  `item_id` SMALLINT UNSIGNED NOT NULL,
  `count` INT UNSIGNED NOT NULL DEFAULT 0,
  `enchant_level` SMALLINT UNSIGNED NOT NULL DEFAULT 0,
  `item_location` VARCHAR(10),
  `item_location_data` INT,
  `custom_type1` INT NOT NULL DEFAULT 0,
  `custom_type2` INT NOT NULL DEFAULT 0,
  `duration_left` INT NOT NULL DEFAULT -1,
  `create_time` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`object_id`)
);