package at.blo0dy.SpringBank.model.produkt.sparen;



import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SparenUtility {

  // TODO: den vergeb ich momentan an 2 stellen, da gibts was zu refactoren. nur noch aus der vorlage nehmen - und vorlage nimmts im optimalfall aus der DB
  private static final Double zinssatz = 6.00 ;

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
    int tage = getTageBisMonatsEnde(sparZinsRechnerVorlage.getDatum()) + (getTageBisJahresEnde(sparZinsRechnerVorlage.getDatum())*30);

    // berechnet Zinsen & kest bis JahresEnde als SparZinsRechnerErgebnis
    BigDecimal zinsen = BigDecimal.valueOf(sparZinsRechnerVorlage.getBetrag())
                                  .multiply(BigDecimal.valueOf(sparZinsRechnerVorlage.getZinssatz()))
                                  .multiply(BigDecimal.valueOf(tage))
                                  .divide(BigDecimal.valueOf(36000),2 , RoundingMode.HALF_UP);

    // rechnet die KESt aus den Zinsen
    BigDecimal kest = zinsen.multiply( BigDecimal.valueOf(0.25)).setScale(2, RoundingMode.HALF_UP);

    return new SparZinsRechnerErgebnis(sparZinsRechnerVorlage.getBetrag(), zinsen.doubleValue(), kest.doubleValue(),
                                        sparZinsRechnerVorlage.getBetrag()+zinsen.doubleValue(),
                                        sparZinsRechnerVorlage.getBetrag()+zinsen.doubleValue()-kest.doubleValue()) ;
  }

  public static int getTageBisMonatsEnde(LocalDate datum) {

    if (datum.getDayOfMonth()==30 || datum.getDayOfMonth()==31) {
      return 1;
    } else {
      return 30 - datum.getDayOfMonth()+1;
    }
  }


  public static int  getTageBisJahresEnde(LocalDate datum) {

    int tageBisJahresEnde;

    if (datum.getMonthValue()==12) {
      return 0;
    } else {
      return 12 - datum.getMonthValue();
    }
  }

    // TODO: den vergeb ich momentan an 2 stellen, da gibts was zu refactoren. nur noch aus der vorlage nehmen - und vorlage nimmts im optimalfall aus der DB
  public static double getZinssatz() {
    return zinssatz;
  } ;

  public static List<AdvancedSparZinsRechnerErgebnis> getSparenZinsBerechnung (AdvancedSparZinsRechnerVorlage sparZinsRechnerVorlage) {

    List<AdvancedSparZinsRechnerErgebnis> ergebnis = new ArrayList<AdvancedSparZinsRechnerErgebnis>();

    AdvancedSparZinsRechnerVorlage neueVorlage = null;

    for (int i = 1; i <= sparZinsRechnerVorlage.getAnlagedauer(); i++ ) {
      if (neueVorlage==null) {
        neueVorlage = sparZinsRechnerVorlage;
        neueVorlage.setBetrag(neueVorlage.getInitialBetrag());
      }
      AdvancedSparZinsRechnerErgebnis rechnungsErgebnis = getSparenZinsBerechnungfuerEinJahr(neueVorlage);
      ergebnis.add(rechnungsErgebnis);
       neueVorlage.setBetrag(rechnungsErgebnis.getEndKapital().doubleValue()) ;
    }
     return ergebnis;
  }

  public static AdvancedSparZinsRechnerErgebnis getSparenZinsBerechnungfuerEinJahr(AdvancedSparZinsRechnerVorlage sparZinsRechnerVorlage) {

    // AnfangsBetrag mal rausschreiben
    BigDecimal initialBetrag = BigDecimal.valueOf(sparZinsRechnerVorlage.getBetrag());

    // Normale Zinsberechnung für ein Jahr mit dem AnfangsBetrag
    BigDecimal kapitalZinsen = BigDecimal.valueOf(sparZinsRechnerVorlage.getBetrag())
                                          .multiply(BigDecimal.valueOf(sparZinsRechnerVorlage.getZinssatz()))
                                          .multiply(BigDecimal.valueOf(360))
                                          .divide(BigDecimal.valueOf(36000),2 , RoundingMode.HALF_UP);

    // Rechnung für monatliche Einzahlung aber Jährlicher Verzinsung: .. mathe is zlang her ...
    // K(n) = R * (12 + 5,5 * p)
    BigDecimal k = BigDecimal.valueOf(sparZinsRechnerVorlage.getMonatlicheEinzahlung()).multiply(BigDecimal.valueOf(12).add(BigDecimal.valueOf(5.5).multiply(BigDecimal.valueOf(sparZinsRechnerVorlage.getZinssatz()/100)))).setScale(2,RoundingMode.HALF_UP);


   /* // Nachschüssige Rentenrechnung
   // mah .. rentenrechnung is da a blödsinn, weil nur jährlicher und ned monatlicher zinseszins:
    // K(n) = R * q^n-1/q-1
    BigDecimal p = BigDecimal.valueOf(sparZinsRechnerVorlage.getZinssatz()/12);
    BigDecimal q = p.divide(BigDecimal.valueOf(100)).add(BigDecimal.ONE);

    BigDecimal k = BigDecimal.valueOf(sparZinsRechnerVorlage.getMonatlicheEinzahlung())
                            .multiply((q.pow(12).subtract(BigDecimal.ONE))
                            .divide(q.subtract(BigDecimal.ONE))).setScale(2, RoundingMode.HALF_UP);*/

    // summe der monatlichen Einzahlungen
    BigDecimal summeMonatlicheEinzahlungen = BigDecimal.valueOf(sparZinsRechnerVorlage.getMonatlicheEinzahlung()).multiply(BigDecimal.valueOf(12));

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
