package at.blo0dy.SpringBank.model.konto.kontoBuchung;


import at.blo0dy.SpringBank.model.enums.BuchungsArtEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "konto_buchung")
@AllArgsConstructor
@NoArgsConstructor
public class KontoBuchung {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  private LocalDate buchungsDatum;
  private LocalDateTime datAnlage;
  private LocalDateTime datAend;

  @DecimalMin(value = "0.01", message = "Bitte einen Betrag zwischen 0,01 und 999.999.999,99 € angeben. Max. 2 Nachkommastellen. Minus beachten!")
  @DecimalMax(value = "999999999.99", message = "Bitte einen Betrag zwischen 0,01 und 999.999.999,99 € angeben. Max. 2 Nachkommastellen. Minus beachten!")
  @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,###,###,###.##")
  @NotNull(message = "Da hats was ")
  @Digits(integer = 12,fraction = 2)
  private BigDecimal buchungsBetrag;

  @DecimalMin(value = "0.01", message = "Bitte nicht mehr als 999.999.999,99 € angeben. Max. 2 Nachkommastellen. Minus beachten!")
  @DecimalMax(value = "999999999.99", message = "Bitte nicht mehr als 999.999.999,99 € angeben. Max. 2 Nachkommastellen. Minus beachten!")
  @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,###,###,###.##")
  @NotNull(message = "Da hats was ")
  @Digits(integer = 12,fraction = 2)
  private BigDecimal saldoNachBuchung;

  @Column(name = "buchungs_text")
  private String buchungsText;

  @ManyToOne()
  @JoinColumn(name = "konto_id")
  private Konto konto;

  @Enumerated(EnumType.STRING)
  @Column(name = "buchungs_art")
  private BuchungsArtEnum buchungsArt;


}
