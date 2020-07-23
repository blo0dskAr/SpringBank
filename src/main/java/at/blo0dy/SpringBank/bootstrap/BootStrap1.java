package at.blo0dy.SpringBank.bootstrap;

import at.blo0dy.SpringBank.Bank;
import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import at.blo0dy.SpringBank.model.enums.*;
import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import at.blo0dy.SpringBank.model.konto.kontoBuchung.KontoBuchung;
import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.model.person.mitarbeiter.loginCredentials.LoginCredentials;
import at.blo0dy.SpringBank.model.person.rolle.Rolle;
import at.blo0dy.SpringBank.model.produkt.zinssatz.Zinssatz;
import at.blo0dy.SpringBank.service.*;
import at.blo0dy.SpringBank.service.adresse.AdresseService;
import at.blo0dy.SpringBank.service.bank.BankService;
import at.blo0dy.SpringBank.service.konto.giro.GiroKontoAntragService;
import at.blo0dy.SpringBank.service.konto.giro.GiroService;
import at.blo0dy.SpringBank.service.konto.kontoBuchung.KontoBuchungService;
import at.blo0dy.SpringBank.service.konto.kredit.KreditKontoAntragService;
import at.blo0dy.SpringBank.service.konto.kredit.KreditService;
import at.blo0dy.SpringBank.service.konto.sparen.SparKontoAntragService;
import at.blo0dy.SpringBank.service.konto.sparen.SparService;
import at.blo0dy.SpringBank.service.konto.zahlungsAuftrag.ZahlungsAuftragService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import at.blo0dy.SpringBank.service.produkt.zinssatz.ZinssatzService;
import at.blo0dy.SpringBank.service.rolle.RolleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
@Profile("dev")
public class BootStrap1 implements CommandLineRunner {

  private final MitarbeiterService mitarbeiterService;
  private final AdresseService adresseService;
  private final KundeService kundeService;
  private final LoginCredentialsService loginCredentialsService;
  private final BankService bankService;
  private final RolleService rolleService;
  private final SparService sparService;
  private final SparKontoAntragService sparKontoAntragService;
  private final KreditKontoAntragService kreditKontoAntragService;
  private final KreditService kreditService;
  private final GiroKontoAntragService giroKontoAntragService;
  private final GiroService giroService;
  private final KontoBuchungService kontoBuchungService;
  private final ZahlungsAuftragService zahlungsAuftragService;
  private final ZinssatzService zinssatzService;


  public BootStrap1(MitarbeiterService mitarbeiterService, AdresseService adresseService, KundeService kundeService, LoginCredentialsService loginCredentialsService,
                    BankService bankService, RolleService rolleService, SparService sparService, SparKontoAntragService sparKontoAntragService, KreditKontoAntragService kreditKontoAntragService,
                    KreditService kreditService, GiroKontoAntragService giroKontoAntragService, GiroService giroService, KontoBuchungService kontoBuchungService,
                    ZahlungsAuftragService zahlungsAuftragService, ZinssatzService zinssatzService) {
    this.mitarbeiterService = mitarbeiterService;
    this.adresseService = adresseService;
    this.kundeService = kundeService;
    this.loginCredentialsService = loginCredentialsService;
    this.bankService = bankService;
    this.rolleService = rolleService;
    this.sparService = sparService;
    this.sparKontoAntragService = sparKontoAntragService;
    this.kreditKontoAntragService = kreditKontoAntragService;
    this.kreditService = kreditService;
    this.giroKontoAntragService = giroKontoAntragService;
    this.giroService = giroService;
    this.kontoBuchungService = kontoBuchungService;
    this.zahlungsAuftragService = zahlungsAuftragService;
    this.zinssatzService = zinssatzService;
  }

  @Autowired
  LocaleResolver localeResolver;

