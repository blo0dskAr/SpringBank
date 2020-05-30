package at.blo0dy.SpringBank.model.person.mitarbeiter.anwender;


import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;

public class Anwender extends Mitarbeiter {

    String loginName;
    String password;

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
