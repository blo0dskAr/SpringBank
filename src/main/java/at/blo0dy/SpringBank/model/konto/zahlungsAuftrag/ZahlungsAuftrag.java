package at.blo0dy.SpringBank.model.konto.zahlungsAuftrag;


import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "zahlungs_auftrag")
@NoArgsConstructor
@AllArgsConstructor
public class ZahlungsAuftrag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  private LocalDate auftragsDatum;
  private LocalDateTime datAnlage;
  private LocalDateTime datAend;

  private BigDecimal betrag;

  @Enumerated(EnumType.STRING)
  @Column(name = "auftrags_status")
  private ZahlungAuftragStatusEnum auftragsStatus;

  @Enumerated(EnumType.STRING)
  @Column(name = "auftrags_art")
  private ZahlungAuftragArtEnum auftragsArt;

}
