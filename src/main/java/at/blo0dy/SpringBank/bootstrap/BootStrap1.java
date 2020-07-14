package at.blo0dy.SpringBank.bootstrap;

import at.blo0dy.SpringBank.Bank;
import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import at.blo0dy.SpringBank.model.enums.AntragStatusEnum;
import at.blo0dy.SpringBank.model.enums.BuchungsArtEnum;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import at.blo0dy.SpringBank.model.konto.kontoBuchung.KontoBuchung;
import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.model.person.mitarbeiter.loginCredentials.LoginCredentials;
import at.blo0dy.SpringBank.model.person.rolle.Rolle;
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
import at.blo0dy.SpringBank.service.kunde.KundeService;
import at.blo0dy.SpringBank.service.rolle.RolleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

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


  public BootStrap1(MitarbeiterService mitarbeiterService, AdresseService adresseService, KundeService kundeService, LoginCredentialsService loginCredentialsService,
                    BankService bankService, RolleService rolleService, SparService sparService, SparKontoAntragService sparKontoAntragService, KreditKontoAntragService kreditKontoAntragService,
                    KreditService kreditService, GiroKontoAntragService giroKontoAntragService, GiroService giroService, KontoBuchungService kontoBuchungService) {
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
  }

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
  Kunde kunde1 = new Kunde();
  kunde1.setKundennummer("123");
  kunde1.setAdresse(adresse3);
  kunde1.setId(3L);
  kunde1.setNachname("McKundeFace");
  kunde1.setVorname("Kundy");
  kunde1.setPassword("$2y$12$yfuEHL2ycFi5oJ6KCqxOceiZaT0N2ukxFNPXZqQZKh.9KErt9lRYm");
  kunde1.setRolle("customer");
  kunde1.setTelefonNummer("asdasd");
  kunde1.setEmailAdresse("wwdifwoeifjowei");
  kunde1.setConnectedGiro("123456789123");

  Kunde kunde2 = new Kunde();
  kunde2.setKundennummer("124");
  kunde2.setAdresse(adresse4);
  kunde2.setId(4L);
  kunde2.setNachname("McTestFace");
  kunde2.setVorname("Testy");
  kunde2.setPassword("$2y$12$yfuEHL2ycFi5oJ6KCqxOceiZaT0N2ukxFNPXZqQZKh.9KErt9lRYm");
  kunde2.setRolle("customer");
  kunde2.setTelefonNummer("12345");
  kunde2.setEmailAdresse("dfdfdfdfdf");
  kunde2.setConnectedGiro("123456789123");

  // kunde persistieren
  kundeService.save(kunde1);
  kundeService.save(kunde2);

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
  sparKontoAntragService.save(sparKontoAntrag);

  SparKontoAntrag sparKontoAntrag1 = new SparKontoAntrag();
  sparKontoAntrag1.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  sparKontoAntrag1.setAntragStatus(AntragStatusEnum.EINGEREICHT);
  sparKontoAntrag1.setId(2L);
  sparKontoAntrag1.setKundennummer(123L);
  sparKontoAntragService.save(sparKontoAntrag1);

  SparKontoAntrag sparKontoAntrag2 = new SparKontoAntrag();
  sparKontoAntrag2.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  sparKontoAntrag2.setAntragStatus(AntragStatusEnum.ABGELEHNT);
  sparKontoAntrag2.setId(3L);
  sparKontoAntrag2.setKundennummer(123L);
  sparKontoAntragService.save(sparKontoAntrag2);

  // kunde 1
  SparKontoAntrag sparKontoAntrag3 = new SparKontoAntrag();
  sparKontoAntrag3.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  sparKontoAntrag3.setAntragStatus(AntragStatusEnum.GENEHMIGT);
  sparKontoAntrag3.setId(4L);
  sparKontoAntrag3.setKundennummer(123L);
  sparKontoAntragService.save(sparKontoAntrag3);

  // kunde 1
  SparKontoAntrag sparKontoAntrag4 = new SparKontoAntrag();
  sparKontoAntrag4.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  sparKontoAntrag4.setAntragStatus(AntragStatusEnum.GENEHMIGT);
  sparKontoAntrag4.setId(5L);
  sparKontoAntrag4.setKundennummer(123L);
  sparKontoAntragService.save(sparKontoAntrag4);

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
  kreditKontoAntragService.save(kreditKontoAntrag2);

  GiroKontoAntrag giroKontoAntrag1 = new GiroKontoAntrag();
  giroKontoAntrag1.setId(8L);
  giroKontoAntrag1.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  giroKontoAntrag1.setAntragStatus(AntragStatusEnum.EINGEREICHT);
  giroKontoAntrag1.setUeberziehungsrahmenGewuenscht(true);
  giroKontoAntrag1.setKundennummer(123L);
  giroKontoAntragService.save(giroKontoAntrag1);

  // kunde 2
  GiroKontoAntrag giroKontoAntrag2 = new GiroKontoAntrag();
  giroKontoAntrag2.setId(9L);
  giroKontoAntrag2.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  giroKontoAntrag2.setAntragStatus(AntragStatusEnum.GENEHMIGT);
  giroKontoAntrag2.setUeberziehungsrahmenGewuenscht(false);
  giroKontoAntrag2.setKundennummer(124L);
  giroKontoAntragService.save(giroKontoAntrag2);

  // kunde 1
  GiroKontoAntrag giroKontoAntrag3 = new GiroKontoAntrag();
  giroKontoAntrag3.setId(10L);
  giroKontoAntrag3.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  giroKontoAntrag3.setAntragStatus(AntragStatusEnum.GENEHMIGT);
  giroKontoAntrag3.setUeberziehungsrahmenGewuenscht(false);
  giroKontoAntrag3.setKundennummer(123L);
  giroKontoAntragService.save(giroKontoAntrag3);


  // SparKonto anlegen
  // kunde 1
  SparKonto sparKonto1 = new SparKonto();
  sparKonto1.setKontoname("tralala1");
  sparKonto1.setEroeffnungsDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  sparKonto1.setAktSaldo(BigDecimal.valueOf(4750.00));
  sparKonto1.setKontonummer(123001L);
  sparKonto1.setKontoStatus(KontoStatusEnum.OFFEN);
  sparKonto1.setKunde(kunde1);
  sparKonto1.setKontoAntrag(sparKontoAntrag3);

  // kunde 1
  SparKonto sparKonto2 = new SparKonto();
  sparKonto2.setKontoname("tralala2");
  sparKonto2.setEroeffnungsDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  sparKonto2.setAktSaldo(BigDecimal.valueOf(5000));
  sparKonto2.setKontonummer(123002L);
  sparKonto2.setKontoStatus(KontoStatusEnum.OFFEN);
  sparKonto2.setKunde(kunde1);
  sparKonto2.setKontoAntrag(sparKontoAntrag4);

  // kunde 2
  SparKonto sparKonto3 = new SparKonto();
  sparKonto3.setKontoname("tralala3");
  sparKonto3.setEroeffnungsDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  sparKonto3.setAktSaldo(BigDecimal.valueOf(5000));
  sparKonto3.setKontonummer(124001L);
  sparKonto3.setKontoStatus(KontoStatusEnum.OFFEN);
  sparKonto3.setKunde(kunde2);
  sparKonto3.setKontoAntrag(sparKontoAntrag);

  sparService.save(sparKonto1);
  sparService.save(sparKonto2);
  sparService.save(sparKonto3);

  // kunde 1
  GiroKonto giroKonto1 = new GiroKonto();
  giroKonto1.setEroeffnungsDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  giroKonto1.setAktSaldo(BigDecimal.valueOf(12345.67));
  giroKonto1.setKontonummer(123003L);
  giroKonto1.setKontoStatus(KontoStatusEnum.OFFEN);
  giroKonto1.setKunde(kunde1);
  giroKonto1.setKontoAntrag(giroKontoAntrag3);
  giroKonto1.setUeberziehungsRahmen(BigDecimal.valueOf(500));
  giroService.save(giroKonto1);

  // kunde 2
  GiroKonto giroKonto2 = new GiroKonto();
  giroKonto2.setEroeffnungsDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
  giroKonto2.setAktSaldo(BigDecimal.valueOf(12345.67));
  giroKonto2.setKontonummer(124003L);
  giroKonto2.setKontoStatus(KontoStatusEnum.OFFEN);
  giroKonto2.setKunde(kunde2);
  giroKonto2.setKontoAntrag(giroKontoAntrag2);
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

  // Paar Sparkonto2 Buchungen
  // kunde 2
  KontoBuchung konto2Buchung1 = new KontoBuchung();
  konto2Buchung1.setId(12L);
  konto2Buchung1.setBuchungsArt(BuchungsArtEnum.HABEN);
  konto2Buchung1.setBuchungsBetrag(BigDecimal.valueOf(5000.00));
  konto2Buchung1.setBuchungsDatum(LocalDate.now());
  konto2Buchung1.setDatAnlage(LocalDateTime.now());
  konto2Buchung1.setBuchungsText("5k");
  konto2Buchung1.setSaldoNachBuchung(BigDecimal.valueOf(5000.00));
  konto2Buchung1.setKonto(sparKonto1);
  kontoBuchungService.save(konto2Buchung1);

  // kunde 2
  KontoBuchung konto2Buchung2 = new KontoBuchung();
  konto2Buchung2.setId(13L);
  konto2Buchung2.setBuchungsArt(BuchungsArtEnum.HABEN);
  konto2Buchung2.setBuchungsBetrag(BigDecimal.valueOf(250));
  konto2Buchung2.setBuchungsDatum(LocalDate.now());
  konto2Buchung2.setDatAnlage(LocalDateTime.now());
  konto2Buchung2.setBuchungsText("Dauersparen");
  konto2Buchung2.setSaldoNachBuchung(BigDecimal.valueOf(5250.00));
  konto2Buchung2.setKonto(sparKonto3);
  kontoBuchungService.save(konto2Buchung2);

  // kunde 2
  KontoBuchung konto2Buchung3 = new KontoBuchung();
  konto2Buchung3.setId(14L);
  konto2Buchung3.setBuchungsArt(BuchungsArtEnum.SOLL);
  konto2Buchung3.setBuchungsBetrag(BigDecimal.valueOf(500.00));
  konto2Buchung3.setBuchungsDatum(LocalDate.now());
  konto2Buchung3.setDatAnlage(LocalDateTime.now());
  konto2Buchung3.setBuchungsText("Soundkarte");
  konto2Buchung3.setSaldoNachBuchung(BigDecimal.valueOf(4750.00));
  konto2Buchung3.setKonto(sparKonto3);
  kontoBuchungService.save(konto2Buchung3);

  // Paar kreditkonto1 Buchungen
  // kunde 1
  KontoBuchung kreditkonto1Buchung1 = new KontoBuchung();
  kreditkonto1Buchung1.setId(4L);
  kreditkonto1Buchung1.setBuchungsArt(BuchungsArtEnum.SOLL);
  kreditkonto1Buchung1.setBuchungsBetrag(BigDecimal.valueOf(15000.00));
  kreditkonto1Buchung1.setBuchungsDatum(LocalDate.now());
  kreditkonto1Buchung1.setDatAnlage(LocalDateTime.now());
  kreditkonto1Buchung1.setBuchungsText("KreditAuszahlung");
  kreditkonto1Buchung1.setSaldoNachBuchung(BigDecimal.valueOf(15000.00));
  kreditkonto1Buchung1.setKonto(kreditKonto1);
  kontoBuchungService.save(kreditkonto1Buchung1);

  // kunde 1
  KontoBuchung kreditkonto1Buchung2 = new KontoBuchung();
  kreditkonto1Buchung2.setId(5L);
  kreditkonto1Buchung2.setBuchungsArt(BuchungsArtEnum.HABEN);
  kreditkonto1Buchung2.setBuchungsBetrag(BigDecimal.valueOf(250));
  kreditkonto1Buchung2.setBuchungsDatum(LocalDate.now());
  kreditkonto1Buchung2.setDatAnlage(LocalDateTime.now());
  kreditkonto1Buchung2.setBuchungsText("Rate");
  kreditkonto1Buchung2.setSaldoNachBuchung(BigDecimal.valueOf(14750.00));
  kreditkonto1Buchung2.setKonto(kreditKonto1);
  kontoBuchungService.save(kreditkonto1Buchung2);

  // kunde 1
  KontoBuchung kreditkonto2Buchung1 = new KontoBuchung();
  kreditkonto2Buchung1.setId(10L);
  kreditkonto2Buchung1.setBuchungsArt(BuchungsArtEnum.SOLL);
  kreditkonto2Buchung1.setBuchungsBetrag(BigDecimal.valueOf(15000.00));
  kreditkonto2Buchung1.setBuchungsDatum(LocalDate.now());
  kreditkonto2Buchung1.setDatAnlage(LocalDateTime.now());
  kreditkonto2Buchung1.setBuchungsText("KreditAuszahlung");
  kreditkonto2Buchung1.setSaldoNachBuchung(BigDecimal.valueOf(15000.00));
  kreditkonto2Buchung1.setKonto(kreditKonto2);
  kontoBuchungService.save(kreditkonto2Buchung1);

  // kunde 1
  KontoBuchung kreditkonto2Buchung2 = new KontoBuchung();
  kreditkonto2Buchung2.setId(11L);
  kreditkonto2Buchung2.setBuchungsArt(BuchungsArtEnum.HABEN);
  kreditkonto2Buchung2.setBuchungsBetrag(BigDecimal.valueOf(250));
  kreditkonto2Buchung2.setBuchungsDatum(LocalDate.now());
  kreditkonto2Buchung2.setDatAnlage(LocalDateTime.now());
  kreditkonto2Buchung2.setBuchungsText("Rate");
  kreditkonto2Buchung2.setSaldoNachBuchung(BigDecimal.valueOf(14750.00));
  kreditkonto2Buchung2.setKonto(kreditKonto2);
  kontoBuchungService.save(kreditkonto2Buchung2);

  // Paar Girokonto1 Buchungen
  // kunde 1
  KontoBuchung girokonto1buchung1 = new KontoBuchung();
  girokonto1buchung1.setId(6L);
  girokonto1buchung1.setBuchungsArt(BuchungsArtEnum.HABEN);
  girokonto1buchung1.setBuchungsBetrag(BigDecimal.valueOf(2500));
  girokonto1buchung1.setBuchungsDatum(LocalDate.now());
  girokonto1buchung1.setDatAnlage(LocalDateTime.now());
  girokonto1buchung1.setBuchungsText("Gehalt");
  girokonto1buchung1.setSaldoNachBuchung(BigDecimal.valueOf(2500));
  girokonto1buchung1.setKonto(giroKonto1);
  kontoBuchungService.save(girokonto1buchung1);

  // kunde 1
  KontoBuchung girokonto1buchung2 = new KontoBuchung();
  girokonto1buchung2.setId(7L);
  girokonto1buchung2.setBuchungsArt(BuchungsArtEnum.SOLL);
  girokonto1buchung2.setBuchungsBetrag(BigDecimal.valueOf(250));
  girokonto1buchung2.setBuchungsDatum(LocalDate.now());
  girokonto1buchung2.setDatAnlage(LocalDateTime.now());
  girokonto1buchung2.setBuchungsText("Einkauf");
  girokonto1buchung2.setSaldoNachBuchung(BigDecimal.valueOf(2250));
  girokonto1buchung2.setKonto(giroKonto1);
  kontoBuchungService.save(girokonto1buchung2);

  // kunde 2
  KontoBuchung girokonto2buchung1 = new KontoBuchung();
  girokonto2buchung1.setId(8L);
  girokonto2buchung1.setBuchungsArt(BuchungsArtEnum.HABEN);
  girokonto2buchung1.setBuchungsBetrag(BigDecimal.valueOf(1600));
  girokonto2buchung1.setBuchungsDatum(LocalDate.now());
  girokonto2buchung1.setDatAnlage(LocalDateTime.now());
  girokonto2buchung1.setBuchungsText("Gehalt");
  girokonto2buchung1.setSaldoNachBuchung(BigDecimal.valueOf(1600));
  girokonto2buchung1.setKonto(giroKonto2);
  kontoBuchungService.save(girokonto2buchung1);

  // kunde 2
  KontoBuchung girokonto2buchung2 = new KontoBuchung();
  girokonto2buchung2.setId(9L);
  girokonto2buchung2.setBuchungsArt(BuchungsArtEnum.SOLL);
  girokonto2buchung2.setBuchungsBetrag(BigDecimal.valueOf(100));
  girokonto2buchung2.setBuchungsDatum(LocalDate.now());
  girokonto2buchung2.setDatAnlage(LocalDateTime.now());
  girokonto2buchung2.setBuchungsText("Einkauf");
  girokonto2buchung2.setSaldoNachBuchung(BigDecimal.valueOf(1500));
  girokonto2buchung2.setKonto(giroKonto2);
  kontoBuchungService.save(girokonto2buchung2);

}
}
