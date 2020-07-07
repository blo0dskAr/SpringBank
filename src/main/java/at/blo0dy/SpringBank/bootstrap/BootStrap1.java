package at.blo0dy.SpringBank.bootstrap;

import at.blo0dy.SpringBank.Bank;
import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import at.blo0dy.SpringBank.model.enums.AntragsStatusEnum;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
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
import at.blo0dy.SpringBank.service.konto.kredit.KreditKontoAntragService;
import at.blo0dy.SpringBank.service.konto.kredit.KreditService;
import at.blo0dy.SpringBank.service.konto.sparen.SparKontoAntragService;
import at.blo0dy.SpringBank.service.konto.sparen.SparService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import at.blo0dy.SpringBank.service.rolle.RolleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

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


  public BootStrap1(MitarbeiterService mitarbeiterService, AdresseService adresseService, KundeService kundeService, LoginCredentialsService loginCredentialsService,
                    BankService bankService, RolleService rolleService, SparService sparService, SparKontoAntragService sparKontoAntragService, KreditKontoAntragService kreditKontoAntragService,
                    KreditService kreditService, GiroKontoAntragService giroKontoAntragService, GiroService giroService) {
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
  }

  @Override
  public void run(String... args) throws Exception {
    loadData();
  }

private void loadData() {

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

  // adresse persistieren
  adresseService.save(adresse3);

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

  // Rolle uebergeben


  // kunde persistieren
  kundeService.save(kunde1);

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
  SparKontoAntrag sparKontoAntrag = new SparKontoAntrag();
  sparKontoAntrag.setAntragsDatum(LocalDateTime.now());
  sparKontoAntrag.setAntragsStatus(AntragsStatusEnum.EINGEREICHT);
  sparKontoAntrag.setId(1L);
  sparKontoAntrag.setErstAuftrag(BigDecimal.valueOf(5000));
  sparKontoAntrag.setDauerAuftrag(BigDecimal.valueOf(250));
  sparKontoAntrag.setKundennummer(123L);
  sparKontoAntragService.save(sparKontoAntrag);

  SparKontoAntrag sparKontoAntrag1 = new SparKontoAntrag();
  sparKontoAntrag1.setAntragsDatum(LocalDateTime.now());
  sparKontoAntrag1.setAntragsStatus(AntragsStatusEnum.EINGEREICHT);
  sparKontoAntrag1.setId(2L);
  sparKontoAntrag1.setKundennummer(123L);
  sparKontoAntragService.save(sparKontoAntrag1);

  SparKontoAntrag sparKontoAntrag2 = new SparKontoAntrag();
  sparKontoAntrag2.setAntragsDatum(LocalDateTime.now());
  sparKontoAntrag2.setAntragsStatus(AntragsStatusEnum.ABGELEHNT);
  sparKontoAntrag2.setId(3L);
  sparKontoAntrag2.setKundennummer(123L);
  sparKontoAntragService.save(sparKontoAntrag2);

  SparKontoAntrag sparKontoAntrag3 = new SparKontoAntrag();
  sparKontoAntrag3.setAntragsDatum(LocalDateTime.now());
  sparKontoAntrag3.setAntragsStatus(AntragsStatusEnum.GENEHMIGT);
  sparKontoAntrag3.setId(4L);
  sparKontoAntrag3.setKundennummer(123L);
  sparKontoAntragService.save(sparKontoAntrag3);

  SparKontoAntrag sparKontoAntrag4 = new SparKontoAntrag();
  sparKontoAntrag4.setAntragsDatum(LocalDateTime.now());
  sparKontoAntrag4.setAntragsStatus(AntragsStatusEnum.GENEHMIGT);
  sparKontoAntrag4.setId(5L);
  sparKontoAntrag4.setKundennummer(123L);
  sparKontoAntragService.save(sparKontoAntrag4);

  KreditKontoAntrag kreditKontoAntrag1 = new KreditKontoAntrag();
  kreditKontoAntrag1.setId(1L);
  kreditKontoAntrag1.setAntragsDatum(LocalDateTime.now());
  kreditKontoAntrag1.setKreditBetrag(BigDecimal.valueOf(15000));
  kreditKontoAntrag1.setAntragsStatus(AntragsStatusEnum.EINGEREICHT);
  kreditKontoAntrag1.setKundennummer(123L);
  kreditKontoAntrag1.setLaufzeit(BigInteger.valueOf(120));
  kreditKontoAntrag1.setZinssatz(BigDecimal.valueOf(8.00));
  kreditKontoAntrag1.setRate(BigDecimal.valueOf(181.99));
  kreditKontoAntrag1.setGesamtBelastung(BigDecimal.valueOf(21838.80));
  kreditKontoAntragService.save(kreditKontoAntrag1);

  GiroKontoAntrag giroKontoAntrag1 = new GiroKontoAntrag();
  giroKontoAntrag1.setId(1L);
  giroKontoAntrag1.setAntragsDatum(LocalDateTime.now());
  giroKontoAntrag1.setAntragsStatus(AntragsStatusEnum.EINGEREICHT);
  giroKontoAntrag1.setUeberziehungsrahmenGewuenscht(true);
  giroKontoAntrag1.setKundennummer(123L);
  giroKontoAntragService.save(giroKontoAntrag1);

  GiroKontoAntrag giroKontoAntrag2 = new GiroKontoAntrag();
  giroKontoAntrag2.setId(2L);
  giroKontoAntrag2.setAntragsDatum(LocalDateTime.now());
  giroKontoAntrag2.setAntragsStatus(AntragsStatusEnum.EINGEREICHT);
  giroKontoAntrag2.setUeberziehungsrahmenGewuenscht(false);
  giroKontoAntrag2.setKundennummer(123L);
  giroKontoAntragService.save(giroKontoAntrag2);


  // SparKonto anlegen
  SparKonto sparKonto1 = new SparKonto();
  sparKonto1.setConnectedGiro("123456789");
  sparKonto1.setEroeffnungsDatum(LocalDate.now());
  sparKonto1.setId(1L);
  sparKonto1.setAktSaldo(BigDecimal.valueOf(2500));
  sparKonto1.setKontonummer(123001L);
  sparKonto1.setKontoStatus(KontoStatusEnum.OFFEN);
  sparKonto1.setKunde(kunde1);
  sparKonto1.setSparKontoAntrag(sparKontoAntrag3);

  SparKonto sparKonto2 = new SparKonto();
  sparKonto2.setConnectedGiro("123456789");
  sparKonto2.setEroeffnungsDatum(LocalDate.now());
  sparKonto2.setId(2L);
  sparKonto2.setAktSaldo(BigDecimal.valueOf(5000));
  sparKonto2.setKontonummer(123002L);
  sparKonto2.setKontoStatus(KontoStatusEnum.OFFEN);
  sparKonto2.setKunde(kunde1);
  sparKonto2.setSparKontoAntrag(sparKontoAntrag4);

  sparService.save(sparKonto1);
  sparService.save(sparKonto2);

}
}