  @Override
  public void run(String... args) throws Exception {
    loadData();
  }

private void loadData() {

    // Formatter Pattern
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // Bank erstellen
  Bank bank = new Bank();
  bankService.saveBank(bank);

  // Zinssatz erstellen

  Zinssatz zsSparen = new Zinssatz(1L,BigDecimal.valueOf(5), KontoProduktEnum.SPAREN);
  Zinssatz zskredit = new Zinssatz(2L,BigDecimal.valueOf(8), KontoProduktEnum.KREDIT);
  Zinssatz zsGiro = new Zinssatz(3L,BigDecimal.valueOf(1), KontoProduktEnum.GIRO);

  zinssatzService.save(zsSparen);
  zinssatzService.save(zskredit);
  zinssatzService.save(zsGiro);

    // Adressen erstellen
  Adresse adresse = new Adresse();
  adresse.setId(1L);
  adresse.setOrt("Wien");
  adresse.setPlz("1110");
  adresse.setLand("Österreich");
  adresse.setStrasse("Simmeringer Hauptstraße 29");

  Adresse adresse2 = new Adresse();
  adresse2.setId(2L);
  adresse2.setOrt("Wien");
  adresse2.setPlz("1100");
  adresse2.setLand("Österreich");
  adresse2.setStrasse("Quellenstraße 114");

  // Adressen persistieren
  adresseService.save(adresse);
  adresseService.save(adresse2);

  // Rollen erstellen
  Rolle customerRolle = new Rolle();
  customerRolle.setName("customer");


  Rolle adminRolle = new Rolle();
  adminRolle.setName("admin");

  Rolle maRolle = new Rolle();
  maRolle.setName("mitarbeiter");

  rolleService.save(customerRolle);
  rolleService.save(adminRolle);
  rolleService.save(maRolle);

  // Mitarbeiter erstellen
  Mitarbeiter mitarbeiter = new Mitarbeiter();
  mitarbeiter.setVorname("Hans");
  mitarbeiter.setNachname("Wurst");
  mitarbeiter.setGeburtsDatum(LocalDate.of(1984,1,1));
  mitarbeiter.setPosition("Dev/Ops Engineer");
  mitarbeiter.setMitarbeiterNummer("666");
  mitarbeiter.setId(1L);

  // Adresse uebergeben
  mitarbeiter.setAdresse(adresse);

  // Rolle uebergeben
  mitarbeiter.addRolle(adminRolle);

  Mitarbeiter mitarbeiter2 = new Mitarbeiter();
  mitarbeiter2.setVorname("Melinda");
  mitarbeiter2.setNachname("Wurst");
  mitarbeiter2.setGeburtsDatum(LocalDate.of(1984,1,1));
  mitarbeiter2.setPosition("Business Analyst");
  mitarbeiter2.setMitarbeiterNummer("667");
  mitarbeiter2.setId(2L);

  mitarbeiter2.setAdresse(adresse2);
  mitarbeiter2.addRolle(maRolle);

  // Mitarbeiter persistieren
  mitarbeiterService.save(mitarbeiter);
  mitarbeiterService.save(mitarbeiter2);

  // ADresse fuer kunde erstellen
  Adresse adresse3 = new Adresse();
  adresse3.setId(3L);
  adresse3.setOrt("Wien");
  adresse3.setPlz("1230");
  adresse3.setLand("Österreich");
  adresse3.setStrasse("Dumdidumstrasse 15");

  Adresse adresse4 = new Adresse();
  adresse4.setId(4L);
  adresse4.setOrt("Wien");
  adresse4.setPlz("1230");
  adresse4.setLand("Österreich");
  adresse4.setStrasse("Dumdidumstrasse 15");

  // adresse persistieren
  adresseService.save(adresse3);
  adresseService.save(adresse4);

  // Kunde erstellen
  // Kunde 1  hat paar Konten und zahlungsaufträge und buchungen
  Kunde kunde1 = new Kunde();
  kunde1.setKundennummer("123");
  kunde1.setAdresse(adresse3);
  kunde1.setId(3L);
  kunde1.setGeburtsDatum(LocalDate.of(1984,1,1));
  kunde1.setNachname("McKundeFace");
  kunde1.setVorname("Kundy");
  kunde1.setPassword("$2y$12$yfuEHL2ycFi5oJ6KCqxOceiZaT0N2ukxFNPXZqQZKh.9KErt9lRYm");
  kunde1.setRolle("customer");
  kunde1.setTelefonNummer("1234156");
  kunde1.setEmailAdresse("test@test.at");
  kunde1.setConnectedGiro("AT123451234567890123");
  kunde1.setLegi(true);
  kunde1.setActive(true);
  kunde1.setHasAcceptedAGB(true);
  kunde1.setFirstLoginDone(true);

  // Kunde 2 hat paar konten und zahlungsaufträge und buchungen
  Kunde kunde2 = new Kunde();
  kunde2.setKundennummer("124");
  kunde2.setAdresse(adresse4);
  kunde2.setId(4L);
  kunde2.setGeburtsDatum(LocalDate.of(1984,1,1));
  kunde2.setNachname("McTestFace");
  kunde2.setVorname("Testy");
  kunde2.setPassword("$2y$12$yfuEHL2ycFi5oJ6KCqxOceiZaT0N2ukxFNPXZqQZKh.9KErt9lRYm");
  kunde2.setRolle("customer");
  kunde2.setTelefonNummer("12345345");
  kunde2.setEmailAdresse("test@test.at");
  kunde2.setConnectedGiro("AT123451234567890123");
  kunde2.setLegi(true);
  kunde2.setActive(true);
  kunde2.setHasAcceptedAGB(true);
  kunde2.setFirstLoginDone(false);

  // Kunde 3 (hat alles false)
  Kunde kunde3 = new Kunde();
  kunde3.setKundennummer("125");
  kunde3.setAdresse(adresse4);
  kunde3.setId(5L);
  kunde3.setGeburtsDatum(LocalDate.of(1984,1,1));
  kunde3.setNachname("HatAllesFalse");
  kunde3.setVorname("Testy");
  kunde3.setPassword("$2y$12$yfuEHL2ycFi5oJ6KCqxOceiZaT0N2ukxFNPXZqQZKh.9KErt9lRYm");
  kunde3.setRolle("customer");
  kunde3.setTelefonNummer("12345345");
  kunde3.setEmailAdresse("test@test.at");
  kunde3.setConnectedGiro("AT123451234567890123");
  kunde3.setLegi(false);
  kunde3.setActive(false);
  kunde3.setHasAcceptedAGB(false);
  kunde3.setFirstLoginDone(false);

  // Kunde 4 (hat alles nur active false)
  Kunde kunde4 = new Kunde();
  kunde4.setKundennummer("126");
  kunde4.setAdresse(adresse4);
  kunde4.setId(6L);
  kunde4.setGeburtsDatum(LocalDate.of(1984,1,1));
  kunde4.setNachname("HatAllesFalse");
  kunde4.setVorname("Testy");
  kunde4.setPassword("$2y$12$yfuEHL2ycFi5oJ6KCqxOceiZaT0N2ukxFNPXZqQZKh.9KErt9lRYm");
  kunde4.setRolle("customer");
  kunde4.setTelefonNummer("12345345");
  kunde4.setEmailAdresse("test@test.at");
  kunde4.setConnectedGiro("AT123451234567890123");
  kunde4.setLegi(true);
  kunde4.setActive(false);
  kunde4.setHasAcceptedAGB(true);
  kunde4.setFirstLoginDone(true);

  // Kunde 5 (legi und active false)
  Kunde kunde5 = new Kunde();
  kunde5.setKundennummer("127");
  kunde5.setAdresse(adresse4);
  kunde5.setId(7L);
  kunde5.setGeburtsDatum(LocalDate.of(1984,1,1));
  kunde5.setNachname("HatLegiFalse");
  kunde5.setVorname("Testy");
  kunde5.setPassword("$2y$12$yfuEHL2ycFi5oJ6KCqxOceiZaT0N2ukxFNPXZqQZKh.9KErt9lRYm");
  kunde5.setRolle("customer");
  kunde5.setTelefonNummer("12345345");
  kunde5.setEmailAdresse("test@test.at");
  kunde5.setConnectedGiro("AT123451234567890123");
  kunde5.setLegi(false);
  kunde5.setActive(false);
  kunde5.setHasAcceptedAGB(true);
  kunde5.setFirstLoginDone(true);

  // Kunde 6 ( agb und active false)
  Kunde kunde6 = new Kunde();
  kunde6.setKundennummer("128");
  kunde6.setAdresse(adresse4);
  kunde6.setId(8L);
  kunde6.setGeburtsDatum(LocalDate.of(1984,1,1));
  kunde6.setNachname("hatAgbFalse");
  kunde6.setVorname("Testy");
  kunde6.setPassword("$2y$12$yfuEHL2ycFi5oJ6KCqxOceiZaT0N2ukxFNPXZqQZKh.9KErt9lRYm");
  kunde6.setRolle("customer");
  kunde6.setTelefonNummer("12345345");
  kunde6.setEmailAdresse("test@test.at");
  kunde6.setConnectedGiro("AT123451234567890123");
  kunde6.setLegi(true);
  kunde6.setActive(false);
  kunde6.setHasAcceptedAGB(false);
  kunde6.setFirstLoginDone(true);

  // kunde persistieren
  kundeService.save(kunde1);
  kundeService.save(kunde2);
  kundeService.save(kunde3);
  kundeService.save(kunde4);
  kundeService.save(kunde5);
  kundeService.save(kunde6);

  // LoginCredentials erstellen
  LoginCredentials lc1 = new LoginCredentials();
  lc1.setLoginName("hwurst");
  lc1.setPassword("$2y$12$yfuEHL2ycFi5oJ6KCqxOceiZaT0N2ukxFNPXZqQZKh.9KErt9lRYm");
  lc1.setId(1L);
  lc1.setMitarbeiter(mitarbeiter);
  loginCredentialsService.save(lc1);

  LoginCredentials lc2 = new LoginCredentials();
  lc2.setLoginName("mwurst");
  lc2.setPassword("$2y$12$yfuEHL2ycFi5oJ6KCqxOceiZaT0N2ukxFNPXZqQZKh.9KErt9lRYm");
  lc2.setId(2L);
  lc2.setMitarbeiter(mitarbeiter2);
  loginCredentialsService.save(lc2);

  // SparKonto Antrag anlegen
  // kunde 2
  SparKontoAntrag sparKontoAntrag = new SparKontoAntrag();
  sparKontoAntrag.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  sparKontoAntrag.setAntragStatus(AntragStatusEnum.GENEHMIGT);
  sparKontoAntrag.setId(1L);
  sparKontoAntrag.setErstAuftrag(BigDecimal.valueOf(5000));
  sparKontoAntrag.setDauerAuftrag(BigDecimal.valueOf(250));
  sparKontoAntrag.setKundennummer(124L);
  sparKontoAntrag.setProdukt(KontoProduktEnum.SPAREN);
  sparKontoAntrag.setDauerAuftrag(BigDecimal.ZERO);
  sparKontoAntrag.setErstAuftrag(BigDecimal.ZERO);
  sparKontoAntragService.save(sparKontoAntrag);

  // Kunde 1
  SparKontoAntrag sparKontoAntrag1 = new SparKontoAntrag();
  sparKontoAntrag1.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  sparKontoAntrag1.setAntragStatus(AntragStatusEnum.EINGEREICHT);
  sparKontoAntrag1.setId(2L);
  sparKontoAntrag1.setKundennummer(123L);
  sparKontoAntrag1.setProdukt(KontoProduktEnum.SPAREN);
  sparKontoAntrag1.setDauerAuftrag(BigDecimal.ZERO);
  sparKontoAntrag1.setErstAuftrag(BigDecimal.ZERO);
  sparKontoAntragService.save(sparKontoAntrag1);

  // Kunde 1
  SparKontoAntrag sparKontoAntrag2 = new SparKontoAntrag();
  sparKontoAntrag2.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  sparKontoAntrag2.setAntragStatus(AntragStatusEnum.ABGELEHNT);
  sparKontoAntrag2.setId(3L);
  sparKontoAntrag2.setKundennummer(123L);
  sparKontoAntrag2.setProdukt(KontoProduktEnum.SPAREN);
  sparKontoAntrag2.setDauerAuftrag(BigDecimal.ZERO);
  sparKontoAntrag2.setErstAuftrag(BigDecimal.ZERO);
  sparKontoAntragService.save(sparKontoAntrag2);

  // kunde 1
  SparKontoAntrag sparKontoAntrag3 = new SparKontoAntrag();
  sparKontoAntrag3.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  sparKontoAntrag3.setAntragStatus(AntragStatusEnum.GENEHMIGT);
  sparKontoAntrag3.setId(4L);
  sparKontoAntrag3.setKundennummer(123L);
  sparKontoAntrag3.setProdukt(KontoProduktEnum.SPAREN);
  sparKontoAntrag3.setDauerAuftrag(BigDecimal.ZERO);
  sparKontoAntrag3.setErstAuftrag(BigDecimal.ZERO);
  sparKontoAntragService.save(sparKontoAntrag3);

  // kunde 1
  SparKontoAntrag sparKontoAntrag4 = new SparKontoAntrag();
  sparKontoAntrag4.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  sparKontoAntrag4.setAntragStatus(AntragStatusEnum.GENEHMIGT);
  sparKontoAntrag4.setId(5L);
  sparKontoAntrag4.setKundennummer(123L);
  sparKontoAntrag4.setProdukt(KontoProduktEnum.SPAREN);
  sparKontoAntrag4.setDauerAuftrag(BigDecimal.ZERO);
  sparKontoAntrag4.setErstAuftrag(BigDecimal.ZERO);
  sparKontoAntragService.save(sparKontoAntrag4);

  // kunde 3
  SparKontoAntrag sparKontoAntrag5 = new SparKontoAntrag();
  sparKontoAntrag5.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  sparKontoAntrag5.setAntragStatus(AntragStatusEnum.EINGEREICHT);
  sparKontoAntrag5.setId(12L);
  sparKontoAntrag5.setKundennummer(125L);
  sparKontoAntrag5.setProdukt(KontoProduktEnum.SPAREN);
  sparKontoAntrag5.setDauerAuftrag(BigDecimal.valueOf(500));
  sparKontoAntrag5.setErstAuftrag(BigDecimal.valueOf(5000));
  sparKontoAntragService.save(sparKontoAntrag5);

  // kunde 4
  SparKontoAntrag sparKontoAntrag6 = new SparKontoAntrag();
  sparKontoAntrag6.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  sparKontoAntrag6.setAntragStatus(AntragStatusEnum.EINGEREICHT);
  sparKontoAntrag6.setId(13L);
  sparKontoAntrag6.setKundennummer(126L);
  sparKontoAntrag6.setProdukt(KontoProduktEnum.SPAREN);
  sparKontoAntrag6.setDauerAuftrag(BigDecimal.valueOf(400));
  sparKontoAntrag6.setErstAuftrag(BigDecimal.valueOf(4000));
  sparKontoAntragService.save(sparKontoAntrag6);

  // kunde 5
  SparKontoAntrag sparKontoAntrag7 = new SparKontoAntrag();
  sparKontoAntrag7.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  sparKontoAntrag7.setAntragStatus(AntragStatusEnum.EINGEREICHT);
  sparKontoAntrag7.setId(14L);
  sparKontoAntrag7.setKundennummer(127L);
  sparKontoAntrag7.setProdukt(KontoProduktEnum.SPAREN);
  sparKontoAntrag7.setDauerAuftrag(BigDecimal.valueOf(300));
  sparKontoAntrag7.setErstAuftrag(BigDecimal.valueOf(3000));
  sparKontoAntragService.save(sparKontoAntrag7);

  // kunde 6
  SparKontoAntrag sparKontoAntrag8 = new SparKontoAntrag();
  sparKontoAntrag8.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  sparKontoAntrag8.setAntragStatus(AntragStatusEnum.EINGEREICHT);
  sparKontoAntrag8.setId(15L);
  sparKontoAntrag8.setKundennummer(128L);
  sparKontoAntrag8.setProdukt(KontoProduktEnum.SPAREN);
  sparKontoAntrag8.setDauerAuftrag(BigDecimal.valueOf(200));
  sparKontoAntrag8.setErstAuftrag(BigDecimal.valueOf(2000));
  sparKontoAntragService.save(sparKontoAntrag8);

  // kunde 2
  KreditKontoAntrag kreditKontoAntrag1 = new KreditKontoAntrag();
  kreditKontoAntrag1.setId(6L);
  kreditKontoAntrag1.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  kreditKontoAntrag1.setKreditBetrag(BigDecimal.valueOf(15000));
  kreditKontoAntrag1.setAntragStatus(AntragStatusEnum.GENEHMIGT);
  kreditKontoAntrag1.setKundennummer(124L);
  kreditKontoAntrag1.setLaufzeit(BigInteger.valueOf(120));
  kreditKontoAntrag1.setZinssatz(BigDecimal.valueOf(8.00));
  kreditKontoAntrag1.setRate(BigDecimal.valueOf(181.99));
  kreditKontoAntrag1.setProdukt(KontoProduktEnum.KREDIT);
  kreditKontoAntrag1.setGesamtBelastung(BigDecimal.valueOf(21838.80));
  kreditKontoAntragService.save(kreditKontoAntrag1);

  // kunde 1
  KreditKontoAntrag kreditKontoAntrag2 = new KreditKontoAntrag();
  kreditKontoAntrag2.setId(7L);
  kreditKontoAntrag2.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  kreditKontoAntrag2.setKreditBetrag(BigDecimal.valueOf(15000));
  kreditKontoAntrag2.setAntragStatus(AntragStatusEnum.GENEHMIGT);
  kreditKontoAntrag2.setKundennummer(123L);
  kreditKontoAntrag2.setLaufzeit(BigInteger.valueOf(120));
  kreditKontoAntrag2.setZinssatz(BigDecimal.valueOf(8.00));
  kreditKontoAntrag2.setRate(BigDecimal.valueOf(181.99));
  kreditKontoAntrag2.setGesamtBelastung(BigDecimal.valueOf(21838.80));
  kreditKontoAntrag2.setProdukt(KontoProduktEnum.KREDIT);
  kreditKontoAntragService.save(kreditKontoAntrag2);

  // kunde 2
  KreditKontoAntrag kreditKontoAntrag3 = new KreditKontoAntrag();
  kreditKontoAntrag3.setId(11L);
  kreditKontoAntrag3.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  kreditKontoAntrag3.setKreditBetrag(BigDecimal.valueOf(15000));
  kreditKontoAntrag3.setAntragStatus(AntragStatusEnum.EINGEREICHT);
  kreditKontoAntrag3.setKundennummer(124L);
  kreditKontoAntrag3.setLaufzeit(BigInteger.valueOf(120));
  kreditKontoAntrag3.setZinssatz(BigDecimal.valueOf(8.00));
  kreditKontoAntrag3.setRate(BigDecimal.valueOf(181.99));
  kreditKontoAntrag3.setGesamtBelastung(BigDecimal.valueOf(21838.80));
  kreditKontoAntrag3.setProdukt(KontoProduktEnum.KREDIT);
  kreditKontoAntragService.save(kreditKontoAntrag3);

  // kunde 3
  KreditKontoAntrag kreditKontoAntrag4 = new KreditKontoAntrag();
  kreditKontoAntrag4.setId(16L);
  kreditKontoAntrag4.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  kreditKontoAntrag4.setKreditBetrag(BigDecimal.valueOf(15000));
  kreditKontoAntrag4.setAntragStatus(AntragStatusEnum.EINGEREICHT);
  kreditKontoAntrag4.setKundennummer(125L);
  kreditKontoAntrag4.setLaufzeit(BigInteger.valueOf(120));
  kreditKontoAntrag4.setZinssatz(BigDecimal.valueOf(8.00));
  kreditKontoAntrag4.setRate(BigDecimal.valueOf(181.99));
  kreditKontoAntrag4.setGesamtBelastung(BigDecimal.valueOf(21838.80));
  kreditKontoAntrag4.setProdukt(KontoProduktEnum.KREDIT);
  kreditKontoAntragService.save(kreditKontoAntrag4);

  // kunde 4
  KreditKontoAntrag kreditKontoAntrag5 = new KreditKontoAntrag();
  kreditKontoAntrag5.setId(17L);
  kreditKontoAntrag5.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  kreditKontoAntrag5.setKreditBetrag(BigDecimal.valueOf(15000));
  kreditKontoAntrag5.setAntragStatus(AntragStatusEnum.EINGEREICHT);
  kreditKontoAntrag5.setKundennummer(126L);
  kreditKontoAntrag5.setLaufzeit(BigInteger.valueOf(120));
  kreditKontoAntrag5.setZinssatz(BigDecimal.valueOf(8.00));
  kreditKontoAntrag5.setRate(BigDecimal.valueOf(181.99));
  kreditKontoAntrag5.setGesamtBelastung(BigDecimal.valueOf(21838.80));
  kreditKontoAntrag5.setProdukt(KontoProduktEnum.KREDIT);
  kreditKontoAntragService.save(kreditKontoAntrag5);

  // kunde 5
  KreditKontoAntrag kreditKontoAntrag6 = new KreditKontoAntrag();
  kreditKontoAntrag6.setId(18L);
  kreditKontoAntrag6.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  kreditKontoAntrag6.setKreditBetrag(BigDecimal.valueOf(15000));
  kreditKontoAntrag6.setAntragStatus(AntragStatusEnum.EINGEREICHT);
  kreditKontoAntrag6.setKundennummer(127L);
  kreditKontoAntrag6.setLaufzeit(BigInteger.valueOf(120));
  kreditKontoAntrag6.setZinssatz(BigDecimal.valueOf(8.00));
  kreditKontoAntrag6.setRate(BigDecimal.valueOf(181.99));
  kreditKontoAntrag6.setGesamtBelastung(BigDecimal.valueOf(21838.80));
  kreditKontoAntrag6.setProdukt(KontoProduktEnum.KREDIT);
  kreditKontoAntragService.save(kreditKontoAntrag6);

  // kunde 6
  KreditKontoAntrag kreditKontoAntrag7 = new KreditKontoAntrag();
  kreditKontoAntrag7.setId(19L);
  kreditKontoAntrag7.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  kreditKontoAntrag7.setKreditBetrag(BigDecimal.valueOf(15000));
  kreditKontoAntrag7.setAntragStatus(AntragStatusEnum.EINGEREICHT);
  kreditKontoAntrag7.setKundennummer(128L);
  kreditKontoAntrag7.setLaufzeit(BigInteger.valueOf(120));
  kreditKontoAntrag7.setZinssatz(BigDecimal.valueOf(8.00));
  kreditKontoAntrag7.setRate(BigDecimal.valueOf(181.99));
  kreditKontoAntrag7.setGesamtBelastung(BigDecimal.valueOf(21838.80));
  kreditKontoAntrag7.setProdukt(KontoProduktEnum.KREDIT);
  kreditKontoAntragService.save(kreditKontoAntrag7);

  // Kunde 1
  GiroKontoAntrag giroKontoAntrag1 = new GiroKontoAntrag();
  giroKontoAntrag1.setId(8L);
  giroKontoAntrag1.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  giroKontoAntrag1.setAntragStatus(AntragStatusEnum.EINGEREICHT);
  giroKontoAntrag1.setUeberziehungsrahmenGewuenscht(true);
  giroKontoAntrag1.setKundennummer(123L);
  giroKontoAntrag1.setProdukt(KontoProduktEnum.GIRO);
  giroKontoAntragService.save(giroKontoAntrag1);

  // kunde 2
  GiroKontoAntrag giroKontoAntrag2 = new GiroKontoAntrag();
  giroKontoAntrag2.setId(9L);
  giroKontoAntrag2.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  giroKontoAntrag2.setAntragStatus(AntragStatusEnum.GENEHMIGT);
  giroKontoAntrag2.setUeberziehungsrahmenGewuenscht(false);
  giroKontoAntrag2.setKundennummer(124L);
  giroKontoAntrag2.setProdukt(KontoProduktEnum.GIRO);
  giroKontoAntragService.save(giroKontoAntrag2);

  // kunde 1
  GiroKontoAntrag giroKontoAntrag3 = new GiroKontoAntrag();
  giroKontoAntrag3.setId(10L);
  giroKontoAntrag3.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  giroKontoAntrag3.setAntragStatus(AntragStatusEnum.GENEHMIGT);
  giroKontoAntrag3.setUeberziehungsrahmenGewuenscht(false);
  giroKontoAntrag3.setKundennummer(123L);
  giroKontoAntrag3.setProdukt(KontoProduktEnum.GIRO);
  giroKontoAntragService.save(giroKontoAntrag3);

  // kunde 3
  GiroKontoAntrag giroKontoAntrag4 = new GiroKontoAntrag();
  giroKontoAntrag4.setId(20L);
  giroKontoAntrag4.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  giroKontoAntrag4.setAntragStatus(AntragStatusEnum.EINGEREICHT);
  giroKontoAntrag4.setUeberziehungsrahmenGewuenscht(false);
  giroKontoAntrag4.setKundennummer(125L);
  giroKontoAntrag4.setProdukt(KontoProduktEnum.GIRO);
  giroKontoAntragService.save(giroKontoAntrag4);

  // kunde 4
  GiroKontoAntrag giroKontoAntrag5 = new GiroKontoAntrag();
  giroKontoAntrag5.setId(21L);
  giroKontoAntrag5.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  giroKontoAntrag5.setAntragStatus(AntragStatusEnum.EINGEREICHT);
  giroKontoAntrag5.setUeberziehungsrahmenGewuenscht(false);
  giroKontoAntrag5.setKundennummer(126L);
  giroKontoAntrag5.setProdukt(KontoProduktEnum.GIRO);
  giroKontoAntragService.save(giroKontoAntrag5);

  // kunde 5
  GiroKontoAntrag giroKontoAntrag6 = new GiroKontoAntrag();
  giroKontoAntrag6.setId(22L);
  giroKontoAntrag6.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  giroKontoAntrag6.setAntragStatus(AntragStatusEnum.EINGEREICHT);
  giroKontoAntrag6.setUeberziehungsrahmenGewuenscht(true);
  giroKontoAntrag6.setKundennummer(127L);
  giroKontoAntrag6.setProdukt(KontoProduktEnum.GIRO);
  giroKontoAntragService.save(giroKontoAntrag6);

  // kunde 6
  GiroKontoAntrag giroKontoAntrag7 = new GiroKontoAntrag();
  giroKontoAntrag7.setId(23L);
  giroKontoAntrag7.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  giroKontoAntrag7.setAntragStatus(AntragStatusEnum.EINGEREICHT);
  giroKontoAntrag7.setUeberziehungsrahmenGewuenscht(true);
  giroKontoAntrag7.setKundennummer(128L);
  giroKontoAntrag7.setProdukt(KontoProduktEnum.GIRO);
  giroKontoAntragService.save(giroKontoAntrag7);


  // SparKonto anlegen
  // kunde 1
  SparKonto sparKonto1 = new SparKonto();
  sparKonto1.setKontoname("tralala1");
  sparKonto1.setEroeffnungsDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  sparKonto1.setAktSaldo(BigDecimal.valueOf(4750.00));
  sparKonto1.setKontonummer(123001L);
  sparKonto1.setKontoStatus(KontoStatusEnum.OFFEN);
  sparKonto1.setKunde(kunde1);
  sparKonto1.setProdukt(KontoProduktEnum.SPAREN);
  sparKonto1.setKontoAntrag(sparKontoAntrag3);

  // kunde 1
  SparKonto sparKonto2 = new SparKonto();
  sparKonto2.setKontoname("tralala2");
  sparKonto2.setEroeffnungsDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  sparKonto2.setAktSaldo(BigDecimal.valueOf(0.00));
  sparKonto2.setKontonummer(123002L);
  sparKonto2.setKontoStatus(KontoStatusEnum.OFFEN);
  sparKonto2.setKunde(kunde1);
  sparKonto2.setProdukt(KontoProduktEnum.SPAREN);
  sparKonto2.setKontoAntrag(sparKontoAntrag4);

  // kunde 2
  SparKonto sparKonto3 = new SparKonto();
  sparKonto3.setKontoname("tralala3");
  sparKonto3.setEroeffnungsDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  sparKonto3.setAktSaldo(BigDecimal.valueOf(4750.00));
  sparKonto3.setKontonummer(124001L);
  sparKonto3.setKontoStatus(KontoStatusEnum.OFFEN);
  sparKonto3.setKunde(kunde2);
  sparKonto3.setProdukt(KontoProduktEnum.SPAREN);
  sparKonto3.setKontoAntrag(sparKontoAntrag);

  sparService.save(sparKonto1);
  sparService.save(sparKonto2);
  sparService.save(sparKonto3);

  // kunde 1
  GiroKonto giroKonto1 = new GiroKonto();
  giroKonto1.setEroeffnungsDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  giroKonto1.setAktSaldo(BigDecimal.valueOf(2250));
  giroKonto1.setKontonummer(123003L);
  giroKonto1.setKontoStatus(KontoStatusEnum.OFFEN);
  giroKonto1.setKunde(kunde1);
  giroKonto1.setKontoAntrag(giroKontoAntrag3);
  giroKonto1.setUeberziehungsRahmen(BigDecimal.valueOf(500));
  giroKonto1.setProdukt(KontoProduktEnum.GIRO);
  giroService.save(giroKonto1);

  // kunde 2
  GiroKonto giroKonto2 = new GiroKonto();
  giroKonto2.setEroeffnungsDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  giroKonto2.setAktSaldo(BigDecimal.valueOf(1500));
  giroKonto2.setKontonummer(124003L);
  giroKonto2.setKontoStatus(KontoStatusEnum.OFFEN);
  giroKonto2.setKunde(kunde2);
  giroKonto2.setKontoAntrag(giroKontoAntrag2);
  giroKonto2.setProdukt(KontoProduktEnum.GIRO);
  giroKonto2.setUeberziehungsRahmen(BigDecimal.valueOf(500));
  giroService.save(giroKonto2);

  // kunde 1
  KreditKonto kreditKonto1 = new KreditKonto();
  kreditKonto1.setEroeffnungsDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  kreditKonto1.setAktSaldo(BigDecimal.valueOf(-14750));
  kreditKonto1.setKontonummer(123004L);
  kreditKonto1.setKontoStatus(KontoStatusEnum.OFFEN);
  kreditKonto1.setKunde(kunde1);
  kreditKonto1.setKontoAntrag(kreditKontoAntrag2);
  kreditKonto1.setKreditBetrag(kreditKontoAntrag2.getKreditBetrag());
  kreditKonto1.setLaufzeit(kreditKontoAntrag2.getLaufzeit());
  kreditKonto1.setRate(kreditKontoAntrag2.getRate());
  kreditKonto1.setProdukt(KontoProduktEnum.KREDIT);
  kreditService.save(kreditKonto1);

  // kunde 2
  KreditKonto kreditKonto2 = new KreditKonto();
  kreditKonto2.setEroeffnungsDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  kreditKonto2.setAktSaldo(BigDecimal.valueOf(-14750));
  kreditKonto2.setKontonummer(124002L);
  kreditKonto2.setKontoStatus(KontoStatusEnum.OFFEN);
  kreditKonto2.setKunde(kunde2);
  kreditKonto2.setKontoAntrag(kreditKontoAntrag1);
  kreditKonto2.setKreditBetrag(kreditKontoAntrag1.getKreditBetrag());
  kreditKonto2.setLaufzeit(kreditKontoAntrag1.getLaufzeit());
  kreditKonto2.setRate(kreditKontoAntrag1.getRate());
  kreditKonto2.setProdukt(KontoProduktEnum.KREDIT);
  kreditService.save(kreditKonto2);


  // Paar Sparkonto1 Buchungen
  // kunde 1
  KontoBuchung konto1Buchung1 = new KontoBuchung();
  konto1Buchung1.setId(1L);
  konto1Buchung1.setBuchungsArt(BuchungsArtEnum.HABEN);
  konto1Buchung1.setBuchungsBetrag(BigDecimal.valueOf(5000.00));
  konto1Buchung1.setBuchungsDatum(LocalDate.now());
  konto1Buchung1.setDatAnlage(LocalDateTime.now());
  konto1Buchung1.setBuchungsText("InitialBetrag");
  konto1Buchung1.setSaldoNachBuchung(BigDecimal.valueOf(5000.00));
  konto1Buchung1.setKonto(sparKonto1);
  kontoBuchungService.save(konto1Buchung1);

  // Zahlungsauftrag zur jeweiligen Buchung
  // Kunde1
  ZahlungsAuftrag zahlungsAuftrag1 = new ZahlungsAuftrag();
  zahlungsAuftrag1.setId(1L);
  zahlungsAuftrag1.setAuftragsStatus(ZahlungAuftragStatusEnum.DURCHGEFUEHRT);
  zahlungsAuftrag1.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
  zahlungsAuftrag1.setKonto(sparKonto1);
  zahlungsAuftrag1.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag1.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag1.setKontonummer(sparKonto1.getKontonummer().toString());
  zahlungsAuftrag1.setBetrag(BigDecimal.valueOf(5000));
  zahlungsAuftrag1.setEmpfaengerKonto(sparKonto1.getKontonummer().toString());
  zahlungsAuftrag1.setSenderKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag1);

