package at.blo0dy.SpringBank.model.antrag.sparen;

import at.blo0dy.SpringBank.model.enums.AntragsStatusEnum;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sparkontoantrag")
public class SparKontoAntrag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "antrags_datum")
  private LocalDateTime antragsDatum;

  @Column(name = "antrags_status")
  private AntragsStatusEnum antragsStatus;

  @Column(name = "erst_auftrag")
  private BigDecimal erstAuftrag;

  @Column(name = "dauer_auftrag")
  private BigDecimal dauerAuftrag;

  @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
  @JoinColumn(name = "kunde_id")
  private Kunde kunde;

}
