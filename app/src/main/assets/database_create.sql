BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS `budget_coasts` (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`id_category`	INTEGER NOT NULL,
	`name`	TEXT NOT NULL,
	`note`	TEXT DEFAULT NULL,
	`amount`	REAL NOT NULL,
	`complete`	INTEGER NOT NULL DEFAULT 0,
	`update`	TEXT DEFAULT NULL,
	FOREIGN KEY(`id_category`) REFERENCES `budget_categories`(`_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);
INSERT INTO `budget_coasts` VALUES (1,1,'Coast 1',NULL,100.0,0,NULL);
INSERT INTO `budget_coasts` VALUES (2,1,'Coast 2 (note)','Note 2',200.0,0,NULL);
INSERT INTO `budget_coasts` VALUES (3,1,'Coast 3',NULL,300.0,0,NULL);
INSERT INTO `budget_coasts` VALUES (4,2,'Coast 4',NULL,400.0,0,NULL);
INSERT INTO `budget_coasts` VALUES (5,2,'Coast 5 (complete)',NULL,500.0,1,NULL);
INSERT INTO `budget_coasts` VALUES (6,3,'Coast 6 (note, complete)','Note 6',600.0,1,NULL);
INSERT INTO `budget_coasts` VALUES (7,4,'Coast 7',NULL,700.0,0,NULL);
INSERT INTO `budget_coasts` VALUES (8,4,'Coast 8',NULL,800.0,0,NULL);
INSERT INTO `budget_coasts` VALUES (9,4,'Coast 9',NULL,900.0,0,NULL);
INSERT INTO `budget_coasts` VALUES (10,5,'Coast 10 (note)','Note 10',1000.0,0,NULL);
CREATE TABLE IF NOT EXISTS `budget_categories` (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`name`	TEXT NOT NULL,
	`position`	INTEGER DEFAULT NULL,
	`timestamp`	INTEGER DEFAULT NULL
);
INSERT INTO `budget_categories` VALUES (1,'Attire & Accessories',1,NULL);
INSERT INTO `budget_categories` VALUES (2,'Health & Beauty',2,NULL);
INSERT INTO `budget_categories` VALUES (3,'Music',3,NULL);
INSERT INTO `budget_categories` VALUES (4,'Flowers',4,NULL);
INSERT INTO `budget_categories` VALUES (5,'Jewelry',5,NULL);
INSERT INTO `budget_categories` VALUES (6,'Photo & Video',6,NULL);
INSERT INTO `budget_categories` VALUES (7,'Ceremony',7,NULL);
INSERT INTO `budget_categories` VALUES (8,'Reception',8,NULL);
INSERT INTO `budget_categories` VALUES (9,'Transportation',9,NULL);
INSERT INTO `budget_categories` VALUES (10,'Accommodation',10,NULL);
INSERT INTO `budget_categories` VALUES (11,'Miscellaneous',11,NULL);
COMMIT;
