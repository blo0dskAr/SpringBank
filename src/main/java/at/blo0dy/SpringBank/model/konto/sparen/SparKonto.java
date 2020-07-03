package at.blo0dy.SpringBank.model.konto.sparen;

import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "sparkonto")
@AllArgsConstructor
@NoArgsConstructor
public class SparKonto extends Konto {

  @Column(name = "connected_giro")
  private String connectedGiro;

  @OneToOne
  private SparKontoAntrag sparKontoAntrag;

  public SparKonto(LocalDate eroeffnungsDatum, Long kontonummer, Kunde kunde, BigDecimal aktSaldo, KontoStatusEnum kontoStatus, String connectedGiro, SparKontoAntrag sparKontoAntrag) {
    super(eroeffnungsDatum, kontonummer, kunde, aktSaldo, kontoStatus);
    this.connectedGiro = connectedGiro;
    this.sparKontoAntrag = sparKontoAntrag;
  }


  // glaub nicht, dass ich das brauch. eher zinssatz mal in db persistieren und über service->repo->(utility)-> draufzugreifen
 /* @Column("zinssatz")
  private BigDecimal zinssatz = BigDecimal.valueOf(5); */






}
