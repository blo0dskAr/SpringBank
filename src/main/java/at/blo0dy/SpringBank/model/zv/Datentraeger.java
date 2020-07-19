package at.blo0dy.SpringBank.model.zv;

import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Datentraeger {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  private int anzahl;

  private BigDecimal summe;

  private LocalDateTime datAnlage;

  @OneToMany(mappedBy = "datentraeger")
  private List<ZahlungsAuftrag> zahlungsAuftragList;

  @Enumerated(EnumType.STRING)
  private ZahlungAuftragArtEnum datentraegerArt;


  public Datentraeger(int anzahl, BigDecimal summe, LocalDateTime datAnlage, List<ZahlungsAuftrag> zahlungsAuftragList, ZahlungAuftragArtEnum datentraegerArt) {
    this.anzahl = anzahl;
    this.summe = summe;
    this.datAnlage = datAnlage;
    this.zahlungsAuftragList = zahlungsAuftragList;
    this.datentraegerArt = datentraegerArt;
  }
}
