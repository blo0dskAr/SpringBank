package at.blo0dy.SpringBank.model.person;

import at.blo0dy.SpringBank.model.person.adresse.Adresse;

import javax.persistence.*;

/*@Entity
@Table(name = "person")*/

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "person")
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "vorname")
  private String vorname;

  @Column(name = "nachname")
  private String nachname;

  @OneToOne(cascade = CascadeType.ALL)
  private Adresse adresse;

  public Person() {}

  public Person(String vorname, String nachname, Adresse adresse) {
    this.vorname = vorname;
    this.nachname = nachname;
    this.adresse = adresse;
  }

  public String getVorname() {
    return vorname;
  }

  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  public String getNachname() {
    return nachname;
  }

  public void setNachname(String nachname) {
    this.nachname = nachname;
  }

  public Adresse getAdresse() {
    return adresse;
  }

  public void setAdresse(Adresse adresse) {
    this.adresse = adresse;
  }

  @Override
  public String toString() {
    return "Person{" +
            "id=" + id +
            ", vorname='" + vorname + '\'' +
            ", nachname='" + nachname + '\'' +
            ", adresse=" + adresse +
            '}';
  }
}
