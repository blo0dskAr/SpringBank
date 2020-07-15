package at.blo0dy.SpringBank.model.enums;


public enum ZahlungAuftragStatusEnum {
  ANGELEGT("Angelegt"),
  DURCHGEFUEHRT("Durchgeführt"),
  STORNIERT("Storniert");


  private final String displayName;

  ZahlungAuftragStatusEnum(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
