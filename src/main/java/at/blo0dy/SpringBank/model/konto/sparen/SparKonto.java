package at.blo0dy.SpringBank.model.konto.sparen;
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
@Table(name = "sparkonto")
@AllArgsConstructor
@NoArgsConstructor
public class SparKonto extends Konto {

  @Column(name = "kontoname")
  private String kontoname;

  public SparKonto(LocalDateTime eroeffnungsDatum, Long kontonummer, Kunde kunde, BigDecimal aktSaldo, KontoStatusEnum kontoStatus, KontoAntrag kontoAntrag, List<KontoBuchung> kontoBuchungList, String kontoname) {
    super(eroeffnungsDatum, kontonummer, kunde, aktSaldo, kontoStatus, kontoAntrag, kontoBuchungList);
    this.kontoname = kontoname;
  }


}








