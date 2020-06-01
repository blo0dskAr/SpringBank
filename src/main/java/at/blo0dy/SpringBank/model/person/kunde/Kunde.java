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


  public Kunde() { }

  public Kunde(String vorname, String nachname, Adresse adresse, int kundenNummer, Person person) {
    super(vorname, nachname, adresse);
    this.kundenNummer = kundenNummer;
  }

  @Override
  public String toString() {
    return super.toString() + " Kunde{" +
            "kundenNummer=" + kundenNummer +
            '}';
  }


}
