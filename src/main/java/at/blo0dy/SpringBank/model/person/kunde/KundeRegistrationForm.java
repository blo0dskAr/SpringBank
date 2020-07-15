package at.blo0dy.SpringBank.model.person.kunde;

import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
public class KundeRegistrationForm {

  @NotBlank(message = "Vorname must be defined.")
  private String vorname;

/*  // TODO:: das wird man rausnehmne können, und unten dann frisch generieren.
  @Min(124)
  @Max(99999999)
  @NotNull(message = "Kundennummer must be defined.")
  private String kundennummer;*/
  @NotBlank(message = "Nachname must be defined.")
  private String nachname;

  @NotBlank(message = "Password must be defined.")
  private String password;

  @Valid
  private Adresse adresse;

  @NotBlank(message = "Telefonnummer must be defined.")
  private String telefonNummer;

  @NotBlank(message = "EmailAdresse must be defined.")
  private String emailAdresse;

  @NotBlank(message = "Darf nicht leer sein")
  private String connectedGiro;


  public Kunde toUser(PasswordEncoder passwordEncoder, String kundennummer) {

    return new Kunde(vorname,  nachname, new Adresse(adresse.getStrasse(), adresse.getPlz(), adresse.getOrt(), adresse.getLand()), kundennummer,  telefonNummer,  emailAdresse, passwordEncoder.encode(password), true, true, true, true, connectedGiro );

  }
}
