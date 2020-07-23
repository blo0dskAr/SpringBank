package at.blo0dy.SpringBank.model.produkt.kredit;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;


public class KreditUtility {

  // TODO: den vergeb ich momentan an 2 stellen, da gibts was zu refactoren. nur noch aus der vorlage nehmen - und vorlage nimmts im optimalfall aus der DB
//  private static final BigDecimal zinssatz = BigDecimal.valueOf(8.00) ;


    // TODO: den vergeb ich momentan an 2 stellen, da gibts was zu refactoren. nur noch aus der vorlage nehmen - und vorlage nimmts im optimalfall aus der DB
//  public static BigDecimal getZinssatz() {
//    return zinssatz;
//  } ;


  public static KreditRechnerErgebnis getKreditRechnerErgebnis(KreditRechnerVorlage kreditRechnerVorlage) {

    // AnfangsBetrag mal rausschreiben
    BigDecimal kreditBetrag = kreditRechnerVorlage.getKreditBetrag();
    BigInteger laufzeit = kreditRechnerVorlage.getLaufzeit();
    BigDecimal zinssatz = kreditRechnerVorlage.getZinssatz();

    // q = p/12/100+1 (mal auf 20 nachkommastellen genau (auf 2 gerundet gibts deutliche differenzen)
    BigDecimal q = zinssatz.divide(BigDecimal.valueOf(12), 20, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(100)).add(BigDecimal.ONE);
    BigDecimal aequiZinssatz = BigDecimal.ONE.divide(q,20,RoundingMode.HALF_UP);

    BigDecimal monatlicheRate = kreditBetrag.multiply((aequiZinssatz.subtract(BigDecimal.ONE))).divide(aequiZinssatz.multiply(aequiZinssatz.pow(laufzeit.intValue()).subtract(BigDecimal.ONE)),2,RoundingMode.HALF_UP);

    BigDecimal gesamtBelastung = monatlicheRate.multiply(BigDecimal.valueOf(laufzeit.intValue()));

    BigDecimal zinsAnteil = gesamtBelastung.subtract(kreditBetrag);

    return new KreditRechnerErgebnis(kreditBetrag, zinsAnteil,gesamtBelastung,monatlicheRate);
  }
}
