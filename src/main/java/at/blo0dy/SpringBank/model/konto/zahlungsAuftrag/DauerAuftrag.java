package at.blo0dy.SpringBank.model.konto.zahlungsAuftrag;

import at.blo0dy.SpringBank.model.enums.DauerAuftragStatusEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "dauerauftrag")
@AllArgsConstructor
@NoArgsConstructor
public class DauerAuftrag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotNull(message = "Zahl zwischen 1 und 31 angeben")
  @Min(value =  1, message = "Zahl zwischen 1 und 31 angeben")
  @Max(value = 31, message = "Zahl zwischen 1 und 31 angeben")
  private Integer tagImMonat;
  private LocalDateTime datAnlage;
  private LocalDateTime datAend;

  @DecimalMin(value = "1.00", message = "Bitte einen Betrag zw. 1,00 und 5.000,00 wählen.")
  @DecimalMax(value = "5000", message = "Bitte einen Betrag zw. 1,00 und 5.000,00 wählen.")
  @Digits(integer = 4, fraction = 2, message = "Bitte einen Betrag zw. 1,00 und 5.000,00 wählen. maximal 2 Nachkommastellen")
  @NotNull(message = "Bitte einen Betrag zw. 1,00 und 5.000,00 wählen.")
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
  private DauerAuftragStatusEnum auftragsStatus;

  @Enumerated(EnumType.STRING)
  @Column(name = "auftrags_art")
  private ZahlungAuftragArtEnum auftragsArt;

}
