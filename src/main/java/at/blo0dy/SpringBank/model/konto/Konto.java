package at.blo0dy.SpringBank.model.konto;

import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "konto")
@Inheritance(strategy = InheritanceType.JOINED)
public class Konto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "eroeffnungs_datum")
  private LocalDate eroeffnungsDatum;

  @Column(name = "kontonummer")
  private Long kontonummer;

//  @Valid
  @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
  @JoinColumn(name="kunde_id")
  private Kunde kunde ;

  @Column(name = "akt_saldo")
  private BigDecimal aktSaldo;

  // private List<Kontobuchungen>

//  @OneToOne(cascade = CascadeType.ALL)
//  private KontoAntrag kontoAntrag ;

  @Column(name = "konto_status")
  private KontoStatusEnum kontoStatus;


}
