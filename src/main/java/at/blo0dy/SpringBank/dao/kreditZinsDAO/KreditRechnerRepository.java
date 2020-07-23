package at.blo0dy.SpringBank.dao.kreditZinsDAO;

import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerVorlage;


public interface KreditRechnerRepository {

//  BigDecimal getZinssatz();

  KreditRechnerErgebnis getKreditRechnerErgebnis(KreditRechnerVorlage kreditRechnerVorlage) ;


}
