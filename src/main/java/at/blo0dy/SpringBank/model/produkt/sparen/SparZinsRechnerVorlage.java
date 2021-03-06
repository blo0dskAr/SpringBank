package at.blo0dy.SpringBank.model.produkt.sparen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SparZinsRechnerVorlage {


  @NotNull(message = "Bitte gültiges Datum wählen")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate datum;

  @NumberFormat(style = NumberFormat.Style.PERCENT)
  private BigDecimal zinssatz;

  @Min(value = 1, message = "Bitte eine Zahl von 1 bis 10.000.000  angeben.")
  @Max(value = 10000000, message = "Bitte eine Zahl von 1 bis 10.000.000 angeben.")
  @NotNull(message = "Bitte eine Zahl von 1 bis 10.000.000  angeben.")
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal betrag;

}
