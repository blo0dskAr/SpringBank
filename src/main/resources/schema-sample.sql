SpringBank DB:

CREATE USER 'springbank'@'localhost' IDENTIFIED BY 'springbankpw';
CREATE USER 'springbank'@'%' IDENTIFIED BY 'springbankpw';

GRANT ALL PRIVILEGES ON * . * TO 'springbank'@'localhost';
GRANT ALL PRIVILEGES ON * . * TO 'springbank'@'%';

CREATE DATABASE  IF NOT EXISTS `spring_bank_test`;
USE `spring_bank_test`;


insert into adresse
(id, strasse, plz, ort, land)
values
(1, "Simmeringer Hauptstraße 29", "1110", "Wien", "Österreich") ;

insert into adresse
(id, strasse, plz, ort, land)
values
(2, "Quellenstraße 114", "1100", "Wien", "Österreich") ;


insert into person
(id, nachname, vorname, adresse_id)
values
(1, "Hans", "Wurst", 1) ;


insert into person
(id, nachname, vorname, adresse_id)
values
(2, "Melinda", "Wurst", 2) ;

insert into mitarbeiter
(id, position, mitarbeiternummer)
values
(1, "dev/ops Engineer", 666) ;

insert into mitarbeiter
(id, position, mitarbeiternummer)
values
(2, "Business-Analyst", 667) ;

insert into login_credentials
(id, is_active, login_name, password, mitarbeiter_id)
values
(1, 1, "hwurst", "$2y$12$yfuEHL2ycFi5oJ6KCqxOceiZaT0N2ukxFNPXZqQZKh.9KErt9lRYm", 1);

insert into login_credentials
(id, is_active, login_name, password, mitarbeiter_id)
values
(2, 1, "mwurst", "$2y$12$yfuEHL2ycFi5oJ6KCqxOceiZaT0N2ukxFNPXZqQZKh.9KErt9lRYm", 2) ;


insert into rolle
(id, name)
values
(1, "admin") ;

insert into rolle
(id, name)
values
(2, "mitarbeiter") ;

insert into map_mita_role
(mita_id, role_id)
values
(1, 1) ;

insert into map_mita_role
(mita_id, role_id)
values
(2, 2) ;

insert into bank
(id, firmen_chef, firmen_name, steuer_nummer)
values
(1, "blo0dy", "blo0dy Investments From Hell Inc.", 12345666) ;

commit ;


DROP TABLE IF EXISTS `map_mita_role`;
DROP TABLE IF EXISTS `rolle`;
DROP TABLE IF EXISTS `bank`;
DROP TABLE IF EXISTS `login_credentials`;
DROP TABLE IF EXISTS `kunde`;
DROP TABLE IF EXISTS `mitarbeiter`;
DROP TABLE IF EXISTS `person`;
DROP TABLE IF EXISTS `adresse`;





DROP TABLE IF EXISTS `bank`;
CREATE TABLE `bank` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `firmen_chef` varchar(45) DEFAULT NULL,
  `firmen_name` varchar(45) DEFAULT NULL,
  `steuer_nummer` BIGINT DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `adresse`;
CREATE TABLE `adresse` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `strasse` varchar(255) DEFAULT NULL,
  `plz` varchar(255) DEFAULT NULL,
  `ort` varchar(255) DEFAULT NULL,
  `land` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `person`;
CREATE TABLE `person` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nachname` varchar(255) DEFAULT NULL,
  `vorname` varchar(255) DEFAULT NULL,
  `adresse_id` BIGINT DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`id`),
  KEY `FK_PERS_ADRS_idx` (`adresse_id`),
  CONSTRAINT `FK_PERS_ADRS` FOREIGN KEY (`adresse_id`) REFERENCES `adresse` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `mitarbeiter`;
CREATE TABLE `mitarbeiter` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `mitarbeiternummer` varchar(45) DEFAULT NULL,
  `position` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`id`),
  KEY `FK_MITA_PERS_idx` (`id`),
  CONSTRAINT `FK_MITA_PERS` FOREIGN KEY (`id`) REFERENCES `person` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;




DROP TABLE IF EXISTS `login_credentials`;
CREATE TABLE `login_credentials` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `is_active` boolean DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `mitarbeiter_id` BIGINT DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`id`),
  KEY `FK_LOCR_MITA_idx` (`mitarbeiter_id`),
  CONSTRAINT `FK_LOCR_MITA` FOREIGN KEY (`mitarbeiter_id`) REFERENCES `mitarbeiter` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



DROP TABLE IF EXISTS `kunde`;
CREATE TABLE `kunde` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `email_adresse` varchar(255) DEFAULT NULL,
  `telefon_nummer` varchar(255) DEFAULT NULL,
  `kundennummer` varchar(255) DEFAULT NULL,
  `first_login_done` BOOLEAN DEFAULT NULL,
  `has_acceptedagb` BOOLEAN DEFAULT NULL,
  `is_active` BOOLEAN DEFAULT NULL,
  `is_legi` BOOLEAN DEFAULT NULL,
  `password` VARCHAR(255) DEFAULT NULL,
  `rolle` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`id`),
  KEY `FK_KUND_PERS_idx` (`id`),
  CONSTRAINT `FK_KUND_PERS` FOREIGN KEY (`id`) REFERENCES `person` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `rolle`;
CREATE TABLE `rolle` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `map_mita_role`;
CREATE TABLE `map_mita_role` (
  `mita_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
	CONSTRAINT `FK_MAP_MITA_ROLE` FOREIGN KEY (`mita_id`) REFERENCES `person` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT `FK_MAP_ROLE_MITA` FOREIGN KEY (`role_id`) REFERENCES `rolle` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    UNIQUE (`mita_id`, `role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

