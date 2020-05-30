package at.blo0dy.SpringBank.bootstrap;

import at.blo0dy.SpringBank.dao.MitarbeiterRepository;
import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.service.AdresseService;
import at.blo0dy.SpringBank.service.MitarbeiterService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.persistence.OneToOne;

@Component
public class BootStrap1 implements CommandLineRunner {

  private final MitarbeiterService mitarbeiterService;
  private final AdresseService adresseService;

  public BootStrap1(MitarbeiterService mitarbeiterService, AdresseService adresseService) {
    this.mitarbeiterService = mitarbeiterService;
    this.adresseService = adresseService;
  }

  @Override
  public void run(String... args) throws Exception {
    loadData();
  }



private void loadData() {

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

  adresseService.save(adresse);
  adresseService.save(adresse2);

  Mitarbeiter mitarbeiter = new Mitarbeiter();
  mitarbeiter.setVorname("Hans");
  mitarbeiter.setNachname("Wurst");
  mitarbeiter.setPosition("Dev/Ops Engineer");
  //mitarbeiter.setPerson(mitarbeiter);
  mitarbeiter.setMitarbeiterNummer(666);
  mitarbeiter.setId(1L);

  mitarbeiter.setAdresse(adresse);

  Mitarbeiter mitarbeiter2 = new Mitarbeiter();
  mitarbeiter2.setVorname("Melinda");
  mitarbeiter2.setNachname("Wurst");
  mitarbeiter2.setPosition("Business Analyst");
  // mitarbeiter2.setPerson(mitarbeiter2);
  mitarbeiter2.setMitarbeiterNummer(667);
  mitarbeiter2.setId(2L);

  mitarbeiter2.setAdresse(adresse2);

  mitarbeiterService.save(mitarbeiter);
  mitarbeiterService.save(mitarbeiter2);


}

}
