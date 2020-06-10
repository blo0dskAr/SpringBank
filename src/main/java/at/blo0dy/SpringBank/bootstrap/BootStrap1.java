package at.blo0dy.SpringBank.bootstrap;

import at.blo0dy.SpringBank.Bank;
import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.model.person.mitarbeiter.loginCredentials.LoginCredentials;
import at.blo0dy.SpringBank.service.*;
import at.blo0dy.SpringBank.service.adresse.AdresseService;
import at.blo0dy.SpringBank.service.bank.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootStrap1 implements CommandLineRunner {

  private final MitarbeiterService mitarbeiterService;
  private final AdresseService adresseService;
  private final KundeService kundeService;
  private final LoginCredentialsService loginCredentialsService;
  private final BankService bankService;

  public BootStrap1(MitarbeiterService mitarbeiterService, AdresseService adresseService, KundeService kundeService, LoginCredentialsService loginCredentialsService, BankService bankService) {
    this.mitarbeiterService = mitarbeiterService;
    this.adresseService = adresseService;
    this.kundeService = kundeService;
    this.loginCredentialsService = loginCredentialsService;
    this.bankService = bankService;
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

  // Mitarbeiter erstellen
  Mitarbeiter mitarbeiter = new Mitarbeiter();
  mitarbeiter.setVorname("Hans");
  mitarbeiter.setNachname("Wurst");
  mitarbeiter.setPosition("Dev/Ops Engineer");
  mitarbeiter.setMitarbeiterNummer("666");
  mitarbeiter.setId(1L);

  // Adresse uebergeben
  mitarbeiter.setAdresse(adresse);

  Mitarbeiter mitarbeiter2 = new Mitarbeiter();
  mitarbeiter2.setVorname("Melinda");
  mitarbeiter2.setNachname("Wurst");
  mitarbeiter2.setPosition("Business Analyst");
  mitarbeiter2.setMitarbeiterNummer("667");
  mitarbeiter2.setId(2L);

  mitarbeiter2.setAdresse(adresse2);

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
  kunde1.setKundenNummer(123);
  kunde1.setAdresse(adresse3);
  kunde1.setId(1L);
  kunde1.setNachname("McKundeFace");
  kunde1.setVorname("Kundy");

  // kunde persistieren
  kundeService.save(kunde1);

  // LoginCredentials erstellen
  LoginCredentials lc1 = new LoginCredentials();
  lc1.setLoginName("hwurst");
  lc1.setPassword("12345");
  lc1.setId(1L);
  lc1.setMitarbeiter(mitarbeiter);
  loginCredentialsService.save(lc1);

  //mitarbeiterService.save(mitarbeiter);



}
}
