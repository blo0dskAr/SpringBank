package at.blo0dy.SpringBank.model.antrag.sparen;

import at.blo0dy.SpringBank.model.antrag.KontoAntrag;
import at.blo0dy.SpringBank.model.enums.AntragStatusEnum;
import at.blo0dy.SpringBank.model.enums.KontoProduktEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sparkontoantrag")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor
@AllArgsConstructor
public class SparKontoAntrag extends KontoAntrag {

  @DecimalMin(value = "0", message = "Bitte einen Betrag zw. 0 und 50.000,00 wählen. Darf auch leer bleiben")
  @DecimalMax(value = "50000", message = "Bitte einen Betrag zw. 0 und 50.000,00 wählen. Darf auch leer bleiben")
  @Digits(integer = 5, fraction = 2, message = "Bitte einen Betrag zw. 0 und 50.000,00 wählen. Darf leer bleiben, maximal 2 Nachkommastellen")
  @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,###,###,###.##" )
  @Column(name = "erst_auftrag")
  @NotNull(message = "Darf nicht leer sein")
  private BigDecimal erstAuftrag;

  @DecimalMin(value = "0", message = "Bitte einen Betrag zw. 0 und 5.000,00 wählen. Darf auch leer bleiben")
  @DecimalMax(value = "5000", message = "Bitte einen Betrag zw. 0 und 5.000,00 wählen. Darf auch leer bleiben")
  @Digits(integer = 4, fraction = 2, message = "Bitte einen Betrag zw. 0 und 5.000,00 wählen. Darf leer bleiben, maximal 2 Nachkommastellen")
  @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,###,###,###.##" )
  @Column(name = "dauer_auftrag")
  @NotNull(message = "Darf nicht leer sein")
  private BigDecimal dauerAuftrag;


  // Custom Constructor for  SparkontoRegistrationForm
  public SparKontoAntrag(LocalDateTime antragDatum, AntragStatusEnum antragStatus, BigDecimal erstAuftrag, BigDecimal dauerAuftrag, Long kundennummer, KontoProduktEnum produkt) {
    super(antragDatum,antragStatus,kundennummer, produkt);
    this.erstAuftrag = erstAuftrag;
    this.dauerAuftrag = dauerAuftrag;
  }


}
