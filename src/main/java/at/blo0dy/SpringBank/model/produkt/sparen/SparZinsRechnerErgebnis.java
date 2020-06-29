package at.blo0dy.SpringBank.model.produkt.sparen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SparZinsRechnerErgebnis {

  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private double anfangsBetrag;
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private double zinsen;
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private double kest;
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private double ergebnisVorKest;
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private double ergebnisNachKest;

}
