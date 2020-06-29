package at.blo0dy.SpringBank.start;

import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.model.produkt.sparen.SparenUtility;
import org.apache.tomcat.jni.Local;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Start {

  public static void main(String[] args) {

    Mitarbeiter mitarbeiter = new Mitarbeiter();

    mitarbeiter.setId(1L);
    mitarbeiter.setMitarbeiterNummer("12345");
    mitarbeiter.setPosition("Chef");
    mitarbeiter.setNachname("Wurst");
    mitarbeiter.setVorname("Hans");

    Adresse adresse = new Adresse();
    adresse.setLand("Austria");
    adresse.setOrt("Wien");
    adresse.setPlz("1100");
    adresse.setOrt("Wien");

    mitarbeiter.setAdresse(adresse);

    System.out.println("MAIN: Mitarbeiter erstellt: " + mitarbeiter);


//    for (int i = 1 ; i <= 31; i++) {
//      System.out.println(i + ". des Monats - Tage bis MonatsEnde: " + SparenUtility.getTageBisMonatsEnde(LocalDate.of(2020, 3, i)));
//    }


//    System.out.println("zinsen von heute(" +LocalDate.now() + "): " + SparenUtility.getZinsenBisJahresEnde(LocalDate.now(), 10000) + " EUR.");



  }

}
