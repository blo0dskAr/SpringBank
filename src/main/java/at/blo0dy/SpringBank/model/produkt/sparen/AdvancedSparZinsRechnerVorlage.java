package at.blo0dy.SpringBank.model.produkt.sparen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvancedSparZinsRechnerVorlage {


  @NumberFormat(style = NumberFormat.Style.NUMBER)
  private int anlagedauer;
  // TODO: den vergeb ich momentan an 2 stellen, da gibts was zu refactoren. nur noch aus der vorlage nehmen - und vorlage nimmts im optimalfall aus der DB
  @NumberFormat(style = NumberFormat.Style.PERCENT)
  private double zinssatz = 6.00;
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
//  @NumberFormat(pattern = "#.###.###.###,##")
  private double betrag;
  @NumberFormat(style = NumberFormat.Style.NUMBER)
  private double monatlicheEinzahlung;
  @NumberFormat(style = NumberFormat.Style.NUMBER)
  private double initialBetrag;

  // TODO: den vergeb ich momentan an 2 stellen, da gibts was zu refactoren. nur noch aus der vorlage nehmen - und vorlage nimmts im optimalfall aus der DB
  public double getZinssatz() {
    return zinssatz;
  }
}
