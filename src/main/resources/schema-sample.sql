-- 09.07.2020 Konto + sub, antrag + sub

-- SpringBank DB:

CREATE USER 'springbank'@'localhost' IDENTIFIED BY 'springbankpw';
CREATE USER 'springbank'@'%' IDENTIFIED BY 'springbankpw';

GRANT ALL PRIVILEGES ON * . * TO 'springbank'@'localhost';
GRANT ALL PRIVILEGES ON * . * TO 'springbank'@'%';

CREATE DATABASE  IF NOT EXISTS `spring_bank_test`;
USE `spring_bank_test`;


----------------------------------------------------
-- some test Data
----------------------------------------------------

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

insert into adresse
(id, strasse, plz, ort, land)
values
(3, "TestStraße 35", '1100','Wien', 'Österreich') ;

insert into person
(id, nachname, vorname, adresse_id)
values
(3, 'McTestFace', 'Testi', 3) ;

insert into kunde
(id, email_adresse, telefon_nummer, kundennummer, first_login_done, has_acceptedagb, is_active, is_legi, password, rolle)
values
(3, 'test@test.at','123456','123',true,true,true,true, '$2y$12$yfuEHL2ycFi5oJ6KCqxOceiZaT0N2ukxFNPXZqQZKh.9KErt9lRYm', 'customer') ;


insert into kontoantrag
(id, antrag_datum, antrag_status, kundennummer)
values
(2, curdate(), 'EINGEREICHT', 123);

insert into kontoantrag
(id, antrag_datum, antrag_status, kundennummer)
values
(3, curdate(), 'ABGELEHNT', 123);

insert into kontoantrag
(id, antrag_datum, antrag_status, kundennummer)
values
(4, curdate(), 'GENEHMIGT', 123);

insert into kontoantrag
(id, antrag_datum, antrag_status, kundennummer)
values
(5, curdate(), 'GENEHMIGT', 123);

insert into kontoantrag
(id, antrag_datum, antrag_status, kundennummer)
values
(6, curdate(), 'EINGEREICHT', 123);

insert into kontoantrag
(id, antrag_datum, antrag_status, kundennummer)
values
(7, curdate(), 'GENEHMIGT', 123);

insert into kontoantrag
(id, antrag_datum, antrag_status, kundennummer)
values
(8, curdate(), 'EINGEREICHT', 123);

insert into kontoantrag
(id, antrag_datum, antrag_status, kundennummer)
values
(9, curdate(), 'EINGEREICHT', 123);

insert into kontoantrag
(id, antrag_datum, antrag_status, kundennummer)
values
(10, curdate(), 'GENEHMIGT', 123);


insert into sparkontoantrag
(dauer_auftrag, erst_auftrag, id)
values
(250.00, 5000.00, 1) ;


insert into sparkontoantrag
(dauer_auftrag, erst_auftrag, id)
values
(null, null, 2) ;


insert into sparkontoantrag
(dauer_auftrag, erst_auftrag, id)
values
(null, null, 3);


insert into sparkontoantrag
(dauer_auftrag, erst_auftrag, id)
values
(null, null, 4);


insert into sparkontoantrag
(dauer_auftrag, erst_auftrag, id)
values
(null, null, 5);

insert into kreditkontoantrag
(gesamt_belastung, kredit_betrag, laufzeit, rate, zinssatz, id)
values
(21838.80, 15000.00, 120.00, 181.99, 8.00, 6) ;

insert into kreditkontoantrag
(gesamt_belastung, kredit_betrag, laufzeit, rate, zinssatz, id)
values
(21838.80, 15000.00, 120.00, 181.99, 8.00, 7) ;

insert into girokontoantrag
(ueberziehungsrahmen_gewuenscht, id)
values
(false, 9) ;

insert into girokontoantrag
(ueberziehungsrahmen_gewuenscht, id)
values
(false, 10) ;

insert into girokontoantrag
(ueberziehungsrahmen_gewuenscht, id)
values
(true, 8) ;


insert into konto
(id, akt_saldo, eroeffnungs_datum, konto_status, kontonummer, konto_antrag_id, kunde_id)
values
(1, 12345.67, curdate(),'OFFEN',123003,10,3) ;

