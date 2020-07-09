package at.blo0dy.SpringBank.model.konto.kredit;


import at.blo0dy.SpringBank.model.antrag.KontoAntrag;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Data
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "kreditkonto")
@AllArgsConstructor
@NoArgsConstructor
public class KreditKonto extends Konto {

  @Column(name = "kredit_betrag")
  private BigDecimal kreditBetrag;

  @Column(name = "rate")
  private BigDecimal rate;

  @Column(name = "laufzeit")
  private BigInteger laufzeit;


  public KreditKonto(LocalDateTime eroeffnungsDatum, Long kontonummer, Kunde kunde, BigDecimal aktSaldo, KontoStatusEnum kontoStatus, KontoAntrag kontoAntrag, BigDecimal kreditBetrag, BigDecimal rate, BigInteger laufzeit) {
    super(eroeffnungsDatum, kontonummer, kunde, aktSaldo, kontoStatus, kontoAntrag);
    this.kreditBetrag = kreditBetrag;
    this.rate = rate;
    this.laufzeit = laufzeit;
  }


//    public KreditKonto(LocalDateTime eroeffnungsDatum, Long kontonummer, Kunde kunde, BigDecimal aktSaldo, KontoStatusEnum kontoStatus, KontoAntrag kontoAntrag, BigDecimal kreditBetrag, BigDecimal rate, BigInteger laufzeit) {
//    super(eroeffnungsDatum, kontonummer, kunde, aktSaldo, kontoStatus, kontoAntrag);
//    this.kreditBetrag = kreditBetrag;
//    this.laufzeit = laufzeit;
//    this.rate = rate;
//  }

  // orig
/*  public KreditKonto(LocalDateTime eroeffnungsDatum, Long kontonummer, Kunde kunde, BigDecimal aktSaldo, KontoStatusEnum kontoStatus, BigDecimal kreditBetrag, BigDecimal rate, BigInteger laufzeit, KreditKontoAntrag kreditKontoAntrag) {
    super(eroeffnungsDatum, kontonummer, kunde, aktSaldo, kontoStatus);
    this.kreditKontoAntrag = kreditKontoAntrag;
    this.kreditBetrag = kreditBetrag;
    this.laufzeit = laufzeit;
    this.rate = rate;
  }*/

}
