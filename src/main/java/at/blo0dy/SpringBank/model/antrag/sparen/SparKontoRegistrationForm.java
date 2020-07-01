package at.blo0dy.SpringBank.model.antrag.sparen;


import at.blo0dy.SpringBank.model.enums.AntragsStatusEnum;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SparKontoRegistrationForm {


  @DateTimeFormat()
  private LocalDateTime antragsDatum = LocalDateTime.now() ;
  private BigDecimal erstAuftrag;
  private BigDecimal dauerAuftrag;
  private Long kundennummer;

  public SparKontoAntrag toSparKontoAntrag() {
    return new SparKontoAntrag(antragsDatum,  AntragsStatusEnum.EINGEREICHT, erstAuftrag, dauerAuftrag, kundennummer);

  }
}
