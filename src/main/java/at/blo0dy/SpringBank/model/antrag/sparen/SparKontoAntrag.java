package at.blo0dy.SpringBank.model.antrag.sparen;

import at.blo0dy.SpringBank.model.antrag.KontoAntrag;
import at.blo0dy.SpringBank.model.enums.AntragsStatusEnum;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sparkontoantrag")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor
@AllArgsConstructor
public class SparKontoAntrag extends KontoAntrag {

//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  @Column(name = "id")
//  private Long id;

//  @Column(name = "antrag_datum")
//  private LocalDateTime antragsDatum;
//
//  @Column(name = "antrags_status")
//  @Enumerated(EnumType.STRING)
//  private AntragsStatusEnum antragsStatus;

  @Column(name = "erst_auftrag")
  private BigDecimal erstAuftrag;

  @Column(name = "dauer_auftrag")
  private BigDecimal dauerAuftrag;

//  @Column(name = "kundennummer")
//  private Long kundennummer;

//  @ManyToOne(cascade = {CascadeType.ALL})
//  @ManyToOne
//  @JoinColumn(name = "kunde_id")
//  private Kunde kunde;

  // Custom Constructor for  SparkontoRegistrationForm
  public SparKontoAntrag(LocalDateTime antragsDatum, AntragsStatusEnum antragsStatus, BigDecimal erstAuftrag, BigDecimal dauerAuftrag, Long kundennummer) {
    super(antragsDatum,antragsStatus,kundennummer);
//    this.antragsDatum = antragsDatum;
//    this.antragsStatus = antragsStatus;
    this.erstAuftrag = erstAuftrag;
    this.dauerAuftrag = dauerAuftrag;
//    this.kundennummer = kundennummer;
  }
}
