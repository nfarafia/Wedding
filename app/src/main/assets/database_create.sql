BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS `budget_costs` (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`id_category`	INTEGER NOT NULL,
	`name`	TEXT DEFAULT NULL,
	`name_en`	TEXT DEFAULT NULL,
	`name_ru`	TEXT DEFAULT NULL,
	`note`	TEXT DEFAULT NULL,
	`amount`	REAL NOT NULL DEFAULT 0,
	`complete`	INTEGER NOT NULL DEFAULT 0,
	`update`	TEXT DEFAULT NULL,
	FOREIGN KEY(`id_category`) REFERENCES `budget_categories`(`_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);
INSERT INTO `budget_costs` VALUES (1,1,NULL,'Cost 1','Счет 1',NULL,100.0,0,NULL);
INSERT INTO `budget_costs` VALUES (2,1,NULL,'Cost 2 (note)','Счет 2 (заметка)','Note 2',200.0,0,NULL);
INSERT INTO `budget_costs` VALUES (3,1,NULL,'Cost 3 (static)',NULL,NULL,300.0,0,NULL);
INSERT INTO `budget_costs` VALUES (4,2,NULL,'Cost 4','Счет 4',NULL,400.0,0,NULL);
INSERT INTO `budget_costs` VALUES (5,2,NULL,'Cost 5','Счет 5',NULL,500.0,1,NULL);
INSERT INTO `budget_costs` VALUES (6,3,NULL,'Cost 6 (note)','Счет 6 (заметка)','Note 6',600.0,1,NULL);
INSERT INTO `budget_costs` VALUES (7,4,NULL,'Cost 7','Счет 7',NULL,700.0,0,NULL);
INSERT INTO `budget_costs` VALUES (8,4,NULL,'Cost 8','Счет 8',NULL,800.0,0,NULL);
INSERT INTO `budget_costs` VALUES (9,4,NULL,'Cost 9','Счет 9',NULL,900.0,0,NULL);
INSERT INTO `budget_costs` VALUES (10,5,NULL,'Cost 10 (note, static)',NULL,'Note 10',1000.0,0,NULL);
CREATE TABLE IF NOT EXISTS `budget_categories` (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`name`	TEXT DEFAULT NULL,
	`name_en`	TEXT DEFAULT NULL,
	`name_ru`	TEXT DEFAULT NULL,
	`position`	INTEGER DEFAULT NULL,
	`update`	TEXT DEFAULT NULL
);
INSERT INTO `budget_categories` VALUES (1,NULL,'Attire & Accessories','Одежда & Aксессуары',1,NULL);
INSERT INTO `budget_categories` VALUES (2,NULL,'Health & Beauty','Красота & Здоровье',2,NULL);
INSERT INTO `budget_categories` VALUES (3,NULL,'Music','Музыка',3,NULL);
INSERT INTO `budget_categories` VALUES (4,NULL,'Flowers','Цветы',4,NULL);
INSERT INTO `budget_categories` VALUES (5,NULL,'Jewelry','Ювелирные изделия',5,NULL);
INSERT INTO `budget_categories` VALUES (6,NULL,'Photo & Video','Фото & Видео',6,NULL);
INSERT INTO `budget_categories` VALUES (7,NULL,'Ceremony','Церемония',7,NULL);
INSERT INTO `budget_categories` VALUES (8,NULL,'Reception','Вечеринка',8,NULL);
INSERT INTO `budget_categories` VALUES (9,NULL,'Transportation','Транспорт',9,NULL);
INSERT INTO `budget_categories` VALUES (10,NULL,'Accommodation','Жилье',10,NULL);
INSERT INTO `budget_categories` VALUES (11,NULL,'Miscellaneous','Разное',11,NULL);
COMMIT;
