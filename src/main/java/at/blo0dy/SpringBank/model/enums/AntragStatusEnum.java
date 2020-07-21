package at.blo0dy.SpringBank.model.enums;

public enum AntragStatusEnum {
  EINGEREICHT("Eingereicht"),
  GENEHMIGT("Genehmigt"),
  ABGELEHNT("Abgelehnt"),
  ABGELEHNT_WEIL_NEU_BERECHNET("Abgelehnt, neu Berechnet");

  private final String displayName;

  AntragStatusEnum(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
