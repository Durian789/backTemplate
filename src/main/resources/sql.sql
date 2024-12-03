CREATE TABLE `kafka_group_config` (
                                      `ID` INT NOT NULL AUTO_INCREMENT,
                                      `group_name` VARCHAR(255) DEFAULT NULL,
                                      `flag` TINYINT(1) DEFAULT NULL,
                                      PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `kafka_group_topic` (
                                     `ID` INT NOT NULL AUTO_INCREMENT,
                                     `topic_name` VARCHAR(255) DEFAULT NULL,
                                     `group_name` VARCHAR(255) DEFAULT NULL,
                                     `lag_count` INT DEFAULT NULL,
                                     `topic_count` INT DEFAULT NULL,
                                     `lag_ratio` DOUBLE DEFAULT NULL,
                                     `send_alarm_flag` TINYINT(1) DEFAULT NULL,
                                     `create_time` DATETIME DEFAULT NULL,
                                     PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `kafka_topic_config` (
                                      `ID` INT NOT NULL AUTO_INCREMENT,
                                      `topic_name` VARCHAR(255) DEFAULT NULL,
                                      `flag` TINYINT(1) DEFAULT NULL,
                                      PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;