  // kunde 1
  KontoBuchung konto1Buchung2 = new KontoBuchung();
  konto1Buchung2.setId(2L);
  konto1Buchung2.setBuchungsArt(BuchungsArtEnum.HABEN);
  konto1Buchung2.setBuchungsBetrag(BigDecimal.valueOf(250));
  konto1Buchung2.setBuchungsDatum(LocalDate.now());
  konto1Buchung2.setDatAnlage(LocalDateTime.now());
  konto1Buchung2.setBuchungsText("Sparen");
  konto1Buchung2.setSaldoNachBuchung(BigDecimal.valueOf(5250.00));
  konto1Buchung2.setKonto(sparKonto1);
  kontoBuchungService.save(konto1Buchung2);

  // Zahlungsauftrag zur jeweiligen Buchung
  // Kunde1
  ZahlungsAuftrag zahlungsAuftrag2 = new ZahlungsAuftrag();
  zahlungsAuftrag2.setId(2L);
  zahlungsAuftrag2.setAuftragsStatus(ZahlungAuftragStatusEnum.DURCHGEFUEHRT);
  zahlungsAuftrag2.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
  zahlungsAuftrag2.setKonto(sparKonto1);
  zahlungsAuftrag2.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag2.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag2.setKontonummer(sparKonto1.getKontonummer().toString());
  zahlungsAuftrag2.setBetrag(BigDecimal.valueOf(250));
  zahlungsAuftrag2.setEmpfaengerKonto(sparKonto1.getKontonummer().toString());
  zahlungsAuftrag2.setSenderKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag2);

  // kunde 1
  KontoBuchung konto1Buchung3 = new KontoBuchung();
  konto1Buchung3.setId(3L);
  konto1Buchung3.setBuchungsArt(BuchungsArtEnum.SOLL);
  konto1Buchung3.setBuchungsBetrag(BigDecimal.valueOf(500.00));
  konto1Buchung3.setBuchungsDatum(LocalDate.now());
  konto1Buchung3.setDatAnlage(LocalDateTime.now());
  konto1Buchung3.setBuchungsText("Grafikkarte");
  konto1Buchung3.setSaldoNachBuchung(BigDecimal.valueOf(4750.00));
  konto1Buchung3.setKonto(sparKonto1);
  kontoBuchungService.save(konto1Buchung3);

  // Zahlungsauftrag zur jeweiligen Buchung
  // Kunde1
  ZahlungsAuftrag zahlungsAuftrag3 = new ZahlungsAuftrag();
  zahlungsAuftrag3.setId(3L);
  zahlungsAuftrag3.setAuftragsStatus(ZahlungAuftragStatusEnum.DURCHGEFUEHRT);
  zahlungsAuftrag3.setAuftragsArt(ZahlungAuftragArtEnum.AUSZAHLUNG);
  zahlungsAuftrag3.setKonto(sparKonto1);
  zahlungsAuftrag3.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag3.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag3.setKontonummer(sparKonto1.getKontonummer().toString());
  zahlungsAuftrag3.setBetrag(BigDecimal.valueOf(250));
  zahlungsAuftrag3.setSenderKonto(sparKonto1.getKontonummer().toString());
  zahlungsAuftrag3.setEmpfaengerKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag3);

  // Paar Sparkonto2 Buchungen
  // kunde 2
  KontoBuchung konto2Buchung1 = new KontoBuchung();
  konto2Buchung1.setId(4L);
  konto2Buchung1.setBuchungsArt(BuchungsArtEnum.HABEN);
  konto2Buchung1.setBuchungsBetrag(BigDecimal.valueOf(5000.00));
  konto2Buchung1.setBuchungsDatum(LocalDate.now());
  konto2Buchung1.setDatAnlage(LocalDateTime.now());
  konto2Buchung1.setBuchungsText("5k");
  konto2Buchung1.setSaldoNachBuchung(BigDecimal.valueOf(5000.00));
  konto2Buchung1.setKonto(sparKonto3);
  kontoBuchungService.save(konto2Buchung1);

  // Zahlungsauftrag zur jeweiligen Buchung
  // Kunde2
  ZahlungsAuftrag zahlungsAuftrag4 = new ZahlungsAuftrag();
  zahlungsAuftrag4.setId(4L);
  zahlungsAuftrag4.setAuftragsStatus(ZahlungAuftragStatusEnum.DURCHGEFUEHRT);
  zahlungsAuftrag4.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
  zahlungsAuftrag4.setKonto(sparKonto3);
  zahlungsAuftrag4.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag4.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag4.setKontonummer(sparKonto3.getKontonummer().toString());
  zahlungsAuftrag4.setBetrag(BigDecimal.valueOf(5000));
  zahlungsAuftrag4.setEmpfaengerKonto(sparKonto3.getKontonummer().toString());
  zahlungsAuftrag4.setSenderKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag4);

  // kunde 2
  KontoBuchung konto2Buchung2 = new KontoBuchung();
  konto2Buchung2.setId(5L);
  konto2Buchung2.setBuchungsArt(BuchungsArtEnum.HABEN);
  konto2Buchung2.setBuchungsBetrag(BigDecimal.valueOf(250));
  konto2Buchung2.setBuchungsDatum(LocalDate.now());
  konto2Buchung2.setDatAnlage(LocalDateTime.now());
  konto2Buchung2.setBuchungsText("Dauersparen");
  konto2Buchung2.setSaldoNachBuchung(BigDecimal.valueOf(5250.00));
  konto2Buchung2.setKonto(sparKonto3);
  kontoBuchungService.save(konto2Buchung2);

  // Zahlungsauftrag zur jeweiligen Buchung
  // Kunde2
  ZahlungsAuftrag zahlungsAuftrag5 = new ZahlungsAuftrag();
  zahlungsAuftrag5.setId(5L);
  zahlungsAuftrag5.setAuftragsStatus(ZahlungAuftragStatusEnum.DURCHGEFUEHRT);
  zahlungsAuftrag5.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
  zahlungsAuftrag5.setKonto(sparKonto3);
  zahlungsAuftrag5.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag5.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag5.setKontonummer(sparKonto3.getKontonummer().toString());
  zahlungsAuftrag5.setBetrag(BigDecimal.valueOf(250));
  zahlungsAuftrag5.setEmpfaengerKonto(sparKonto3.getKontonummer().toString());
  zahlungsAuftrag5.setSenderKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag5);

  // kunde 2
  KontoBuchung konto2Buchung3 = new KontoBuchung();
  konto2Buchung3.setId(6L);
  konto2Buchung3.setBuchungsArt(BuchungsArtEnum.SOLL);
  konto2Buchung3.setBuchungsBetrag(BigDecimal.valueOf(500.00));
  konto2Buchung3.setBuchungsDatum(LocalDate.now());
  konto2Buchung3.setDatAnlage(LocalDateTime.now());
  konto2Buchung3.setBuchungsText("Soundkarte");
  konto2Buchung3.setSaldoNachBuchung(BigDecimal.valueOf(4750.00));
  konto2Buchung3.setKonto(sparKonto3);
  kontoBuchungService.save(konto2Buchung3);

  // Zahlungsauftrag zur jeweiligen Buchung
  // Kunde2
  ZahlungsAuftrag zahlungsAuftrag6 = new ZahlungsAuftrag();
  zahlungsAuftrag6.setId(6L);
  zahlungsAuftrag6.setAuftragsStatus(ZahlungAuftragStatusEnum.DURCHGEFUEHRT);
  zahlungsAuftrag6.setAuftragsArt(ZahlungAuftragArtEnum.AUSZAHLUNG);
  zahlungsAuftrag6.setKonto(sparKonto3);
  zahlungsAuftrag6.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag6.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag6.setKontonummer(sparKonto3.getKontonummer().toString());
  zahlungsAuftrag6.setBetrag(BigDecimal.valueOf(500));
  zahlungsAuftrag6.setSenderKonto(sparKonto3.getKontonummer().toString());
  zahlungsAuftrag6.setEmpfaengerKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag6);

  // Paar kreditkonto1 Buchungen
  // kunde 1
  KontoBuchung kreditkonto1Buchung1 = new KontoBuchung();
  kreditkonto1Buchung1.setId(7L);
  kreditkonto1Buchung1.setBuchungsArt(BuchungsArtEnum.SOLL);
  kreditkonto1Buchung1.setBuchungsBetrag(BigDecimal.valueOf(15000.00));
  kreditkonto1Buchung1.setBuchungsDatum(LocalDate.now());
  kreditkonto1Buchung1.setDatAnlage(LocalDateTime.now());
  kreditkonto1Buchung1.setBuchungsText("KreditAuszahlung");
  kreditkonto1Buchung1.setSaldoNachBuchung(BigDecimal.valueOf(-15000.00));
  kreditkonto1Buchung1.setKonto(kreditKonto1);
  kontoBuchungService.save(kreditkonto1Buchung1);

  // Zahlungsauftrag zur jeweiligen Buchung
  // Kunde 1
  ZahlungsAuftrag zahlungsAuftrag7 = new ZahlungsAuftrag();
  zahlungsAuftrag7.setId(7L);
  zahlungsAuftrag7.setAuftragsStatus(ZahlungAuftragStatusEnum.DURCHGEFUEHRT);
  zahlungsAuftrag7.setAuftragsArt(ZahlungAuftragArtEnum.AUSZAHLUNG);
  zahlungsAuftrag7.setKonto(kreditKonto1);
  zahlungsAuftrag7.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag7.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag7.setKontonummer(kreditKonto1.getKontonummer().toString());
  zahlungsAuftrag7.setBetrag(BigDecimal.valueOf(15000));
  zahlungsAuftrag7.setSenderKonto(kreditKonto1.getKontonummer().toString());
  zahlungsAuftrag7.setEmpfaengerKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag7);

  // kunde 1
  KontoBuchung kreditkonto1Buchung2 = new KontoBuchung();
  kreditkonto1Buchung2.setId(8L);
  kreditkonto1Buchung2.setBuchungsArt(BuchungsArtEnum.HABEN);
  kreditkonto1Buchung2.setBuchungsBetrag(BigDecimal.valueOf(250));
  kreditkonto1Buchung2.setBuchungsDatum(LocalDate.now());
  kreditkonto1Buchung2.setDatAnlage(LocalDateTime.now());
  kreditkonto1Buchung2.setBuchungsText("Rate");
  kreditkonto1Buchung2.setSaldoNachBuchung(BigDecimal.valueOf(-14750.00));
  kreditkonto1Buchung2.setKonto(kreditKonto1);
  kontoBuchungService.save(kreditkonto1Buchung2);

  // Zahlungsauftrag zur jeweiligen Buchung
  // Kunde 1
  ZahlungsAuftrag zahlungsAuftrag8 = new ZahlungsAuftrag();
  zahlungsAuftrag8.setId(8L);
  zahlungsAuftrag8.setAuftragsStatus(ZahlungAuftragStatusEnum.DURCHGEFUEHRT);
  zahlungsAuftrag8.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
  zahlungsAuftrag8.setKonto(kreditKonto1);
  zahlungsAuftrag8.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag8.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag8.setKontonummer(kreditKonto1.getKontonummer().toString());
  zahlungsAuftrag8.setBetrag(BigDecimal.valueOf(250));
  zahlungsAuftrag8.setEmpfaengerKonto(kreditKonto1.getKontonummer().toString());
  zahlungsAuftrag8.setSenderKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag8);

  // kunde 2
  KontoBuchung kreditkonto2Buchung1 = new KontoBuchung();
  kreditkonto2Buchung1.setId(9L);
  kreditkonto2Buchung1.setBuchungsArt(BuchungsArtEnum.SOLL);
  kreditkonto2Buchung1.setBuchungsBetrag(BigDecimal.valueOf(15000.00));
  kreditkonto2Buchung1.setBuchungsDatum(LocalDate.now());
  kreditkonto2Buchung1.setDatAnlage(LocalDateTime.now());
  kreditkonto2Buchung1.setBuchungsText("KreditAuszahlung");
  kreditkonto2Buchung1.setSaldoNachBuchung(BigDecimal.valueOf(-15000.00));
  kreditkonto2Buchung1.setKonto(kreditKonto2);
  kontoBuchungService.save(kreditkonto2Buchung1);

  // Zahlungsauftrag zur jeweiligen Buchung
  // Kunde 2
  ZahlungsAuftrag zahlungsAuftrag9 = new ZahlungsAuftrag();
  zahlungsAuftrag9.setId(9L);
  zahlungsAuftrag9.setAuftragsStatus(ZahlungAuftragStatusEnum.DURCHGEFUEHRT);
  zahlungsAuftrag9.setAuftragsArt(ZahlungAuftragArtEnum.AUSZAHLUNG);
  zahlungsAuftrag9.setKonto(kreditKonto2);
  zahlungsAuftrag9.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag9.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag9.setKontonummer(kreditKonto2.getKontonummer().toString());
  zahlungsAuftrag9.setBetrag(BigDecimal.valueOf(15000));
  zahlungsAuftrag9.setSenderKonto(kreditKonto2.getKontonummer().toString());
  zahlungsAuftrag9.setEmpfaengerKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag9);

  // kunde 2
  KontoBuchung kreditkonto2Buchung2 = new KontoBuchung();
  kreditkonto2Buchung2.setId(10L);
  kreditkonto2Buchung2.setBuchungsArt(BuchungsArtEnum.HABEN);
  kreditkonto2Buchung2.setBuchungsBetrag(BigDecimal.valueOf(250));
  kreditkonto2Buchung2.setBuchungsDatum(LocalDate.now());
  kreditkonto2Buchung2.setDatAnlage(LocalDateTime.now());
  kreditkonto2Buchung2.setBuchungsText("Rate");
  kreditkonto2Buchung2.setSaldoNachBuchung(BigDecimal.valueOf(-14750.00));
  kreditkonto2Buchung2.setKonto(kreditKonto2);
  kontoBuchungService.save(kreditkonto2Buchung2);

  // Zahlungsauftrag zur jeweiligen Buchung
  // Kunde 2
  ZahlungsAuftrag zahlungsAuftrag10 = new ZahlungsAuftrag();
  zahlungsAuftrag10.setId(10L);
  zahlungsAuftrag10.setAuftragsStatus(ZahlungAuftragStatusEnum.DURCHGEFUEHRT);
  zahlungsAuftrag10.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
  zahlungsAuftrag10.setKonto(kreditKonto2);
  zahlungsAuftrag10.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag10.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag10.setKontonummer(kreditKonto2.getKontonummer().toString());
  zahlungsAuftrag10.setBetrag(BigDecimal.valueOf(250));
  zahlungsAuftrag10.setEmpfaengerKonto(kreditKonto2.getKontonummer().toString());
  zahlungsAuftrag10.setSenderKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag10);

  // Paar Girokonto1 Buchungen
  // kunde 1
  KontoBuchung girokonto1buchung1 = new KontoBuchung();
  girokonto1buchung1.setId(11L);
  girokonto1buchung1.setBuchungsArt(BuchungsArtEnum.HABEN);
  girokonto1buchung1.setBuchungsBetrag(BigDecimal.valueOf(2500));
  girokonto1buchung1.setBuchungsDatum(LocalDate.now());
  girokonto1buchung1.setDatAnlage(LocalDateTime.now());
  girokonto1buchung1.setBuchungsText("Gehalt");
  girokonto1buchung1.setSaldoNachBuchung(BigDecimal.valueOf(2500));
  girokonto1buchung1.setKonto(giroKonto1);
  kontoBuchungService.save(girokonto1buchung1);

  // Zahlungsauftrag zur jeweiligen Buchung
  // Kunde 1
  ZahlungsAuftrag zahlungsAuftrag11 = new ZahlungsAuftrag();
  zahlungsAuftrag11.setId(11L);
  zahlungsAuftrag11.setAuftragsStatus(ZahlungAuftragStatusEnum.DURCHGEFUEHRT);
  zahlungsAuftrag11.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
  zahlungsAuftrag11.setKonto(giroKonto1);
  zahlungsAuftrag11.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag11.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag11.setKontonummer(giroKonto1.getKontonummer().toString());
  zahlungsAuftrag11.setBetrag(BigDecimal.valueOf(2500));
  zahlungsAuftrag11.setEmpfaengerKonto(giroKonto1.getKontonummer().toString());
  zahlungsAuftrag11.setSenderKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag11);

  // kunde 1
  KontoBuchung girokonto1buchung2 = new KontoBuchung();
  girokonto1buchung2.setId(12L);
  girokonto1buchung2.setBuchungsArt(BuchungsArtEnum.SOLL);
  girokonto1buchung2.setBuchungsBetrag(BigDecimal.valueOf(250));
  girokonto1buchung2.setBuchungsDatum(LocalDate.now());
  girokonto1buchung2.setDatAnlage(LocalDateTime.now());
  girokonto1buchung2.setBuchungsText("Einkauf");
  girokonto1buchung2.setSaldoNachBuchung(BigDecimal.valueOf(2250));
  girokonto1buchung2.setKonto(giroKonto1);
  kontoBuchungService.save(girokonto1buchung2);

  // Zahlungsauftrag zur jeweiligen Buchung
  // Kunde 1
  ZahlungsAuftrag zahlungsAuftrag12 = new ZahlungsAuftrag();
  zahlungsAuftrag12.setId(12L);
  zahlungsAuftrag12.setAuftragsStatus(ZahlungAuftragStatusEnum.DURCHGEFUEHRT);
  zahlungsAuftrag12.setAuftragsArt(ZahlungAuftragArtEnum.AUSZAHLUNG);
  zahlungsAuftrag12.setKonto(giroKonto1);
  zahlungsAuftrag12.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag12.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag12.setKontonummer(giroKonto1.getKontonummer().toString());
  zahlungsAuftrag12.setBetrag(BigDecimal.valueOf(250));
  zahlungsAuftrag12.setSenderKonto(giroKonto1.getKontonummer().toString());
  zahlungsAuftrag12.setEmpfaengerKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag12);

  // kunde 2
  KontoBuchung girokonto2buchung1 = new KontoBuchung();
  girokonto2buchung1.setId(13L);
  girokonto2buchung1.setBuchungsArt(BuchungsArtEnum.HABEN);
  girokonto2buchung1.setBuchungsBetrag(BigDecimal.valueOf(1600));
  girokonto2buchung1.setBuchungsDatum(LocalDate.now());
  girokonto2buchung1.setDatAnlage(LocalDateTime.now());
  girokonto2buchung1.setBuchungsText("Gehalt");
  girokonto2buchung1.setSaldoNachBuchung(BigDecimal.valueOf(1600));
  girokonto2buchung1.setKonto(giroKonto2);
  kontoBuchungService.save(girokonto2buchung1);

  // Zahlungsauftrag zur jeweiligen Buchung
  // Kunde 2
  ZahlungsAuftrag zahlungsAuftrag13 = new ZahlungsAuftrag();
  zahlungsAuftrag13.setId(13L);
  zahlungsAuftrag13.setAuftragsStatus(ZahlungAuftragStatusEnum.DURCHGEFUEHRT);
  zahlungsAuftrag13.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
  zahlungsAuftrag13.setKonto(giroKonto2);
  zahlungsAuftrag13.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag13.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag13.setKontonummer(giroKonto2.getKontonummer().toString());
  zahlungsAuftrag13.setBetrag(BigDecimal.valueOf(1600));
  zahlungsAuftrag13.setEmpfaengerKonto(giroKonto2.getKontonummer().toString());
  zahlungsAuftrag13.setSenderKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag13);

  // kunde 2
  KontoBuchung girokonto2buchung2 = new KontoBuchung();
  girokonto2buchung2.setId(14L);
  girokonto2buchung2.setBuchungsArt(BuchungsArtEnum.SOLL);
  girokonto2buchung2.setBuchungsBetrag(BigDecimal.valueOf(100));
  girokonto2buchung2.setBuchungsDatum(LocalDate.now());
  girokonto2buchung2.setDatAnlage(LocalDateTime.now());
  girokonto2buchung2.setBuchungsText("Einkauf");
  girokonto2buchung2.setSaldoNachBuchung(BigDecimal.valueOf(1500));
  girokonto2buchung2.setKonto(giroKonto2);
  kontoBuchungService.save(girokonto2buchung2);

  // Zahlungsauftrag zur jeweiligen Buchung
  // Kunde 2
  ZahlungsAuftrag zahlungsAuftrag14 = new ZahlungsAuftrag();
  zahlungsAuftrag14.setId(14L);
  zahlungsAuftrag14.setAuftragsStatus(ZahlungAuftragStatusEnum.DURCHGEFUEHRT);
  zahlungsAuftrag14.setAuftragsArt(ZahlungAuftragArtEnum.AUSZAHLUNG);
  zahlungsAuftrag14.setKonto(giroKonto2);
  zahlungsAuftrag14.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag14.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag14.setKontonummer(giroKonto2.getKontonummer().toString());
  zahlungsAuftrag14.setBetrag(BigDecimal.valueOf(100));
  zahlungsAuftrag14.setSenderKonto(giroKonto2.getKontonummer().toString());
  zahlungsAuftrag14.setEmpfaengerKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag14);

  // Stornierte und Wartende Auszahlungen
  ZahlungsAuftrag zahlungsAuftrag15 = new ZahlungsAuftrag();
  zahlungsAuftrag15.setId(15L);
  zahlungsAuftrag15.setAuftragsStatus(ZahlungAuftragStatusEnum.STORNIERT);
  zahlungsAuftrag15.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
  zahlungsAuftrag15.setKonto(giroKonto1);
  zahlungsAuftrag15.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag15.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag15.setKontonummer(giroKonto1.getKontonummer().toString());
  zahlungsAuftrag15.setBetrag(BigDecimal.valueOf(100));
  zahlungsAuftrag15.setEmpfaengerKonto(giroKonto1.getKontonummer().toString());
  zahlungsAuftrag15.setSenderKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag15);

  // Zahlungen die auf VerbuchungsBatch warten
  ZahlungsAuftrag zahlungsAuftrag16 = new ZahlungsAuftrag();
  zahlungsAuftrag16.setId(16L);
  zahlungsAuftrag16.setAuftragsStatus(ZahlungAuftragStatusEnum.ANGELEGT);
  zahlungsAuftrag16.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
  zahlungsAuftrag16.setKonto(giroKonto2);
  zahlungsAuftrag16.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag16.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag16.setKontonummer(giroKonto1.getKontonummer().toString());
  zahlungsAuftrag16.setBetrag(BigDecimal.valueOf(1500));
  zahlungsAuftrag16.setEmpfaengerKonto(giroKonto1.getKontonummer().toString());
  zahlungsAuftrag16.setSenderKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag16);

  ZahlungsAuftrag zahlungsAuftrag17 = new ZahlungsAuftrag();
  zahlungsAuftrag17.setId(17L);
  zahlungsAuftrag17.setAuftragsStatus(ZahlungAuftragStatusEnum.ANGELEGT);
  zahlungsAuftrag17.setAuftragsArt(ZahlungAuftragArtEnum.AUSZAHLUNG);
  zahlungsAuftrag17.setKonto(giroKonto2);
  zahlungsAuftrag17.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag17.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag17.setKontonummer(giroKonto2.getKontonummer().toString());
  zahlungsAuftrag17.setBetrag(BigDecimal.valueOf(400));
  zahlungsAuftrag17.setSenderKonto(giroKonto2.getKontonummer().toString());
  zahlungsAuftrag17.setEmpfaengerKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag17);

  ZahlungsAuftrag zahlungsAuftrag18 = new ZahlungsAuftrag();
  zahlungsAuftrag18.setId(18L);
  zahlungsAuftrag18.setAuftragsStatus(ZahlungAuftragStatusEnum.ANGELEGT);
  zahlungsAuftrag18.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
  zahlungsAuftrag18.setKonto(giroKonto2);
  zahlungsAuftrag18.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag18.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag18.setKontonummer(giroKonto2.getKontonummer().toString());
  zahlungsAuftrag18.setBetrag(BigDecimal.valueOf(100));
  zahlungsAuftrag18.setEmpfaengerKonto(giroKonto2.getKontonummer().toString());
  zahlungsAuftrag18.setSenderKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag18);

  ZahlungsAuftrag zahlungsAuftrag19 = new ZahlungsAuftrag();
  zahlungsAuftrag19.setId(19L);
  zahlungsAuftrag19.setAuftragsStatus(ZahlungAuftragStatusEnum.ANGELEGT);
  zahlungsAuftrag19.setAuftragsArt(ZahlungAuftragArtEnum.AUSZAHLUNG);
  zahlungsAuftrag19.setKonto(giroKonto1);
  zahlungsAuftrag19.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag19.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag19.setKontonummer(giroKonto1.getKontonummer().toString());
  zahlungsAuftrag19.setBetrag(BigDecimal.valueOf(230));
  zahlungsAuftrag19.setSenderKonto(giroKonto1.getKontonummer().toString());
  zahlungsAuftrag19.setEmpfaengerKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag19);

  ZahlungsAuftrag zahlungsAuftrag20 = new ZahlungsAuftrag();
  zahlungsAuftrag20.setId(20L);
  zahlungsAuftrag20.setAuftragsStatus(ZahlungAuftragStatusEnum.ANGELEGT);
  zahlungsAuftrag20.setAuftragsArt(ZahlungAuftragArtEnum.AUSZAHLUNG);
  zahlungsAuftrag20.setKonto(giroKonto1);
  zahlungsAuftrag20.setDatAnlage(LocalDateTime.now());
  zahlungsAuftrag20.setAuftragsDatum(LocalDate.now());
  zahlungsAuftrag20.setKontonummer(giroKonto1.getKontonummer().toString());
  zahlungsAuftrag20.setBetrag(BigDecimal.valueOf(360));
  zahlungsAuftrag20.setSenderKonto(giroKonto1.getKontonummer().toString());
  zahlungsAuftrag20.setEmpfaengerKonto("1234566788912");
  zahlungsAuftragService.save(zahlungsAuftrag20);
}
}
