package at.blo0dy.SpringBank.model.konto;

import at.blo0dy.SpringBank.model.antrag.KontoAntrag;
import at.blo0dy.SpringBank.model.enums.KontoProduktEnum;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.kontoBuchung.KontoBuchung;
import at.blo0dy.SpringBank.model.konto.dauerauftrag.DauerAuftrag;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "konto")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
public class Konto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "eroeffnungs_datum")
  private LocalDateTime eroeffnungsDatum;

  @Column(name = "kontonummer")
  private String kontonummer;

  @ManyToOne
  @JoinColumn(name="kunde_id")
  private Kunde kunde ;

  @OneToOne
  private KontoAntrag kontoAntrag;

  @Column(name = "akt_saldo")
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  private BigDecimal aktSaldo;

  @Column(name = "konto_status")
  @Enumerated(EnumType.STRING)
  private KontoStatusEnum kontoStatus;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "konto")
  private List<KontoBuchung> kontoBuchungList;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "konto")
  private List<ZahlungsAuftrag> kontoZahlungsAuftragList;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "konto")
  private List<DauerAuftrag> dauerAuftragList;

  @Enumerated(value = EnumType.STRING)
  private KontoProduktEnum produkt;

  public Konto(LocalDateTime eroeffnungsDatum, String kontonummer, Kunde kunde, BigDecimal aktSaldo, KontoStatusEnum kontoStatus, KontoAntrag kontoAntrag, List<KontoBuchung> kontoBuchungList, KontoProduktEnum produkt) {
    this.eroeffnungsDatum = eroeffnungsDatum;
    this.kontonummer = kontonummer;
    this.kunde = kunde;
    this.aktSaldo = aktSaldo;
    this.kontoStatus = kontoStatus;
    this.kontoAntrag = kontoAntrag;
    this.kontoBuchungList = kontoBuchungList;
    this.produkt = produkt;
  }



}
