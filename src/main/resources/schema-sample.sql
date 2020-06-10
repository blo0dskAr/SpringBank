CREATE DATABASE IF NOT EXISTS `spring_bank_test`;
USE `spring_bank_test`;

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
  `gueltig_ab` datetime DEFAULT NULL,
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
  `kundennummer` INT DEFAULT NULL,
  `first_login_done` BOOLEAN DEFAULT NULL,
  `has_acceptedagb` BOOLEAN DEFAULT NULL,
  `is_active` BOOLEAN DEFAULT NULL,
  `is_legi` BOOLEAN DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`id`),
  KEY `FK_KUND_PERS_idx` (`id`),
  CONSTRAINT `FK_KUND_PERS` FOREIGN KEY (`id`) REFERENCES `person` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


