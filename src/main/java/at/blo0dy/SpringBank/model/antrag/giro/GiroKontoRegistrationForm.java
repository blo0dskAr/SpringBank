package at.blo0dy.SpringBank.model.antrag.giro;


import at.blo0dy.SpringBank.model.enums.AntragsStatusEnum;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class GiroKontoRegistrationForm {


  @DateTimeFormat()
  private LocalDateTime antragsDatum = LocalDateTime.now() ;
  private boolean ueberziehungsrahmenGewuenscht;
  private Long kundennummer;

  public GiroKontoAntrag toGiroKontoAntrag() {
    return new GiroKontoAntrag(antragsDatum,  AntragsStatusEnum.EINGEREICHT, ueberziehungsrahmenGewuenscht, kundennummer);

  }
}
