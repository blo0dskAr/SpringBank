package at.blo0dy.SpringBank.model.produkt.kredit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KreditRechnerErgebnis {

  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal kreditBetrag;

  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal zinsAnteil;

  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal gesamtBelastung;

  @NumberFormat(style = NumberFormat.Style.NUMBER)
  private BigDecimal monatlicheRate;

}
