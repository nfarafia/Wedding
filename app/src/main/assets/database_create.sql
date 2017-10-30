BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS `budget_payments` (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`id_cost`	INTEGER DEFAULT NULL,
	`name`	TEXT DEFAULT NULL,
	`name_en`	TEXT DEFAULT NULL,
	`name_ru`	TEXT DEFAULT NULL,
	`amount`	REAL NOT NULL DEFAULT 1,
	`date`	TEXT DEFAULT NULL,
	`complete`	INTEGER NOT NULL DEFAULT 0,
	`notification`	TEXT DEFAULT NULL,
	`update`	TEXT DEFAULT NULL,
	`active`	INTEGER NOT NULL DEFAULT 1,
	FOREIGN KEY(`id_cost`) REFERENCES `budget_costs`(`_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);
INSERT INTO `budget_payments` VALUES (1,1,NULL,'Payment 1','Платеж 1',10.1,'2017-10-13 01:23',1,NULL,NULL,1);
INSERT INTO `budget_payments` VALUES (2,1,NULL,'Payment 2','Платеж 2',20.22,'2017-10-26 01:23',0,NULL,NULL,1);
INSERT INTO `budget_payments` VALUES (3,2,NULL,'Payment 3 (static)',NULL,30.33,'2017-10-26 01:23',1,NULL,NULL,1);
INSERT INTO `budget_payments` VALUES (4,3,NULL,'Payment 4','Платеж 4',40.44,'2017-10-26 01:23',1,NULL,NULL,1);
INSERT INTO `budget_payments` VALUES (5,4,NULL,'Payment 5','Платеж 5',50.55,'2017-10-29 01:23',0,NULL,NULL,1);
INSERT INTO `budget_payments` VALUES (6,5,NULL,'Payment 6','Платеж 6',60.66,'2017-10-26 01:23',1,NULL,NULL,1);
INSERT INTO `budget_payments` VALUES (7,5,NULL,'Payment 7','Платеж 7',70.77,'2017-10-27 01:23',0,NULL,NULL,1);
INSERT INTO `budget_payments` VALUES (8,5,NULL,'Payment 8','Платеж 8',80.88,'2017-10-27 01:23',1,NULL,NULL,1);
INSERT INTO `budget_payments` VALUES (9,6,NULL,'Payment 9','Платеж 9',90.99,'2017-10-26 01:23',1,NULL,NULL,1);
INSERT INTO `budget_payments` VALUES (10,7,NULL,'Payment 10 (static)',NULL,100.0,'2017-10-27 01:23',0,NULL,NULL,1);
INSERT INTO `budget_payments` VALUES (11,1,NULL,'Payment 11','Платеж 11',110.01,'2017-10-28 01:23',0,NULL,NULL,1);
INSERT INTO `budget_payments` VALUES (12,1,NULL,'Payment 12','Платеж 12',120.02,'2017-07-13 01:23',1,NULL,NULL,1);
INSERT INTO `budget_payments` VALUES (13,1,NULL,'Payment 13 (static)',NULL,130.03,'2017-10-27 01:23',0,NULL,NULL,1);
CREATE TABLE IF NOT EXISTS `budget_costs` (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`id_category`	INTEGER NOT NULL,
	`name`	TEXT DEFAULT NULL,
	`name_en`	TEXT DEFAULT NULL,
	`name_ru`	TEXT DEFAULT NULL,
	`note`	TEXT DEFAULT NULL,
	`note_en`	TEXT DEFAULT NULL,
	`note_ru`	TEXT DEFAULT NULL,
	`amount`	REAL NOT NULL DEFAULT 0,
	`update`	TEXT DEFAULT NULL,
	`active`	INTEGER NOT NULL DEFAULT 1,
	FOREIGN KEY(`id_category`) REFERENCES `budget_categories`(`_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);
INSERT INTO `budget_costs` VALUES (1,1,NULL,'Cost 1','Счет 1',NULL,NULL,NULL,100.1,NULL,1);
INSERT INTO `budget_costs` VALUES (2,1,NULL,'Cost 2 (note)','Счет 2 (заметка)',NULL,'Note 2 (static)',NULL,200.22,NULL,1);
INSERT INTO `budget_costs` VALUES (3,1,NULL,'Cost 3 (static)',NULL,NULL,NULL,NULL,300.33,NULL,1);
INSERT INTO `budget_costs` VALUES (4,2,NULL,'Cost 4','Счет 4',NULL,NULL,NULL,400.44,NULL,1);
INSERT INTO `budget_costs` VALUES (5,2,NULL,'Cost 5','Счет 5',NULL,NULL,NULL,500.55,NULL,1);
INSERT INTO `budget_costs` VALUES (6,3,NULL,'Cost 6 (note)','Счет 6 (заметка)',NULL,'Note 6','Заметка 6',600.66,NULL,1);
INSERT INTO `budget_costs` VALUES (7,4,NULL,'Cost 7','Счет 7',NULL,NULL,NULL,700.77,NULL,1);
INSERT INTO `budget_costs` VALUES (8,4,NULL,'Cost 8','Счет 8',NULL,NULL,NULL,800.88,NULL,1);
INSERT INTO `budget_costs` VALUES (9,4,NULL,'Cost 9','Счет 9',NULL,NULL,NULL,900.99,NULL,1);
INSERT INTO `budget_costs` VALUES (10,5,NULL,'Cost 10 (note, static)',NULL,NULL,'Note 10','Заметка 10',1000.0,NULL,1);
CREATE TABLE IF NOT EXISTS `budget_categories` (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`name`	TEXT DEFAULT NULL,
	`name_en`	TEXT DEFAULT NULL,
	`name_ru`	TEXT DEFAULT NULL,
	`position`	INTEGER DEFAULT NULL,
	`update`	TEXT DEFAULT NULL,
	`active`	INTEGER NOT NULL DEFAULT 1
);
INSERT INTO `budget_categories` VALUES (1,NULL,'Attire & Accessories','Одежда & Aксессуары',0,NULL,1);
INSERT INTO `budget_categories` VALUES (2,NULL,'Health & Beauty','Красота & Здоровье',1,NULL,1);
INSERT INTO `budget_categories` VALUES (3,NULL,'Music','Музыка',2,NULL,1);
INSERT INTO `budget_categories` VALUES (4,NULL,'Flowers','Цветы',3,NULL,1);
INSERT INTO `budget_categories` VALUES (5,NULL,'Jewelry','Ювелирные изделия',4,NULL,1);
INSERT INTO `budget_categories` VALUES (6,NULL,'Photo & Video','Фото & Видео',5,NULL,1);
INSERT INTO `budget_categories` VALUES (7,NULL,'Ceremony','Церемония',6,NULL,1);
INSERT INTO `budget_categories` VALUES (8,NULL,'Reception','Вечеринка',7,NULL,1);
INSERT INTO `budget_categories` VALUES (9,NULL,'Transportation','Транспорт',8,NULL,1);
INSERT INTO `budget_categories` VALUES (10,NULL,'Accommodation','Жилье',9,NULL,1);
INSERT INTO `budget_categories` VALUES (11,NULL,'Miscellaneous','Разное',10,NULL,1);
COMMIT;
