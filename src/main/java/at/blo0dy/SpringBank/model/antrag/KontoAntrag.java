package at.blo0dy.SpringBank.model.antrag;

import at.blo0dy.SpringBank.model.enums.AntragStatusEnum;
import at.blo0dy.SpringBank.model.enums.KontoProduktEnum;
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
  private LocalDateTime antragDatum;

  @Column(name = "antrag_status")
  @Enumerated(EnumType.STRING)
  private AntragStatusEnum antragStatus;

  @Column(name = "kundennummer")
  private Long kundennummer;

  @Column(name = "produkt")
  @Enumerated(EnumType.STRING)
  private KontoProduktEnum produkt;

  public KontoAntrag(LocalDateTime antragDatum, AntragStatusEnum antragStatus, Long kundennummer, KontoProduktEnum produkt) {
    this.antragDatum = antragDatum;
    this.antragStatus = antragStatus;
    this.kundennummer = kundennummer;
    this.produkt = produkt;
  }

}


