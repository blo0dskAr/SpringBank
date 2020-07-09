package at.blo0dy.SpringBank.model.antrag.giro;

import at.blo0dy.SpringBank.model.antrag.KontoAntrag;
import at.blo0dy.SpringBank.model.enums.AntragStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "girokontoantrag")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor
@AllArgsConstructor
public class GiroKontoAntrag extends KontoAntrag {

  @Column(name = "ueberziehungsrahmen_gewuenscht")
  private boolean ueberziehungsrahmenGewuenscht;

  // Custom Constructor for  GirokontoRegistrationForm
  public GiroKontoAntrag(LocalDateTime antragDatum, AntragStatusEnum antragStatus, boolean ueberziehungsrahmenGewuenscht , Long kundennummer) {
    super(antragDatum,antragStatus,kundennummer);
    this.ueberziehungsrahmenGewuenscht = ueberziehungsrahmenGewuenscht;
  }
}
