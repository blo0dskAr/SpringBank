package at.blo0dy.SpringBank.model.konto.sparen;

import at.blo0dy.SpringBank.model.konto.Konto;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Data
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "sparkonto")
public class SparKonto extends Konto {

  @Column(name = "connected_giro")
  private String connectedGiro;

  // glaub nicht, dass ich das brauch. eher zinssatz mal in db persistieren und über service->repo->(utility)-> draufzugreifen
 /* @Column("zinssatz")
  private BigDecimal zinssatz = BigDecimal.valueOf(5); */






}
