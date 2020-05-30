package at.blo0dy.SpringBank.model.person.mitarbeiter;

import at.blo0dy.SpringBank.model.person.Person;
import at.blo0dy.SpringBank.model.person.adresse.Adresse;

public class Mitarbeiter extends Person {

  private int mitarbeiterNummer;
  private String position;
  private Adresse adresse;

  public Mitarbeiter() {}

  public Mitarbeiter(int mitarbeiterNummer, String position, Adresse adresse) {
    this.mitarbeiterNummer = mitarbeiterNummer;
    this.position = position;
    this.adresse = adresse;
  }

  public Mitarbeiter(String vorname, String nachname, int mitarbeiterNummer, String position, Adresse adresse) {
    super(vorname, nachname);
    this.mitarbeiterNummer = mitarbeiterNummer;
    this.position = position;
    this.adresse = adresse;
  }




}
