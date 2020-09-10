package at.blo0dy.SpringBank.model.person.mitarbeiter;

import at.blo0dy.SpringBank.model.person.Person;
import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import at.blo0dy.SpringBank.model.person.mitarbeiter.loginCredentials.LoginCredentials;
import at.blo0dy.SpringBank.model.person.rolle.Rolle;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@Table(name = "mitarbeiter")
@PrimaryKeyJoinColumn(name = "id")
@Inheritance(strategy = InheritanceType.JOINED)
public class Mitarbeiter extends Person {

  @Column(name = "mitarbeiternummer")
  private String mitarbeiterNummer;

  @Column(name = "position")
  @NotBlank(message = "Position muss angegeben werden.")
  private String position;

  @OneToOne(mappedBy = "mitarbeiter",
          cascade = {CascadeType.ALL})
  private LoginCredentials loginCredentials;

  @Override
  public Long getId() {
    return super.getId();
  }

  @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH})
  @JoinTable(
        name = "map_mita_role",
        joinColumns = @JoinColumn(
                name = "mita_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(
                name = "role_id", referencedColumnName = "id"))
private List<Rolle> rollen = new ArrayList<>();


  public Mitarbeiter() {  }

  public Mitarbeiter(String vorname, String nachname, Adresse adresse, String mitarbeiterNummer, String position, LocalDate geburtsDatum) {
    super(vorname, nachname, adresse, geburtsDatum);
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

  public void addRolle(Rolle rolle) {
    rollen.add(rolle);
  }

  public String  getPassword() {
    return loginCredentials.getPassword() ;
  }


}

