package at.blo0dy.SpringBank.model.person.adresse;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "adresse")
public class Adresse {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "strasse")
  @NotBlank(message = "Straße muss mindestens Adresse und Hausnummer enthalten (zB. Teststraße 5/D/7)")
  @Pattern(regexp = "^[a-zA-ZäÄöÖüÜß -.,]{2,40}[0-9]{1,5}[0-9 -/a-zA-Z]{0,15}$", message = "Straße muss mindestens Adresse und Hausnummer enthalten (zB. Teststraße 5/D/7)")
  private String strasse;

  @Column(name = "plz")
  @Pattern(regexp = "^[0-9]{4,5}$", message = "PLZ als 4 bzw. 5-stellige Zahl angeben")
  @NotBlank(message = "PLZ als 4 bzw. 5-stellige Zahl angeben")
  private String plz;

  @Column(name = "ort")
  @NotBlank(message = "Ort darf nur aus Buchstaben (max. 30 Zeichen) bestehen.")
  @Pattern(regexp = "^[a-zA-ZäÄöÖüÜß -]{2,30}$", message = "Ort darf nur aus Buchstaben (max. 30 Zeichen) bestehen.")
  private String ort;

  @Column(name = "land")
  @NotBlank(message = "Ort darf nur aus Buchstaben (max. 30 Zeichen) bestehen.")
  @Pattern(regexp = "^[a-zA-ZäÄöÖüÜß -]{2,30}$", message = "Land darf nur aus Buchstaben (max. 30 Zeichen) bestehen.")
  private String land;

  public Adresse() {
  }

  public Adresse(String strasse, String plz, String ort, String land) {
    this.strasse = strasse;
    this.plz = plz;
    this.ort = ort;
    this.land = land;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStrasse() {
    return strasse;
  }

  public void setStrasse(String strasse) {
    this.strasse = strasse;
  }

  public String getPlz() {
    return plz;
  }

  public void setPlz(String plz) {
    this.plz = plz;
  }

  public String getOrt() {
    return ort;
  }

  public void setOrt(String ort) {
    this.ort = ort;
  }

  public String getLand() {
    return land;
  }

  public void setLand(String land) {
    this.land = land;
  }

  @Override
  public String toString() {
    return strasse + ", " + plz + " " + ort + ", " + land;
  }
}
