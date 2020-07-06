package at.blo0dy.SpringBank.model.antrag.kredit;

import at.blo0dy.SpringBank.model.enums.AntragsStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

  @Column(name = "kredit_auftrag")
  private BigDecimal kreditBetrag;

  @Column(name = "zinssatz")
  private BigDecimal zinssatz;

  @Column(name = "rate")
  private BigDecimal rate;

  @Column(name = "laufzeit")
  private BigInteger laufzeit;

  @Column(name = "gesamt_belastung")
  private BigDecimal gesamtBelastung;

  @Column(name = "kundennummer")
  private Long kundennummer;


  // Custom Constructor for  SparkontoRegistrationForm
  public KreditKontoAntrag(LocalDateTime antragsDatum, AntragsStatusEnum antragsStatus, BigDecimal kreditBetrag, BigDecimal zinssatz, BigDecimal rate, BigInteger laufzeit, BigDecimal gesamtBelastung, Long kundennummer) {
    this.antragsDatum = antragsDatum;
    this.antragsStatus = antragsStatus;
    this.kreditBetrag = kreditBetrag;
    this.rate = rate;
    this.laufzeit = laufzeit;
    this.gesamtBelastung = gesamtBelastung;
    this.kundennummer = kundennummer;
  }
}
