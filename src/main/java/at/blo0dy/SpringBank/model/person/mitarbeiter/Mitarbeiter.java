package at.blo0dy.SpringBank.model.person.mitarbeiter;

import at.blo0dy.SpringBank.model.person.Person;
import at.blo0dy.SpringBank.model.person.adresse.Adresse;

import javax.persistence.*;

@Entity
@Table(name = "mitarbeiter")
public class Mitarbeiter extends Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "mitarbeiternummer")
  private int mitarbeiterNummer;

  @Column(name = "position")
  private String position;

  // @Column(name = "adresse")
  @OneToOne
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getMitarbeiterNummer() {
    return mitarbeiterNummer;
  }

  public void setMitarbeiterNummer(int mitarbeiterNummer) {
    this.mitarbeiterNummer = mitarbeiterNummer;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public Adresse getAdresse() {
    return adresse;
  }

  public void setAdresse(Adresse adresse) {
    this.adresse = adresse;
  }

  @Override
  public String toString() {
    return "Mitarbeiter{" +
            "id=" + id +
            ", mitarbeiterNummer=" + mitarbeiterNummer +
            ", position='" + position + '\'' +
            ", adresse=" + adresse +
            '}';
  }
}
