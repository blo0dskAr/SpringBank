package at.blo0dy.SpringBank.model.person.mitarbeiter.loginCredentials;


import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
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

  @CreationTimestamp
  private Timestamp gueltigAb;

  @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH})
  @JoinColumn(name="mitarbeiter_id")
  private Mitarbeiter mitarbeiter;

  public LoginCredentials() {}

  public LoginCredentials(String loginName, String password) {
    this.loginName = loginName;
    this.password = password;
  }

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Timestamp getGueltigAb() {
    return gueltigAb;
  }

  public void setGueltigAb(Timestamp gueltigAb) {
    this.gueltigAb = gueltigAb;
  }

  public Mitarbeiter getMitarbeiter() {
    return mitarbeiter;
  }

  public void setMitarbeiter(Mitarbeiter mitarbeiter) {
    this.mitarbeiter = mitarbeiter;
  }
}