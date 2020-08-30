package at.blo0dy.SpringBank.model.produkt.sparen;



import at.blo0dy.SpringBank.dao.produkt.zinssatz.ZinssatzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// TODO eigentlich soll ich dieses ding als service injecten oder ? hat mit model nix zu tun. icxh versuchs mal, mal guggen wo ich ddas ueberall verwend, dass dort der neue service injected wird
@Service
public class SparenUtility {

  public static final int ANZAHL_MONATE_IM_JAHR = 12;
  private ZinssatzRepository zinssatzRepository;

  @Autowired
  public SparenUtility(ZinssatzRepository zinssatzRepository) {
    this.zinssatzRepository = zinssatzRepository;
  }

  //  // TODO: den vergeb ich momentan an 2 stellen, da gibts was zu refactoren. nur noch aus der vorlage nehmen - und vorlage nimmts im optimalfall aus der DB
//  private static final Double zinssatz = 6.00 ;

//  public static SparZinsRechnerErgebnis getZinsenBisJahresEnde(LocalDate datum, double betrag) {
//
//    // hol Tage bis Jahresende
//    int tage = getTageBisMonatsEnde(datum) + (getTageBisJahresEnde(datum)*30);
//
//    // berechnet Zinsen & kest bis JahresEnde als SparZinsRechnerErgebnis
//    BigDecimal zinsen = BigDecimal.valueOf(betrag).multiply(BigDecimal.valueOf(zinssatz)).multiply(BigDecimal.valueOf(tage)).divide(BigDecimal.valueOf(36000),2 , RoundingMode.HALF_UP);
//    BigDecimal kest = zinsen.multiply( BigDecimal.valueOf(0.25)).setScale(2, RoundingMode.HALF_UP);
//
//    return new SparZinsRechnerErgebnis(betrag, zinsen.doubleValue(), kest.doubleValue()) ;
//  }

  public static SparZinsRechnerErgebnis getZinsenBisJahresEnde(SparZinsRechnerVorlage sparZinsRechnerVorlage) {

    // hol Tage bis Jahresende
    int tage = getTageBisMonatsEnde(sparZinsRechnerVorlage.getDatum()) + (getVolleMonateBisJahresEnde(sparZinsRechnerVorlage.getDatum())*30);

    // berechnet Zinsen & kest bis JahresEnde als SparZinsRechnerErgebnis
    BigDecimal zinsen = sparZinsRechnerVorlage.getBetrag()
                                  .multiply(sparZinsRechnerVorlage.getZinssatz())
                                  .multiply(BigDecimal.valueOf(tage))
                                  .divide(BigDecimal.valueOf(360),2 , RoundingMode.HALF_UP);

    // rechnet die KESt aus den Zinsen
    BigDecimal kest = zinsen.multiply( BigDecimal.valueOf(0.25)).setScale(2, RoundingMode.HALF_UP);

    // holt mir (so als workaround momentan) den betrag als BigDecimal
    BigDecimal betrag = sparZinsRechnerVorlage.getBetrag() ;

    return new SparZinsRechnerErgebnis(betrag, zinsen, kest,
            betrag.add(zinsen),  betrag.add(zinsen).subtract(kest)) ;
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



    // Rechnung für monatliche Einzahlung aber Jährlicher Verzinsung: .. mathe is zlang her ...
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
