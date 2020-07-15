package at.blo0dy.SpringBank.model.enums;

public enum KontoProduktEnum {
  SPAREN("Sparen"),
  KREDIT("Kredit"),
  GIRO("Giro");

  private final String displayName;

  KontoProduktEnum(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
