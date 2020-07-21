package at.blo0dy.SpringBank.model.enums;

public enum LegiDokumentStatusEnum {
  NEU("Neu"),
  BEARBEITET("Bearbeitet");

  private final String displayName;

  LegiDokumentStatusEnum(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
