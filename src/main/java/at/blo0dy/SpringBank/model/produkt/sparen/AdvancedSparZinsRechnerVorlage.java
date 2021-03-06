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

  @NumberFormat(style = NumberFormat.Style.PERCENT)
  private BigDecimal zinssatz;

  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal betrag;

  @Min(value = 1, message = "Bitte eine Zahl von 1 bis 5.000,00 angeben.")
  @Max(value = 5000, message = "Bitte eine Zahl von 1 bis 5.000,00 angeben.")
  @NotNull(message = "Bitte eine Zahl von 1 bis 5.000,00 angeben.")
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal monatlicheEinzahlung;

  @Min(value = 1, message = "Bitte eine Zahl von 1 bis 10.000.000  angeben.")
  @Max(value = 10000000, message = "Bitte eine Zahl von 1 bis 10.000.000 angeben.")
  @NotNull(message = "Bitte eine Zahl von 1 bis 10.000.000 angeben.")
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal initialBetrag;


}
