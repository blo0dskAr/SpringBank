package at.blo0dy.SpringBank;

import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;

import javax.persistence.*;
import java.util.ArrayList;


@Entity
@Table(name = "bank")
public class Bank {

  @Id
  private final Long id = 1L;

  // Parameter für meine Bank
  @Column
  private String firmenName = "my Bank";

  @Column
  private String firmenChef = "Ich";

  @Column
  private Long steuerNummer = 12345L;

  // irgendwie brauch ichs noch ned.. ich les es anders im frontend aus (aus db) ...
  // und erzeugt mir ne seltsame spalte in der DB, ich comments mal aus
  // private ArrayList<Mitarbeiter> angestellte = new ArrayList<>();

  public Bank() {};

  // Konstruktor für meine Bank
  public Bank(String firmenName, String firmenChef, Long steuerNummer) {
    this.firmenChef = firmenChef;
    this.firmenName = firmenName;
    this.steuerNummer = steuerNummer;
  }

  public Long getId() {
    return id;
  }

  public String getFirmenName() {
    return firmenName;
  }

  public void setFirmenName(String firmenName) {
    this.firmenName = firmenName;
  }

  public String getFirmenChef() {
    return firmenChef;
  }

  public void setFirmenChef(String firmenChef) {
    this.firmenChef = firmenChef;
  }

  public Long getSteuerNummer() {
    return steuerNummer;
  }

  public void setSteuerNummer(Long steuerNummer) {
    this.steuerNummer = steuerNummer;
  }

/*   // Anheuern neuer Mitarbeiter
  public void anheuern(Mitarbeiter neuesOpfer) {angestellte.add(neuesOpfer); }

  // Feuern verbrauchter Mitarbeiter
  public void feuern(Mitarbeiter mitarbeiter) { angestellte.remove(mitarbeiter); }*/


/*  public Anwender createLogin(Mitarbeiter mitarbeiter, String loginName) {
    angestellte.remove(mitarbeiter);
    Anwender neuerAnwender = new Anwender(mitarbeiter.getVorname(), mitarbeiter.getNachname(), mitarbeiter.getMitarbeiterNummer(), mitarbeiter.getPosition(), loginName);
    angestellte.add(neuerAnwender);

    return neuerAnwender;
  }*/

  // Erstellt ein Passwort für einen Anwender, das vorher in der Kommandozeile abgefragt wird
  // ToDo: Frage: Wäre die Methode besser in der Anwenderklasse aufgehoben? (weil meine Liste initialisier ich ja hier in der Bank-Klasse)
/*  public void createPassword(Anwender anwender) {
    System.out.print("Bitte neues Passwort angeben für user " + anwender.getVorname() + ", " + anwender.getNachname() + " angeben: ");
    String neuesPasswort = new Scanner(System.in).nextLine();
    anwender.setPassword(neuesPasswort);
  }*/

  // Gibt eine Personalliste mit meinen angestellten (Mitarbeiter & Anwender) aus.
  // Prüft vorher obs inhalt gibt
  // TODO: Hat man hier noch methoden ? oder nur noch alles über service layers? Weil wie bekomm ich hier meine Angestellten befüllt? WO bind ich mir das DataRepository ein?
/*  public String personalListe() {
    String returnString = "";
    if (angestellte.size() == 0 || angestellte.isEmpty()) {
      returnString += "Hat derzeit keine Mitarbeiter";
    } else {
      returnString += "angestellte: ";
      for (Mitarbeiter mitarbeiter : angestellte ) {
        returnString += "\n " + mitarbeiter;
      }
    }
    return returnString;
  }*/

  // Gibt StandardInfo über die Bank aus
  public String info() {
    return "Die Firma " + firmenName + " mit der Steuernummer: " + steuerNummer + " hat als Overlord: " + firmenChef + "." ;
  }


}
