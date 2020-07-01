package at.blo0dy.SpringBank.model.produkt.sparen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvancedSparZinsRechnerErgebnis {

  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal anfangsBetrag;
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal summeMonatlicheEinzahlungen;
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal zinsAnteil;
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal kestAnteil;
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal endKapital;

}
