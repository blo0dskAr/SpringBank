package at.blo0dy.SpringBank.model.produkt.sparen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SparZinsRechnerVorlage {

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate datum;
  @NumberFormat(style = NumberFormat.Style.PERCENT)
  private double zinssatz;
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
//  @NumberFormat(pattern = "#.###.###.###,##")
  private double betrag;

}
