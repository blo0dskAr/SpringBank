package at.blo0dy.SpringBank.dao.sparZinsDAO;

import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerVorlage;
import at.blo0dy.SpringBank.model.produkt.sparen.SparenUtility;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Component
public class SparZinsRechnerRepositoryImpl implements SparZinsRechnerRepository{

  @Override
  public double getZinssatz() {
    return SparenUtility.getZinssatz();
  }

  @Override
  public SparZinsRechnerErgebnis getSparZinsRechnerEregebnis(LocalDate datum, double betrag) {
    return SparenUtility.getZinsenBisJahresEnde(datum, betrag);
  }

  @Override
  public SparZinsRechnerErgebnis getSparZinsRechnerEregebnis(SparZinsRechnerVorlage sparZinsRechnerVorlage) {
    return SparenUtility.getZinsenBisJahresEnde(sparZinsRechnerVorlage);
  }


}
