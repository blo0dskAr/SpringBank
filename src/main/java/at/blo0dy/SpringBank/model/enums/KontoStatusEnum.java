package at.blo0dy.SpringBank.model.enums;

public enum KontoStatusEnum {
  IN_EROEFFNUNG("In Eröffnung"),
  OFFEN("Offen"),
  GESCHLOSSEN("Geschlossen");

  private final String displayName;

  KontoStatusEnum(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
