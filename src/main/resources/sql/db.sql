CREATE DATABASE IF NOT EXISTS SpringBootAssignment;

USE SpringBootAssignment;

CREATE TABLE `User` (
                        `id` BIGINT NOT NULL,
                        `name` VARCHAR(255),
                        `username` VARCHAR(255),
                        `email` VARCHAR(255),
                        `phone` VARCHAR(255),
                        `website` VARCHAR(255),
                        `street` VARCHAR(255),
                        `suite` VARCHAR(255),
                        `city` VARCHAR(255),
                        `zipcode` VARCHAR(255),
                        `lat` VARCHAR(255),
                        `lng` VARCHAR(255),
                        `company_name` VARCHAR(255),
                        `catch_Phrase` VARCHAR(255),
                        `bs` VARCHAR(255),
                        PRIMARY KEY (`id`)
);

CREATE TABLE `Album` (
                         `id` BIGINT NOT NULL,
                         `user_Id` BIGINT,
                         `title` VARCHAR(255),
                         PRIMARY KEY (`id`),
                         FOREIGN KEY (`user_Id`) REFERENCES `User`(`id`)
);

CREATE TABLE `Photo` (
                         `id` BIGINT NOT NULL,
                         `album_Id` BIGINT,
                         `title` VARCHAR(255),
                         `url` VARCHAR(255),
                         `thumbnail_Url` VARCHAR(255),
                         PRIMARY KEY (`id`),
                         FOREIGN KEY (`album_Id`) REFERENCES `Album`(`id`)
);


CREATE TABLE `customer` (
                            `customer_id` int NOT NULL AUTO_INCREMENT,
                            `name` varchar(100) NOT NULL,
                            `email` varchar(100) NOT NULL,
                            `mobile_number` varchar(20) NOT NULL,
                            `pwd` varchar(500) NOT NULL,
                            `role` varchar(100) NOT NULL,
                            `create_dt` date DEFAULT NULL,
                            PRIMARY KEY (`customer_id`)
);

INSERT INTO `customer` (`name`,`email`,`mobile_number`, `pwd`, `role`,`create_dt`)
VALUES ('kevin','kevin.boumerheb@icloud.com','70678969', '$2y$12$oRRbkNfwuR8ug4MlzH5FOeui.//1mkd.RsOAJMbykTSupVy.x/vb2','admin',CURDATE());


CREATE TABLE `authorities` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `customer_id` int NOT NULL,
                               `name` varchar(50) NOT NULL,
                               PRIMARY KEY (`id`),
                               KEY `customer_id` (`customer_id`),
                               CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
);

INSERT INTO `authorities` (`customer_id`, `name`)
VALUES (1, 'ROLE_ADMIN');