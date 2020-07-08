package at.blo0dy.SpringBank.model.antrag.kredit;


import at.blo0dy.SpringBank.model.enums.AntragsStatusEnum;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditUtility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KreditKontoRegistrationForm {


  @DateTimeFormat()
  private LocalDateTime antragsDatum = LocalDateTime.now() ;

  // Hier aktuell keine Formvalidation Annotationen notwendig, wird durch KreditRechnerVorlage geprüft
  private BigDecimal kreditBetrag;
  private BigDecimal rate;
  private BigInteger laufzeit;
  private BigDecimal zinssatz;
  private BigDecimal gesamtBelastung;
  private Long kundennummer;

  public KreditKontoAntrag toKreditKontoAntrag() {
    log.debug("toKreditKontoAntrag Methode: " + this.toString());
    return new KreditKontoAntrag(antragsDatum,  AntragsStatusEnum.EINGEREICHT, kreditBetrag, zinssatz, rate, laufzeit, gesamtBelastung, kundennummer);

  }
}
