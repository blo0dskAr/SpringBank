package at.blo0dy.SpringBank.dao.sparZinsDAO;

import at.blo0dy.SpringBank.model.produkt.sparen.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SparZinsRechnerRepositoryImpl implements SparZinsRechnerRepository{

  @Override
  public SparZinsRechnerErgebnis getSparZinsRechnerEregebnis(SparZinsRechnerVorlage sparZinsRechnerVorlage) {
    return SparenUtility.getZinsenBisJahresEnde(sparZinsRechnerVorlage);
  }

  @Override
  public List<AdvancedSparZinsRechnerErgebnis> getAdvancedSparZinsRechnerErgebnis(AdvancedSparZinsRechnerVorlage sparZinsRechnerVorlage) {
    return SparenUtility.getSparenZinsBerechnung(sparZinsRechnerVorlage);
  }


}
