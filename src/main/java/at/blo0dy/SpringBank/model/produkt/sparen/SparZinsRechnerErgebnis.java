package at.blo0dy.SpringBank.model.produkt.sparen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SparZinsRechnerErgebnis {

  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal anfangsBetrag;
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal zinsen;
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal kest;
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal ergebnisVorKest;
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal ergebnisNachKest;

}