insert into konto
(id, akt_saldo, eroeffnungs_datum, konto_status, kontonummer, konto_antrag_id, kunde_id)
values
(2, -15000.00, curdate(),'OFFEN',123004,7,3) ;

insert into konto
(id, akt_saldo, eroeffnungs_datum, konto_status, kontonummer, konto_antrag_id, kunde_id)
values
(3, 2500.00, curdate(),'OFFEN',123001,4,3) ;

insert into konto
(id, akt_saldo, eroeffnungs_datum, konto_status, kontonummer, konto_antrag_id, kunde_id)
values
(4, 5000.00, curdate(),'OFFEN',123002,5,3) ;

insert into sparkonto
(connected_giro, id)
values
(123456789, 3) ;

insert into sparkonto
(connected_giro, id)
values
(123456789, 4) ;

insert into girokonto
(ueberziehungs_rahmen, id)
values
(500.00, 1);

insert into kreditkonto
(kredit_betrag, laufzeit, rate, id)
values
(15000.00, 120.00, 181.99, 2) ;




commit ;



-------------------------------------------
-- Drop Tables for Greenfield-Setup
-------------------------------------------
DROP TABLE IF EXISTS `map_mita_role`;
DROP TABLE IF EXISTS `rolle`;
DROP TABLE IF EXISTS `bank`;
DROP TABLE IF EXISTS `login_credentials`;
DROP TABLE IF EXISTS `mitarbeiter`;
DROP TABLE IF EXISTS `sparkonto`;
DROP TABLE IF EXISTS `kreditkonto`;
DROP TABLE IF EXISTS `girokonto`;
DROP TABLE IF EXISTS `sparkontoantrag`;
DROP TABLE IF EXISTS `kreditkontoantrag`;
DROP TABLE IF EXISTS `girokontoantrag`;
DROP TABLE IF EXISTS `konto`;
DROP TABLE IF EXISTS `kontoantrag`;
DROP TABLE IF EXISTS `kunde`;
DROP TABLE IF EXISTS `person`;
DROP TABLE IF EXISTS `adresse`;




--------------------------------------------
-- Greenfield Setup
--------------------------------------------

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

DROP TABLE IF EXISTS `kontoantrag`;
CREATE TABLE `kontoantrag` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `antrag_datum` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `antrag_status` varchar(255) DEFAULT NULL,
  `kundennummer` BIGINT DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `sparkontoantrag`;
CREATE TABLE `sparkontoantrag` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `dauer_auftrag` DEC(19,2) DEFAULT NULL,
  `erst_auftrag` DEC(19,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `kreditkontoantrag`;
CREATE TABLE `kreditkontoantrag` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `gesamt_belastung` DEC(19,2) DEFAULT NULL,
  `kredit_betrag` DEC(19,2) DEFAULT NULL,
  `laufzeit` DEC(19,2) DEFAULT NULL,
  `rate` DEC(19,2) DEFAULT NULL,
  `zinssatz` DEC(19,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `girokontoantrag`;
CREATE TABLE `girokontoantrag` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `ueberziehungsrahmen_gewuenscht` BOOLEAN DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `konto`;
CREATE TABLE `konto` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `akt_saldo` DEC(19,2) DEFAULT NULL,
  `eroeffnungs_datum` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `konto_status` varchar(255) DEFAULT NULL,
  `kontonummer` BIGINT DEFAULT NULL,
  `konto_antrag_id` BIGINT DEFAULT NULL,
  `kunde_id` BIGINT DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`id`),
  KEY `FK_KONT_ANTR_idx` (`konto_antrag_id`),
  CONSTRAINT `FK_KONT_ANTR` FOREIGN KEY (`konto_antrag_id`) REFERENCES `kontoantrag` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  KEY `FK_KONT_KUND_idx` (`kunde_id`),
  CONSTRAINT `FK_KONT_KUND` FOREIGN KEY (`kunde_id`) REFERENCES `kunde` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION

) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `sparkonto`;
CREATE TABLE `sparkonto` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `connected_giro` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `kreditkonto`;
CREATE TABLE `kreditkonto` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `kredit_betrag` DEC(19,2) DEFAULT NULL,
  `laufzeit` DEC(19,2) DEFAULT NULL,
  `rate` DEC(19,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `girokonto`;
CREATE TABLE `girokonto` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `ueberziehungs_rahmen` DEC(19,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
