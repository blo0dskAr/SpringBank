package at.blo0dy.SpringBank.model.konto.kontoBuchung;


import at.blo0dy.SpringBank.model.enums.BuchungsArtEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "konto_buchung")
@AllArgsConstructor
@NoArgsConstructor
public class KontoBuchung {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  private LocalDate buchungsDatum;
  private LocalDateTime datAnlage;
  private LocalDateTime datAend;

  private BigDecimal buchungsBetrag;
  private BigDecimal saldoNachBuchung;

  @Column(name = "buchungs_text")
  private String buchungsText;

  @ManyToOne()
  @JoinColumn(name = "konto_id")
  private Konto konto;

  @Enumerated
  @Column(name = "buchungs_art")
  private BuchungsArtEnum buchungsArt;


}
