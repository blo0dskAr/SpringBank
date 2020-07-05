package at.blo0dy.SpringBank.model.antrag.kredit;


import at.blo0dy.SpringBank.model.enums.AntragsStatusEnum;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class KreditKontoRegistrationForm {


  @DateTimeFormat()
  private LocalDateTime antragsDatum = LocalDateTime.now() ;
  private BigDecimal kreditBetrag;
  private BigDecimal rate;
  private BigInteger Laufzeit;
  private BigDecimal zinssatz;
  private Long kundennummer;

  public KreditKontoAntrag toKreditKontoAntrag() {
    return new KreditKontoAntrag(antragsDatum,  AntragsStatusEnum.EINGEREICHT, kreditBetrag, zinssatz, rate, Laufzeit, kundennummer);

  }
}
