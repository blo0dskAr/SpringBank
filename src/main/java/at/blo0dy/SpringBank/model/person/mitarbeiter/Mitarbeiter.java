package at.blo0dy.SpringBank.model.person.mitarbeiter;

import at.blo0dy.SpringBank.model.person.Person;
import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import at.blo0dy.SpringBank.model.person.mitarbeiter.loginCredentials.LoginCredentials;
import at.blo0dy.SpringBank.model.person.rolle.Rolle;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@Table(name = "mitarbeiter")
@PrimaryKeyJoinColumn(name = "id")
@Inheritance(strategy = InheritanceType.JOINED)
@Builder
public class Mitarbeiter extends Person {

/*  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;*/

  @Column(name = "mitarbeiternummer")
  @Min(1)
  @Max(99999999)
  @NotNull(message = "Mitarbeiternummer must be defined.")
  private String mitarbeiterNummer;

  @Column(name = "position")
  @NotBlank(message = "Position must be defined.")
  private String position;

  @OneToMany(mappedBy = "mitarbeiter",
         // cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH})
          cascade = {CascadeType.ALL})
  private List<LoginCredentials> loginCredentials;

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

  public Mitarbeiter(String vorname, String nachname, Adresse adresse, String mitarbeiterNummer, String position) {
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

  // Custom Methods
  // ööhh .. wo kommt das her ? :) verwend ich das ? =)
  public void addRolle(Rolle rolle) {
    rollen.add(rolle);
  }

  public String  getLoginName() {
    return loginCredentials.get(1).getLoginName();
  }

}

