package at.blo0dy.SpringBank.model.produkt.sparen;



import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;


public class SparenUtility {

  private static final Double zinssatz = 5.0 ;

  public static SparZinsRechnerErgebnis getZinsenBisJahresEnde(LocalDate datum, double betrag) {

    // hol Tage bis Jahresende
    int tage = getTageBisMonatsEnde(datum) + (getTageBisJahresEnde(datum)*30);

    // berechnet Zinsen & kest bis JahresEnde als SparZinsRechnerErgebnis
    BigDecimal zinsen = BigDecimal.valueOf(betrag).multiply(BigDecimal.valueOf(zinssatz)).multiply(BigDecimal.valueOf(tage)).divide(BigDecimal.valueOf(36000),2 , RoundingMode.HALF_UP);
    BigDecimal kest = zinsen.multiply( BigDecimal.valueOf(0.25)).setScale(2, RoundingMode.HALF_UP);

    return new SparZinsRechnerErgebnis(betrag, zinsen.doubleValue(), kest.doubleValue()) ;
  }

  public static SparZinsRechnerErgebnis getZinsenBisJahresEnde(SparZinsRechnerVorlage sparZinsRechnerVorlage) {

    // hol Tage bis Jahresende
    int tage = getTageBisMonatsEnde(sparZinsRechnerVorlage.getDatum()) + (getTageBisJahresEnde(sparZinsRechnerVorlage.getDatum())*30);

    // berechnet Zinsen & kest bis JahresEnde als SparZinsRechnerErgebnis
    BigDecimal zinsen = BigDecimal.valueOf(sparZinsRechnerVorlage.getBetrag())
                                  .multiply(BigDecimal.valueOf(sparZinsRechnerVorlage.getZinssatz()))
                                  .multiply(BigDecimal.valueOf(tage))
                                  .divide(BigDecimal.valueOf(36000),2 , RoundingMode.HALF_UP);

    BigDecimal kest = zinsen.multiply( BigDecimal.valueOf(0.25)).setScale(2, RoundingMode.HALF_UP);

    return new SparZinsRechnerErgebnis(sparZinsRechnerVorlage.getBetrag(), zinsen.doubleValue(), kest.doubleValue()) ;

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


  public static double getZinssatz() {
    return zinssatz;
  } ;




}
