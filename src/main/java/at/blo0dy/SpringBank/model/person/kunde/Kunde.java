package at.blo0dy.SpringBank.model.person.kunde;

import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.person.Person;
import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "kunde")
@PrimaryKeyJoinColumn(name = "id")
public class Kunde extends Person implements UserDetails {

  @Column(name = "kundennummer", nullable = false, unique = true)
  @NotBlank(message = "kundennummer darf nicht leer sein.")
  private String kundennummer;

  // TODO: hier muss mal ein pattern her, damit gscheite passwoerter vergeben werden. zum testen ists aber muehsam...
  @Column(name = "password")
  private String password;

  // ToDo: Eigene klassen? Oberklasse kontakt? kann mehr als eine tel haben etc.
  @NotBlank(message = "Bei einer Telefonnummer dürfen nur Ziffern angegeben werden 5-20 Zeichen sind einzuhalten.")
  @Pattern(regexp = "^[+]?[0-9]{5,20}", message = "Bei einer Telefonnummer dürfen nur Ziffern angegeben werden 5-20 Zeichen sind einzuhalten.")
  private String telefonNummer;
  @NotBlank(message = "Email im Format <name>@<domain>.<land> angeben.")
  @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+.[A-Z]{2,6}$", message = "Email im Format <name>@<domain>.<land> angeben.", flags = Pattern.Flag.CASE_INSENSITIVE)
  private String emailAdresse;

  private String rolle = "customer";

  @OneToMany(mappedBy = "kunde",
                cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE})
  private List<Konto> kontenListe;

  private boolean isLegi;
  private boolean hasAcceptedAGB ;
  private boolean isActive;
  private boolean firstLoginDone ;

  @Column(name = "connected_giro")
  @Pattern(regexp = "^AT[0-9a-zA-Z]{18}$", message = "Bitte IBAN im Format: \"AT## #### #### #### ####\" angeben. (Abstände nicht notwendig)")
  private String connectedGiro;

  public Kunde() { }

  public Kunde(String vorname, String nachname, Adresse adresse, String kundennummer, String telefonNummer, String emailAdresse, String password, boolean isLegi,
               boolean hasAcceptedAGB, boolean isActive, boolean firstLoginDone, String connectedGiro, LocalDate geburtsDatum) {
    super(vorname, nachname, adresse, geburtsDatum);
    this.kundennummer = kundennummer;
    this.telefonNummer = telefonNummer;
    this.emailAdresse = emailAdresse;
    this.password = password;
    this.isLegi = isLegi;
    this.hasAcceptedAGB = hasAcceptedAGB;
    this.isActive = isActive;
    this.firstLoginDone = firstLoginDone;
    this.rolle = "customer";
    this.connectedGiro = connectedGiro;
  }


  // Stuff den ich implementieren soll aufgrund des Userdetail Implementierung.
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.asList(new SimpleGrantedAuthority("customer"));
  }

  @Override
  public String getUsername() {
    return kundennummer;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }


}
