package at.blo0dy.SpringBank.dao.sparZinsDAO;

import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerVorlage;
import at.blo0dy.SpringBank.model.produkt.sparen.SparenUtility;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


public interface SparZinsRechnerRepository {

  public double getZinssatz();

  SparZinsRechnerErgebnis getSparZinsRechnerEregebnis(LocalDate datum, double betrag) ;

  SparZinsRechnerErgebnis getSparZinsRechnerEregebnis(SparZinsRechnerVorlage sparZinsRechnerVorlage) ;


}
