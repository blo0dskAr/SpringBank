package at.blo0dy.SpringBank.model.antrag.kredit;


import at.blo0dy.SpringBank.model.enums.AntragsStatusEnum;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditUtility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KreditKontoRegistrationForm {


  @DateTimeFormat()
  private LocalDateTime antragsDatum = LocalDateTime.now() ;

  @DecimalMin(value = "1000" , message = "Wert muss zwischen 1.000 € und 80.000 € liegen")
  @DecimalMax(value = "80000", message = "Wert muss zwischen 1.000 € und 80.000 € liegen")
  @Digits(integer = 5, fraction = 0)
  @NotNull(message = "Wert muss zwischen 1.000 € und 80.000 € liegen")
  private BigDecimal kreditBetrag;
  @DecimalMin(value = "1" , message = "Kredit Muss vorher berechnet werden")
  @Digits(integer = 5, fraction = 2, message = "Kredit Muss vorher berechnet werden")
  @NotNull(message = "Kredit Muss vorher berechnet werden")
  private BigDecimal rate;
  @DecimalMin(value = "12", message = "Wert muss zwischen 12 und 180 Monaten liegen")
  @DecimalMax(value = "180", message = "Wert muss zwischen 12 und 180 Monaten liegen")
  @NotNull(message = "Wert muss zwischen 12 und 180 Monaten liegen")
  @Digits(integer = 3, fraction = 0, message = "Wert muss zwischen 12 und 180 Monaten liegen")
  private BigInteger laufzeit;
  @DecimalMin(value = "1" , message = "Bei dem Zinssatz kann was nicht stimmen")
  @DecimalMax(value = "20", message = "Bei dem Zinssatz kann was nicht stimmen")
  @Digits(integer = 4, fraction = 2, message = "Kredit Muss vorher berechnet werden")
  @NotNull(message = "Kredit Muss vorher berechnet werden")
  private BigDecimal zinssatz;
  private Long kundennummer;

  public KreditKontoAntrag toKreditKontoAntrag() {
    return new KreditKontoAntrag(antragsDatum,  AntragsStatusEnum.EINGEREICHT, kreditBetrag, KreditUtility.getZinssatz(), rate, laufzeit, kundennummer);

  }
}
