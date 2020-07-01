package at.blo0dy.SpringBank.model.antrag.person;

import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class KundeRegistrationForm {

  private String vorname;
  private String kundennummer;
  private String nachname;
  private String password;
  private String strasse;
  private String land;
  private String ort;
  private String plz;
  private String telefonNummer;
  private String emailAdresse;


  public Kunde toUser(PasswordEncoder passwordEncoder) {
//    return new Kunde(username, passwordEncoder.encode(password),
//                    fullname, street, city, state, zip, phone );

    return new Kunde(vorname,  nachname, new Adresse(strasse, plz, ort, land), kundennummer,  telefonNummer,  emailAdresse, passwordEncoder.encode(password), true, true, true, true );

  }
}
