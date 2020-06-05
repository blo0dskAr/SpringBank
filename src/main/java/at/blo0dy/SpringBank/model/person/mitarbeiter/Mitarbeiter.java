package at.blo0dy.SpringBank.model.person.mitarbeiter;

import at.blo0dy.SpringBank.model.person.Person;
import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import at.blo0dy.SpringBank.model.person.mitarbeiter.loginCredentials.LoginCredentials;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "mitarbeiter")
@PrimaryKeyJoinColumn(name = "id")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Mitarbeiter extends Person {

/*  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;*/

  @Column(name = "mitarbeiternummer")
  private int mitarbeiterNummer;

  @Column(name = "position")
  private String position;

  @OneToMany(mappedBy = "mitarbeiter",
          cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH})
  private List<LoginCredentials> loginCredentials;

/*  @OneToOne(cascade = CascadeType.ALL)
  private Person person;*/

  public Mitarbeiter() {  }

  public Mitarbeiter(String vorname, String nachname, Adresse adresse, int mitarbeiterNummer, String position) {
    super(vorname, nachname, adresse);
    this.mitarbeiterNummer = mitarbeiterNummer;
    this.position = position;
  }

  @Override
  public String toString() {
    return super.toString() + " Mitarbeiter{" +
            "mitarbeiterNummer=" + mitarbeiterNummer +
            ", position='" + position + '\'' +
            '}';
  }
}

