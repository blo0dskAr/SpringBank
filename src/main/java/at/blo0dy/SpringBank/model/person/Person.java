package at.blo0dy.SpringBank.model.person;

import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  // TODO: Mal ein RegexPattern für Name (mit hatscheks usw.) suchen.
  @NotBlank(message = "Vorname must be defined.")
  @Column(name = "vorname")
  @Pattern(regexp = "^[a-zA-ZäÄöÖüÜß -]+$", message = "Darf nur aus Buchstaben und dem Sonderzeichen \"-\" bestehen")
  private String vorname;

  @NotBlank(message = "Nachname must be defined.")
  @Column(name = "nachname")
  @Pattern(regexp = "^[a-zA-ZäÄöÖüÜß -]+$", message = "Darf nur aus Buchstaben und dem Sonderzeichen \"-\" bestehen")
  private String nachname;

  @Valid
  @OneToOne(cascade = CascadeType.ALL)
  private Adresse adresse;

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
