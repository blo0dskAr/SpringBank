package at.blo0dy.SpringBank.model.konto.zahlungsAuftrag;


import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.zv.Datentraeger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @FutureOrPresent
  @NotNull
  private LocalDate auftragsDatum;
  private LocalDateTime datAnlage;
  private LocalDateTime datAend;

  @DecimalMin(value = "0.01", message = "Bitte einen Betrag zw. 0,01 und 50.000,00 wählen.")
  @DecimalMax(value = "50000", message = "Bitte einen Betrag zw. 0,01 und 50.000,00 wählen.")
  @Digits(integer = 5, fraction = 2, message = "Bitte einen Betrag zw. 0,01 und 50.000,00 wählen. maximal 2 Nachkommastellen")
  @NotNull(message = "Bitte einen Betrag zw. 0,01 und 50.000,00 wählen.")
  @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,###,###,###.##" )
  private BigDecimal betrag;

  @ManyToOne
  @JoinColumn(name = "konto_id")
  private Konto konto;

  @NotBlank
  @Column(name = "kontonummer")
  private String kontonummer;

  @Enumerated(EnumType.STRING)
  @Column(name = "auftrags_status")
  private ZahlungAuftragStatusEnum auftragsStatus;

  @Enumerated(EnumType.STRING)
  @Column(name = "auftrags_art")
  private ZahlungAuftragArtEnum auftragsArt;

  @Column(name = "empfaenger_konto")
  private String empfaengerKonto;

  @Column(name = "senderKonto")
  private String senderKonto;

  @ManyToOne
  @JoinColumn(name = "datentraeger_id")
  private Datentraeger datentraeger;


  public ZahlungsAuftrag(@FutureOrPresent @NotNull LocalDate auftragsDatum, LocalDateTime datAnlage, LocalDateTime datAend, @DecimalMin(value = "0.01", message = "Bitte einen Betrag zw. 0,01 und 50.000,00 wählen.") @DecimalMax(value = "50000", message = "Bitte einen Betrag zw. 0,01 und 50.000,00 wählen.") @Digits(integer = 5, fraction = 2, message = "Bitte einen Betrag zw. 0,01 und 50.000,00 wählen. maximal 2 Nachkommastellen") @NotNull(message = "Bitte einen Betrag zw. 0,01 und 50.000,00 wählen.") BigDecimal betrag, Konto konto, @NotBlank String kontonummer, ZahlungAuftragStatusEnum auftragsStatus, ZahlungAuftragArtEnum auftragsArt, String empfaengerKonto, String senderKonto) {
    this.auftragsDatum = auftragsDatum;
    this.datAnlage = datAnlage;
    this.datAend = datAend;
    this.betrag = betrag;
    this.konto = konto;
    this.kontonummer = kontonummer;
    this.auftragsStatus = auftragsStatus;
    this.auftragsArt = auftragsArt;
    this.empfaengerKonto = empfaengerKonto;
    this.senderKonto = senderKonto;
  }
}
