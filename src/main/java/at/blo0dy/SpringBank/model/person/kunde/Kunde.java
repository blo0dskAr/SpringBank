package at.blo0dy.SpringBank.model.person.kunde;

import at.blo0dy.SpringBank.model.person.Person;
import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "kunde")
@PrimaryKeyJoinColumn(name = "person_id")
public class Kunde extends Person {

/*  @Id
  //@GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;*/

  @Column(name = "kundennummer")
  private int kundenNummer;

  // ToDo: Eigene klassen? Oberklasse kontakt? kann mehr als eine tel haben etc.
  private String telefonNummer;
  private String emailAdresse;

  private Adresse adresse;
  // private List<Konto> konten;

  private boolean isLegi = false;
  private boolean hasAcceptedAGB = false;
  private boolean isActive = false;
  private boolean firstLoginDone = false;


  public Kunde() { }

  public Kunde(String vorname, String nachname, Adresse adresse, int kundenNummer, String telefonNummer, String emailAdresse, Adresse adresse1, boolean isLegi, boolean hasAcceptedAGB, boolean isActive, boolean firstLoginDone) {
    super(vorname, nachname, adresse);
    this.kundenNummer = kundenNummer;
    this.telefonNummer = telefonNummer;
    this.emailAdresse = emailAdresse;
    this.adresse = adresse1;
    this.isLegi = isLegi;
    this.hasAcceptedAGB = hasAcceptedAGB;
    this.isActive = isActive;
    this.firstLoginDone = firstLoginDone;
  }

  @Override
  public String toString() {
    return "Kunde{" +
            "kundenNummer=" + kundenNummer +
            ", telefonNummer='" + telefonNummer + '\'' +
            ", emailAdresse='" + emailAdresse + '\'' +
            ", adresse=" + adresse +
            ", isLegi=" + isLegi +
            ", hasAcceptedAGB=" + hasAcceptedAGB +
            ", isActive=" + isActive +
            ", firstLoginDone=" + firstLoginDone +
            '}';
  }


  public void checkActive() {
    if (this.isLegi == true && this.hasAcceptedAGB == true) {
      setActive(true);
    } else {
      setActive(false);
    }
  }

}
