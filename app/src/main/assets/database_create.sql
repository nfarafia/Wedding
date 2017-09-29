BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS `coasts_sections` (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`name`	TEXT NOT NULL,
	`position`	INTEGER DEFAULT NULL
);
INSERT INTO `coasts_sections` VALUES (1,'Attire & Accessories',1);
INSERT INTO `coasts_sections` VALUES (2,'Health & Beauty',2);
INSERT INTO `coasts_sections` VALUES (3,'Music',3);
INSERT INTO `coasts_sections` VALUES (4,'Flowers',4);
INSERT INTO `coasts_sections` VALUES (5,'Jewelry',5);
INSERT INTO `coasts_sections` VALUES (6,'Photo & Video',6);
INSERT INTO `coasts_sections` VALUES (7,'Ceremony',7);
INSERT INTO `coasts_sections` VALUES (8,'Reception',8);
INSERT INTO `coasts_sections` VALUES (9,'Transportation',9);
INSERT INTO `coasts_sections` VALUES (10,'Accommodation',10);
INSERT INTO `coasts_sections` VALUES (11,'Miscellaneous',11);
CREATE TABLE IF NOT EXISTS `coasts` (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`id_section`	INTEGER DEFAULT NULL,
	`productname`	TEXT NOT NULL,
	`quantity`	INTEGER DEFAULT NULL,
	FOREIGN KEY(`id_section`) REFERENCES `coasts_sections`(`_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);
INSERT INTO `coasts` VALUES (1,1,'Coast 1',1);
INSERT INTO `coasts` VALUES (2,1,'Coast 2',2);
INSERT INTO `coasts` VALUES (3,1,'Coast 3',3);
INSERT INTO `coasts` VALUES (4,2,'Coast 4',4);
INSERT INTO `coasts` VALUES (5,2,'Coast 5',5);
INSERT INTO `coasts` VALUES (6,3,'Coast 6',6);
INSERT INTO `coasts` VALUES (7,4,'Coast 7',7);
INSERT INTO `coasts` VALUES (8,4,'Coast 8',8);
INSERT INTO `coasts` VALUES (9,4,'Coast 9',9);
INSERT INTO `coasts` VALUES (10,5,'Coast 10',10);
COMMIT;
