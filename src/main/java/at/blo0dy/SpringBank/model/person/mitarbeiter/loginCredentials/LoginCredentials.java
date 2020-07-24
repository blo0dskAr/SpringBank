package at.blo0dy.SpringBank.model.person.mitarbeiter.loginCredentials;


import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import lombok.Data;

import javax.persistence.*;


@Entity
@Data
@Table(name = "login_credentials")
public class LoginCredentials {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column
  private String loginName;

  @Column
  private String password;

  @Column
  private boolean isActive = true;

  @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH})
  @JoinColumn(name="mitarbeiter_id")
  private Mitarbeiter mitarbeiter;


  public LoginCredentials() {}

  public LoginCredentials(String loginName, String password) {
    this.loginName = loginName;
    this.password = password;
  }

}
