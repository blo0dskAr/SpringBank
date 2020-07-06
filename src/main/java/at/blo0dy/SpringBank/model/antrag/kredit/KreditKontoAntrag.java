package at.blo0dy.SpringBank.model.antrag.kredit;

import at.blo0dy.SpringBank.model.enums.AntragsStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "kreditkontoantrag")
@NoArgsConstructor
@AllArgsConstructor
public class KreditKontoAntrag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "antrag_datum")
  private LocalDateTime antragsDatum;

  @Column(name = "antrags_status")
  @Enumerated(EnumType.STRING)
  private AntragsStatusEnum antragsStatus;

  @DecimalMin(value = "1000", message = "Bitte einen Betrag zwischen 1.000 und 80.000 angeben.")
  @DecimalMax(value = "80000", message = "Bitte einen Betrag zwischen 1.000 und 80.000 angeben.")
  @NotNull(message = "Bitte einen Betrag zwischen 1.000 und 80.000 angeben.")
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  @Column(name = "kredit_auftrag")
  private BigDecimal kreditBetrag;

  @NumberFormat(style = NumberFormat.Style.PERCENT)
  @NotNull(message = "Bitte einen Zinssatz zwischen 1 und 100 % angeben.")
  @Column(name = "zinssatz")
  private BigDecimal zinssatz;

  @Column(name = "rate")
  private BigDecimal rate;

  @Min(value = 12, message = "Bitte eine Laufzeit zwischen 12 und 180 Monaten angeben.")
  @Max(value = 180, message = "Bitte eine Laufzeit zwischen 12 und 180 Monaten angeben.")
  @NotNull(message = "Bitte eine Laufzeit zwischen 12 und 180 Monaten angeben.")
  @NumberFormat(style = NumberFormat.Style.NUMBER)
  @Column(name = "laufzeit")
  private BigInteger laufzeit;

  @DecimalMin(value = "1000", message = "Bitte einen Betrag zwischen 1.000 und 80.000 angeben.")
  @DecimalMax(value = "80000", message = "Bitte einen Betrag zwischen 1.000 und 80.000 angeben.")
  @NotNull(message = "Bitte einen Betrag zwischen 1.000 und 80.000 angeben.")
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  @Column(name = "gesamt_belastung")
  private BigDecimal gesamtBelastung;

  @Column(name = "kundennummer")
  private Long kundennummer;


  // Custom Constructor for  SparkontoRegistrationForm
  public KreditKontoAntrag(LocalDateTime antragsDatum, AntragsStatusEnum antragsStatus, BigDecimal kreditBetrag, BigDecimal zinssatz, BigDecimal rate, BigInteger laufzeit, BigDecimal gesamtBelastung, Long kundennummer) {
    this.antragsDatum = antragsDatum;
    this.antragsStatus = antragsStatus;
    this.kreditBetrag = kreditBetrag;
    this.zinssatz = zinssatz;
    this.rate = rate;
    this.laufzeit = laufzeit;
    this.gesamtBelastung = gesamtBelastung;
    this.kundennummer = kundennummer;
  }
}
