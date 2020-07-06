package at.blo0dy.SpringBank.model.konto.giro;
import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "girokonto")
@AllArgsConstructor
@NoArgsConstructor
public class GiroKonto extends Konto {

//  @OneToOne(cascade = CascadeType.ALL)
  @OneToOne
  private GiroKontoAntrag giroKontoAntrag;

  private BigDecimal ueberziehungsRahmen;

  public GiroKonto(LocalDate eroeffnungsDatum, Long kontonummer, Kunde kunde, BigDecimal aktSaldo, KontoStatusEnum kontoStatus, GiroKontoAntrag giroKontoAntrag, BigDecimal ueberziehungsRahmen) {
    super(eroeffnungsDatum, kontonummer, kunde, aktSaldo, kontoStatus);
    this.giroKontoAntrag = giroKontoAntrag;
    this.ueberziehungsRahmen = ueberziehungsRahmen;
  }
}
