package at.blo0dy.SpringBank.model.enums;


public enum BuchungsArtEnum {

  SOLL("Soll"),
  HABEN("Haben");

  private final String displayName;

  BuchungsArtEnum(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }





}
