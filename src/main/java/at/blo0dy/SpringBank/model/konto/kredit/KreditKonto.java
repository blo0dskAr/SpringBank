package at.blo0dy.SpringBank.model.konto.kredit;


import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Entity
@Data
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "kreditkonto")
@AllArgsConstructor
@NoArgsConstructor
public class KreditKonto extends Konto {

//  @OneToOne(cascade = CascadeType.ALL)
  @OneToOne
  private KreditKontoAntrag kreditKontoAntrag;

  @Column(name = "kredit_betrag")
  private BigDecimal kreditBetrag;

  @Column(name = "rate")
  private BigDecimal rate;

  @Column(name = "laufzeit")
  private BigInteger laufzeit;


  public KreditKonto(LocalDate eroeffnungsDatum, Long kontonummer, Kunde kunde, BigDecimal aktSaldo, KontoStatusEnum kontoStatus, BigDecimal kreditBetrag, BigDecimal rate, BigInteger laufzeit,  KreditKontoAntrag kreditKontoAntrag) {
    super(eroeffnungsDatum, kontonummer, kunde, aktSaldo, kontoStatus);
    this.kreditKontoAntrag = kreditKontoAntrag;
    this.kreditBetrag = kreditBetrag;
    this.laufzeit = laufzeit;
    this.rate = rate;
  }

}
