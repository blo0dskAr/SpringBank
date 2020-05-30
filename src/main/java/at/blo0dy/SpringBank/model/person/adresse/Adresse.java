package at.blo0dy.SpringBank.model.person.adresse;

public class Adresse {

  String strasse;
  String plz;
  String ort;
  String land;

  public Adresse() {
  }

  public Adresse(String strasse, String plz, String ort, String land) {
    this.strasse = strasse;
    this.plz = plz;
    this.ort = ort;
    this.land = land;
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
}
