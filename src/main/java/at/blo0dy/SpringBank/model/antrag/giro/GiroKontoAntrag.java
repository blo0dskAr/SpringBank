package at.blo0dy.SpringBank.model.antrag.giro;

import at.blo0dy.SpringBank.model.enums.AntragsStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "girokontoantrag")
@NoArgsConstructor
@AllArgsConstructor
public class GiroKontoAntrag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "antrag_datum")
  private LocalDateTime antragsDatum;

  @Column(name = "antrags_status")
  @Enumerated(EnumType.STRING)
  private AntragsStatusEnum antragsStatus;

  @Column(name = "ueberziehungsrahmen_gewuenscht")
  private boolean ueberziehungsrahmenGewuenscht;

  @Column(name = "kundennummer")
  private Long kundennummer;

//  @ManyToOne(cascade = {CascadeType.ALL})
//  @ManyToOne
//  @JoinColumn(name = "kunde_id")
//  private Kunde kunde;

  // Custom Constructor for  GirokontoRegistrationForm
  public GiroKontoAntrag(LocalDateTime antragsDatum, AntragsStatusEnum antragsStatus, boolean ueberziehungsrahmenGewuenscht , Long kundennummer) {
    this.antragsDatum = antragsDatum;
    this.antragsStatus = antragsStatus;
    this.ueberziehungsrahmenGewuenscht = ueberziehungsrahmenGewuenscht;
    this.kundennummer = kundennummer;
  }
}
