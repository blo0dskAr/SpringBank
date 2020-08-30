package at.blo0dy.SpringBank.model.produkt.zinssatz;

import at.blo0dy.SpringBank.model.enums.KontoProduktEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Entity
@Data
@Table(name = "zinssatz")
@AllArgsConstructor
@NoArgsConstructor
public class Zinssatz {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotNull
  @NumberFormat(style = NumberFormat.Style.PERCENT)
  private BigDecimal zinssatz ;

  @Enumerated(EnumType.STRING)
  private KontoProduktEnum produkt;


}
