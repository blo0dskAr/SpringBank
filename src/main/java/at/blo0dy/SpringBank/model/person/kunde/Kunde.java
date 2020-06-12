package at.blo0dy.SpringBank.model.person.kunde;

import at.blo0dy.SpringBank.model.person.Person;
import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "kunde")
@PrimaryKeyJoinColumn(name = "id")
public class Kunde extends Person  {

/*  @Id
  //@GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;*/

  @Column(name = "kundennummer")
  private String kundennummer;
  private String password;

  // ToDo: Eigene klassen? Oberklasse kontakt? kann mehr als eine tel haben etc.
  private String telefonNummer;
  private String emailAdresse;

  private String rolle = "customer";

//  private Adresse adresse;
  // private List<Konto> konten;

  private boolean isLegi = true;
  private boolean hasAcceptedAGB = true;
  private boolean isActive = true;
  private boolean firstLoginDone = true;


  public Kunde() { }

  public Kunde(String vorname, String nachname, Adresse adresse, String kundennummer, String telefonNummer, String emailAdresse, boolean isLegi, boolean hasAcceptedAGB, boolean isActive, boolean firstLoginDone) {
    super(vorname, nachname, adresse);
    this.kundennummer = kundennummer;
    this.telefonNummer = telefonNummer;
    this.emailAdresse = emailAdresse;
    this.isLegi = isLegi;
    this.hasAcceptedAGB = hasAcceptedAGB;
    this.isActive = isActive;
    this.firstLoginDone = firstLoginDone;
    this.rolle = "customer";
  }

  @Override
  public String toString() {
    return "Kunde{" +
            "kundenNummer=" + kundennummer +
            ", telefonNummer='" + telefonNummer + '\'' +
            ", emailAdresse='" + emailAdresse + '\'' +
            ", isLegi=" + isLegi +
            ", hasAcceptedAGB=" + hasAcceptedAGB +
            ", isActive=" + isActive +
            ", firstLoginDone=" + firstLoginDone +
            '}';
  }

  public void checkActive() {
    if (this.isLegi == true && this.hasAcceptedAGB == true) {
      setActive(true);
    } else {
      setActive(false);
    }
  }

/*  // Stuff den ich implementieren soll aufgrund des Userdetail Implementierung.
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.asList(new SimpleGrantedAuthority("ROLE_customer"));
  }

  @Override
  public String getUsername() {
    return getKundenNummer();
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
  }*/
}
