package at.blo0dy.SpringBank.model.person.kunde;

import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class KundeRegistrationForm {

  @NotBlank(message = "Vorname muss angegeben werden.")
  @Pattern(regexp = "^[a-zA-ZäÄöÖüÜß -]+$", message = "Darf nicht leer sein & nur aus Buchstaben und dem Sonderzeichen \"-\" bestehen")
  private String vorname;

  @NotBlank(message = "Nachname muss angegeben werden.")
  @Pattern(regexp = "^[a-zA-ZäÄöÖüÜß -]+$", message = "Darf nicht leer sein & nur aus Buchstaben und dem Sonderzeichen \"-\" bestehen")
  private String nachname;

  @NotBlank(message = "Password muss angegeben werden.")
  @Pattern(regexp = "^[a-zA-Z0-9äÄöÖüÜß -]+$", message = "Darf nicht leer sein & nur aus Buchstaben und dem Sonderzeichen \"-\" bestehen")
  private String password;

  @Valid
  private Adresse adresse;

  @NotBlank(message = "Bei einer Telefonnummer dürfen nur Ziffern angegeben werden 5-20 Zeichen sind einzuhalten.")
  @Pattern(regexp = "^[+]?[0-9]{5,20}", message = "Bei einer Telefonnummer dürfen nur Ziffern angegeben werden 5-20 Zeichen sind einzuhalten.")
  private String telefonNummer;
  @NotBlank(message = "Email im Format <name>@<domain>.<land> angeben.")
  @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+.[A-Z]{2,6}$", message = "Email im Format <name>@<domain>.<land> angeben.", flags = Pattern.Flag.CASE_INSENSITIVE)
  private String emailAdresse;

  @NotBlank(message = "Bitte IBAN im Format: \"AT## #### #### #### ####\" angeben. (Abstände nicht notwendig)")
  @Column(name = "connected_giro")
  @Pattern(regexp = "^AT[0-9a-zA-Z]{18}$", message = "Bitte IBAN im Format: \"AT## #### #### #### ####\" angeben. (Abstände nicht notwendig)")
  private String connectedGiro;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Past(message = "Darf nicht leer sein, muss in der Vergangenheit liegen.")
  @NotNull(message = "Darf nicht leer sein, muss in der Vergangenheit liegen.")
  private LocalDate geburtsDatum;


  private boolean hasAcceptedAGB ;


  public Kunde toUser(PasswordEncoder passwordEncoder, String kundennummer) {

    return new Kunde(vorname,  nachname, new Adresse(adresse.getStrasse(), adresse.getPlz(), adresse.getOrt(), adresse.getLand()), kundennummer,  telefonNummer,  emailAdresse, passwordEncoder.encode(password), false, hasAcceptedAGB, false, false, connectedGiro, geburtsDatum );

  }
}
