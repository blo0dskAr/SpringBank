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
  @NotBlank(message = "Straße must be defined.")
  private String strasse;

  @Column(name = "plz")
  @Pattern(regexp = "[0-9]{4,5}" )
  private String plz;

  @Column(name = "ort")
  @NotBlank(message = "Ort must be defined.")
  private String ort;

  @Column(name = "land")
  @NotBlank(message = "Land must be defined.")
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

/*  @Override
  public String toString() {
    return "Adresse{" +
            "id=" + id +
            ", strasse='" + strasse + '\'' +
            ", plz='" + plz + '\'' +
            ", ort='" + ort + '\'' +
            ", land='" + land + '\'' +
            '}';
  }*/

  @Override
  public String toString() {
    return strasse + ", " + plz + " " + ort + ", " + land;
  }
}
