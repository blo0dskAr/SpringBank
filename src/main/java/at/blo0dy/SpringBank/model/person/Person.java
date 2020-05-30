package at.blo0dy.SpringBank.model.person;

public class Person {

  String vorname;
  String nachname;

  public Person() {}

  public Person(String vorname, String nachname) {
    this.vorname = vorname;
    this.nachname = nachname;
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
}
