package at.blo0dy.SpringBank.model.person.kunde;

import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.person.Person;
import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "kunde")
@PrimaryKeyJoinColumn(name = "id")
public class Kunde extends Person implements UserDetails {

  @Column(name = "kundennummer", nullable = false, unique = true)
  private String kundennummer;

  @Column(name = "password")
  private String password;

  // ToDo: Eigene klassen? Oberklasse kontakt? kann mehr als eine tel haben etc.
  private String telefonNummer;
  private String emailAdresse;

  private String rolle = "customer";

  @OneToMany(mappedBy = "kunde",
//             cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH})
//                cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE})
                cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE})
  private List<Konto> kontenListe;

  private boolean isLegi = true;
  private boolean hasAcceptedAGB = true;
  private boolean isActive = true;
  private boolean firstLoginDone = true;


  public Kunde() { }

  public Kunde(String vorname, String nachname, Adresse adresse, String kundennummer, String telefonNummer, String emailAdresse, String password, boolean isLegi, boolean hasAcceptedAGB, boolean isActive, boolean firstLoginDone) {
    super(vorname, nachname, adresse);
    this.kundennummer = kundennummer;
    this.telefonNummer = telefonNummer;
    this.emailAdresse = emailAdresse;
    this.password = password;
    this.isLegi = isLegi;
    this.hasAcceptedAGB = hasAcceptedAGB;
    this.isActive = isActive;
    this.firstLoginDone = firstLoginDone;
    this.rolle = "customer";
  }

//  @Override
//  public String toString() {
//    return "Kunde{" +
//            "kundenNummer=" + kundennummer +
//            ", telefonNummer='" + telefonNummer + '\'' +
//            ", emailAdresse='" + emailAdresse + '\'' +
//            ", isLegi=" + isLegi +
//            ", hasAcceptedAGB=" + hasAcceptedAGB +
//            ", isActive=" + isActive +
//            ", firstLoginDone=" + firstLoginDone +
//            '}';
//  }

  public void checkActive() {
    if (this.isLegi == true && this.hasAcceptedAGB == true) {
      setActive(true);
    } else {
      setActive(false);
    }
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
    return isActive;
  }

  @Override
  public boolean isAccountNonLocked() {
    return isActive;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return isActive;
  }

  @Override
  public boolean isEnabled() {
    return isActive;
  }
}
