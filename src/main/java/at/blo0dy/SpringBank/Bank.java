package at.blo0dy.SpringBank;


import javax.persistence.*;


@Entity
@Table(name = "bank")
public class Bank {

  @Id
  private final Long id = 1L;

  @Column
  private String firmenName = "HonestBank";

  @Column
  private String firmenChef = "blo0dy";

  @Column
  private Long steuerNummer = 12345L;

  public Bank() {};

  public Bank(String firmenName, String firmenChef, Long steuerNummer) {
    this.firmenChef = firmenChef;
    this.firmenName = firmenName;
    this.steuerNummer = steuerNummer;
  }

  public Long getId() {
    return id;
  }

  public String getFirmenName() {
    return firmenName;
  }

  public void setFirmenName(String firmenName) {
    this.firmenName = firmenName;
  }

  public String getFirmenChef() {
    return firmenChef;
  }

  public void setFirmenChef(String firmenChef) {
    this.firmenChef = firmenChef;
  }

  public Long getSteuerNummer() {
    return steuerNummer;
  }

  public void setSteuerNummer(Long steuerNummer) {
    this.steuerNummer = steuerNummer;
  }

  public String info() {
    return "Die Firma " + firmenName + " mit der Steuernummer: " + steuerNummer + " hat als Overlord: " + firmenChef + "." ;
  }


}
