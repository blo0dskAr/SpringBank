package at.blo0dy.SpringBank.model.antrag;

import at.blo0dy.SpringBank.model.enums.AntragsStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "kontoantrag")
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
public class KontoAntrag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "antrag_datum")
  private LocalDateTime antragsDatum;

  @Column(name = "antrags_status")
  @Enumerated(EnumType.STRING)
  private AntragsStatusEnum antragsStatus;

  @Column(name = "kundennummer")
  private Long kundennummer;

  public KontoAntrag(LocalDateTime antragsDatum, AntragsStatusEnum antragsStatus, Long kundennummer) {
    this.antragsDatum = antragsDatum;
    this.antragsStatus = antragsStatus;
    this.kundennummer = kundennummer;
  }
}


