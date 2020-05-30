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

  //@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @OneToOne(cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.PERSIST})
  @JoinColumn(name = "person_id")
  private Person person;

  public Mitarbeiter() {}

  public Mitarbeiter(String vorname, String nachname, Adresse adresse, int mitarbeiterNummer, String position) {
    super(vorname, nachname, adresse);
    this.mitarbeiterNummer = mitarbeiterNummer;
    this.position = position;
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

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  @Override
  public String toString() {
    return "Mitarbeiter{" +
            "id=" + id +
            ", mitarbeiterNummer=" + mitarbeiterNummer +
            ", position='" + position + '\'' +
          //  ", person=" + person +
            '}';
  }
}
