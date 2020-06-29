package at.blo0dy.SpringBank.model.produkt.sparen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SparZinsRechnerVorlage {

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate datum;
  private Double zinssatz;
  private Double betrag;

}
