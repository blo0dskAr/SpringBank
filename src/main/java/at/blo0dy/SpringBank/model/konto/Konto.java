package at.blo0dy.SpringBank.model.konto;

import at.blo0dy.SpringBank.model.antrag.KontoAntrag;
import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "konto")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
public class Konto {

  public Konto(LocalDateTime eroeffnungsDatum, Long kontonummer, Kunde kunde, BigDecimal aktSaldo, KontoStatusEnum kontoStatus, KontoAntrag kontoAntrag) {
    this.eroeffnungsDatum = eroeffnungsDatum;
    this.kontonummer = kontonummer;
    this.kunde = kunde;
    this.aktSaldo = aktSaldo;
    this.kontoStatus = kontoStatus;
    this.kontoAntrag = kontoAntrag;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "eroeffnungs_datum")
  private LocalDateTime eroeffnungsDatum;

  @Column(name = "kontonummer")
  private Long kontonummer;

//  @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
  @ManyToOne
  @JoinColumn(name="kunde_id")
  private Kunde kunde ;

  @OneToOne
  private KontoAntrag kontoAntrag;

  @Column(name = "akt_saldo")
  private BigDecimal aktSaldo;

  // private List<Kontobuchungen>

//  @OneToOne(cascade = CascadeType.ALL)
//  private KontoAntrag kontoAntrag ;

  @Column(name = "konto_status")
  @Enumerated(EnumType.STRING)
  private KontoStatusEnum kontoStatus;


}
