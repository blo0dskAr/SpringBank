package at.blo0dy.SpringBank.model.konto.giro;
import at.blo0dy.SpringBank.model.antrag.KontoAntrag;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.kontoBuchung.KontoBuchung;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "girokonto")
@AllArgsConstructor
@NoArgsConstructor
public class GiroKonto extends Konto {

  private BigDecimal ueberziehungsRahmen;

  public GiroKonto(LocalDateTime eroeffnungsDatum, Long kontonummer, Kunde kunde, BigDecimal aktSaldo, KontoStatusEnum kontoStatus, KontoAntrag kontoAntrag, BigDecimal ueberziehungsRahmen, List<KontoBuchung> kontoBuchungList) {
    super(eroeffnungsDatum, kontonummer, kunde, aktSaldo, kontoStatus, kontoAntrag, kontoBuchungList);
    this.ueberziehungsRahmen = ueberziehungsRahmen;
  }

  //  orig
//  public GiroKonto(LocalDateTime eroeffnungsDatum, Long kontonummer, Kunde kunde, BigDecimal aktSaldo, KontoStatusEnum kontoStatus, GiroKontoAntrag giroKontoAntrag, BigDecimal ueberziehungsRahmen) {
//    super(eroeffnungsDatum, kontonummer, kunde, aktSaldo, kontoStatus);
//    this.giroKontoAntrag = giroKontoAntrag;
//    this.ueberziehungsRahmen = ueberziehungsRahmen;
//  }
}










