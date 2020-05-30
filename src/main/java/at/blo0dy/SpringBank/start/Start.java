package at.blo0dy.SpringBank.start;

import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;

public class Start {

  public static void main(String[] args) {

    Mitarbeiter mitarbeiter = new Mitarbeiter();

    mitarbeiter.setId(1L);
    mitarbeiter.setMitarbeiterNummer(12345);
    mitarbeiter.setPosition("Chef");
    mitarbeiter.setNachname("Wurst");
    mitarbeiter.setVorname("Hans");

    Adresse adresse = new Adresse();
    adresse.setLand("Austria");
    adresse.setOrt("Wien");
    adresse.setPlz("1100");
    adresse.setOrt("Wien");

    mitarbeiter.setAdresse(adresse);

    System.out.println("MAIN: Mitarbeiter erstellt: " + mitarbeiter);



  }

}
