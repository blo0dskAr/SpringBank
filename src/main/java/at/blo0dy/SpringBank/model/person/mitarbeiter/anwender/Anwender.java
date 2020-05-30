package at.blo0dy.SpringBank.model.person.mitarbeiter.anwender;


import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import org.yaml.snakeyaml.DumperOptions;

import javax.persistence.*;


@Entity
@Table(name = "anwender")
public class Anwender extends Mitarbeiter {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column
  private String loginName;

  @Column
  private String password;

  @OneToOne(cascade = CascadeType.ALL)
  private Mitarbeiter mitarbeiter;

  public Anwender() {}

  public Anwender(String loginName, String password) {
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

}
