package at.blo0dy.SpringBank.model.enums;

public enum DauerAuftragStatusEnum {

  ANGELEGT("Angelegt"),
  STORNIERT("Storniert");

  String displayName;

  DauerAuftragStatusEnum(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
