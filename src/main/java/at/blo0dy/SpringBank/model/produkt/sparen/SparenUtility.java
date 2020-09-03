package at.blo0dy.SpringBank.model.produkt.sparen;



import at.blo0dy.SpringBank.dao.produkt.zinssatz.ZinssatzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SparenUtility {

  public static final int ANZAHL_MONATE_IM_JAHR = 12;
  private ZinssatzRepository zinssatzRepository;

  @Autowired
  public SparenUtility(ZinssatzRepository zinssatzRepository) {
    this.zinssatzRepository = zinssatzRepository;
  }

  public static SparZinsRechnerErgebnis getZinsenBisJahresEnde(SparZinsRechnerVorlage sparZinsRechnerVorlage) {

    BigDecimal rechnungsBetrag = sparZinsRechnerVorlage.getBetrag() ;
    // hol Tage bis Jahresende
    int tage = getTageBisMonatsEnde(sparZinsRechnerVorlage.getDatum()) + (getVolleMonateBisJahresEnde(sparZinsRechnerVorlage.getDatum())*30);
    // berechnet Zinsen & kest bis JahresEnde als SparZinsRechnerErgebnis
    BigDecimal zinsen = rechnungsBetrag.multiply(sparZinsRechnerVorlage.getZinssatz())
                                  .multiply(BigDecimal.valueOf(tage))
                                  .divide(BigDecimal.valueOf(360),2 , RoundingMode.HALF_UP);
    // rechnet die KESt aus den Zinsen
    BigDecimal kest = zinsen.multiply( BigDecimal.valueOf(0.25)).setScale(2, RoundingMode.HALF_UP);
    return new SparZinsRechnerErgebnis(rechnungsBetrag, zinsen, kest, rechnungsBetrag.add(zinsen),  rechnungsBetrag.add(zinsen).subtract(kest)) ;
  }

  /**
   * Monate zählen als 30 Tage fuer Zinsberechnung gemaess BWG 30/360 berechnung.
   * @param datum
   * @return
   */
  public static int getTageBisMonatsEnde(LocalDate datum) {
    // TODO: 28 februar ..
    if (isEndOfMonth(datum)) {
      return 1;
    } else {
      return 30 - datum.getDayOfMonth()+1;
    }
  }

  private static boolean isEndOfMonth(LocalDate datum) {
    return datum.getDayOfMonth()==30 || datum.getDayOfMonth()==31;
  }


  public static int  getVolleMonateBisJahresEnde(LocalDate datum) {
    return ANZAHL_MONATE_IM_JAHR - datum.getMonthValue();
    }

  public static List<AdvancedSparZinsRechnerErgebnis> getSparenZinsBerechnung (AdvancedSparZinsRechnerVorlage sparZinsRechnerVorlage) {
    List<AdvancedSparZinsRechnerErgebnis> ergebnis = new ArrayList<AdvancedSparZinsRechnerErgebnis>();
    AdvancedSparZinsRechnerVorlage neueVorlage = null;
    for (int i = 1; i <= sparZinsRechnerVorlage.getAnlagedauer().intValue() ; i++ ) {
      if (neueVorlage==null) {
        neueVorlage = sparZinsRechnerVorlage;
        neueVorlage.setBetrag(neueVorlage.getInitialBetrag());
      }
      AdvancedSparZinsRechnerErgebnis rechnungsErgebnis = getSparenZinsBerechnungfuerEinJahr(neueVorlage);
      ergebnis.add(rechnungsErgebnis);
       neueVorlage.setBetrag(rechnungsErgebnis.getEndKapital()) ;
    }
     return ergebnis;
  }

  public static AdvancedSparZinsRechnerErgebnis getSparenZinsBerechnungfuerEinJahr(AdvancedSparZinsRechnerVorlage sparZinsRechnerVorlage) {

    // AnfangsBetrag mal rausschreiben
    BigDecimal initialBetrag = sparZinsRechnerVorlage.getBetrag();

    // Normale Zinsberechnung für ein Jahr mit dem AnfangsBetrag
    BigDecimal kapitalZinsen = initialBetrag
            .multiply(sparZinsRechnerVorlage.getZinssatz())
            .setScale(2,RoundingMode.HALF_UP);

    // Rechnung für monatliche, nachschüssige Einzahlung aber Jährlicher Verzinsung: .. mathe is zlang her ...
    // K(n) = R * (12 + 5,5 * p)
    BigDecimal k = sparZinsRechnerVorlage.getMonatlicheEinzahlung()
                        .multiply(BigDecimal.valueOf(ANZAHL_MONATE_IM_JAHR)
                                .add(BigDecimal.valueOf(5.5)
                                        .multiply(sparZinsRechnerVorlage.getZinssatz()
                        .setScale(2,RoundingMode.HALF_UP))));

    // summe der monatlichen Einzahlungen
    BigDecimal summeMonatlicheEinzahlungen = sparZinsRechnerVorlage.getMonatlicheEinzahlung().multiply(BigDecimal.valueOf(ANZAHL_MONATE_IM_JAHR));

    // Zinsanteil durch monatliche rausrechnen
    BigDecimal zinsAnteil = k.subtract(summeMonatlicheEinzahlungen);

    // zinsen aus kapital und frischen einzahlungen addieren
    BigDecimal gesamtZinsen = zinsAnteil.add(kapitalZinsen);

    // KESt-Anteil rausrechnen
     BigDecimal kestAnteil = gesamtZinsen.multiply( BigDecimal.valueOf(0.25)).setScale(2, RoundingMode.HALF_UP);

    // endBetrag nach KESt (neuer anfangs Betrag)
    BigDecimal endBetrag = initialBetrag.add(gesamtZinsen).subtract(kestAnteil).add(summeMonatlicheEinzahlungen);

    return new AdvancedSparZinsRechnerErgebnis(initialBetrag, summeMonatlicheEinzahlungen,gesamtZinsen,kestAnteil,endBetrag);
  }
}
