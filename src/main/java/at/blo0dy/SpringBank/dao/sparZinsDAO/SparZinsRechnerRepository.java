package at.blo0dy.SpringBank.dao.sparZinsDAO;

import at.blo0dy.SpringBank.model.produkt.sparen.AdvancedSparZinsRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.sparen.AdvancedSparZinsRechnerVorlage;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerVorlage;

import java.util.List;


public interface SparZinsRechnerRepository {


  SparZinsRechnerErgebnis getSparZinsRechnerEregebnis(SparZinsRechnerVorlage sparZinsRechnerVorlage) ;

  List<AdvancedSparZinsRechnerErgebnis> getAdvancedSparZinsRechnerErgebnis(AdvancedSparZinsRechnerVorlage sparZinsRechnerVorlage) ;
}
