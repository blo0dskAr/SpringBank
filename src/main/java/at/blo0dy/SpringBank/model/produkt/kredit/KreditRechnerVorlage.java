package at.blo0dy.SpringBank.model.produkt.kredit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KreditRechnerVorlage {

  @Min(value = 12, message = "Bitte eine Laufzeit zwischen 12 und 180 Monaten angeben.")
  @Max(value = 180, message = "Bitte eine Laufzeit zwischen 12 und 180 Monaten angeben.")
  @NotNull(message = "Bitte eine Laufzeit zwischen 12 und 180 Monaten angeben.")
  @NumberFormat(style = NumberFormat.Style.NUMBER)
  private BigInteger laufzeit;

  @NumberFormat(style = NumberFormat.Style.PERCENT)
  private BigDecimal zinssatz;

  @Min(value = 1000, message = "Bitte einen Betrag zwischen 1.000 und 80.000 angeben.")
  @Max(value = 80000, message = "Bitte einen Betrag zwischen 1.000 und 80.000 angeben.")
  @NotNull(message = "Bitte einen Betrag zwischen 1.000 und 80.000 angeben.")
  @Digits(integer = 5, fraction = 2, message = "Der Betrag darf maximal 5 Stellen vor dem Komma und 2 Nachkommastellen besitzen")
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal kreditBetrag;

  public BigDecimal getZinssatz() {
    return zinssatz;
  }
}
