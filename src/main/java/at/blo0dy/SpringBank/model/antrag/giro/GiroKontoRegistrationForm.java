package at.blo0dy.SpringBank.model.antrag.giro;


import at.blo0dy.SpringBank.model.enums.AntragStatusEnum;
import at.blo0dy.SpringBank.model.enums.KontoProduktEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Slf4j
@Data
public class GiroKontoRegistrationForm {


  @DateTimeFormat
  private LocalDateTime antragDatum;
  private boolean ueberziehungsrahmenGewuenscht;
  private Long kundennummer;

  public GiroKontoAntrag toGiroKontoAntrag() {
    log.debug("GiroFormToGiroKontoAntrag wird aufgerufen: " + this.toString());
    return new GiroKontoAntrag(antragDatum,  AntragStatusEnum.EINGEREICHT, ueberziehungsrahmenGewuenscht, kundennummer, KontoProduktEnum.GIRO);

  }
}
