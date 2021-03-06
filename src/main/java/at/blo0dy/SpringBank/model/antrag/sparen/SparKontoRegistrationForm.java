package at.blo0dy.SpringBank.model.antrag.sparen;


import at.blo0dy.SpringBank.model.enums.AntragStatusEnum;
import at.blo0dy.SpringBank.model.enums.KontoProduktEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Data
public class SparKontoRegistrationForm {


  @DateTimeFormat
  private LocalDateTime antragDatum ;
  @DecimalMin(value = "0", message = "Bitte einen Betrag zw. 0 und 50.000,00 wählen. Darf auch leer bleiben")
  @DecimalMax(value = "50000", message = "Bitte einen Betrag zw. 0 und 50.000,00 wählen. Darf auch leer bleiben")
  @Digits(integer = 5, fraction = 2, message = "Bitte einen Betrag zw. 0 und 50.000,00 wählen. Darf leer bleiben, maximal 2 Nachkommastellen")
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal erstAuftrag;
  @DecimalMin(value = "0", message = "Bitte einen Betrag zw. 0 und 5.000,00 wählen. Darf auch leer bleiben")
  @DecimalMax(value = "5000", message = "Bitte einen Betrag zw. 0 und 5.000,00 wählen. Darf auch leer bleiben")
  @Digits(integer = 4, fraction = 2, message = "Bitte einen Betrag zw. 0 und 5.000,00 wählen. Darf leer bleiben, maximal 2 Nachkommastellen")
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal dauerAuftrag;
  private Long kundennummer;

  public SparKontoAntrag toSparKontoAntrag() {
    log.debug("SparFormtoSparKontoAntrag wird aufgerufen: " + antragDatum + " " + erstAuftrag + " " + dauerAuftrag + " " + kundennummer);
    if (erstAuftrag == null) {
      setErstAuftrag(BigDecimal.ZERO);
    }
    if (dauerAuftrag == null) {
      setDauerAuftrag(BigDecimal.ZERO);
    }
    return new SparKontoAntrag(antragDatum,  AntragStatusEnum.EINGEREICHT, erstAuftrag, dauerAuftrag, kundennummer, KontoProduktEnum.SPAREN);

  }
}
