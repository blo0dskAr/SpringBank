package at.blo0dy.SpringBank.model.produkt.sparen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvancedSparZinsRechnerVorlage {

  @Min(value = 1, message = "Bitte eine Zahl von 1 bis 30 angeben.")
  @Max(value = 30, message = "Bitte eine Zahl von 1 bis 30 angeben.")
  @NotNull(message = "Bitte eine Zahl von 1 bis 30 angeben.")
  @NumberFormat(style = NumberFormat.Style.NUMBER)
  private BigInteger anlagedauer;

  // TODO: den vergeb ich momentan an 2 stellen, da gibts was zu refactoren. nur noch aus der vorlage nehmen - und vorlage nimmts im optimalfall aus der DB
  @NumberFormat(style = NumberFormat.Style.PERCENT)
  private BigDecimal zinssatz;
//  private BigDecimal zinssatz = BigDecimal.valueOf(6.00);

  @NumberFormat(style = NumberFormat.Style.CURRENCY)
//  @NumberFormat(pattern = "#.###.###.###,##")
  private BigDecimal betrag;

  @Min(value = 1, message = "Bitte eine Zahl von 1 bis 5.000,00 angeben.")
  @Max(value = 5000, message = "Bitte eine Zahl von 1 bis 5.000,00 angeben.")
  @NotNull(message = "Bitte eine Zahl von 1 bis 5.000,00 angeben.")
  @NumberFormat(style = NumberFormat.Style.NUMBER)
  private BigDecimal monatlicheEinzahlung;

  @Min(value = 1, message = "Bitte eine Zahl von 1 bis 10.000.000  angeben.")
  @Max(value = 10000000, message = "Bitte eine Zahl von 1 bis 10.000.000 angeben.")
  @NotNull(message = "Bitte eine Zahl von 1 bis 10.000.000  angeben.")
  @NumberFormat(style = NumberFormat.Style.NUMBER)
  private BigDecimal initialBetrag;

  // TODO: den vergeb ich momentan an 2 stellen, da gibts was zu refactoren. nur noch aus der vorlage nehmen - und vorlage nimmts im optimalfall aus der DB
  public BigDecimal getZinssatz() {
    return zinssatz;
  }
}
