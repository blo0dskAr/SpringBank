package at.blo0dy.SpringBank.model.enums;

public enum ZahlungAuftragArtEnum {
  EINZAHLUNG("Einzahlung"),
  AUSZAHLUNG("Auszahlung");

  private final String displayName;

  ZahlungAuftragArtEnum(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
