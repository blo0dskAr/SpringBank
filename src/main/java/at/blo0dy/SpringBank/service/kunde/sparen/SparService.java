package at.blo0dy.SpringBank.service.kunde.sparen;

import at.blo0dy.SpringBank.model.produkt.sparen.AdvancedSparZinsRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.sparen.AdvancedSparZinsRechnerVorlage;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerVorlage;

import java.time.LocalDate;
import java.util.List;

public interface SparService {

  double getZinssatz();

//  SparZinsRechnerErgebnis getSparZinsRechnerEregebnis(LocalDate datum, double betrag);

  SparZinsRechnerErgebnis getSparZinsRechnerEregebnis(SparZinsRechnerVorlage sparZinsRechnerVorlage);

  List<AdvancedSparZinsRechnerErgebnis> getAdvancedSparZinsRechnerErgebnis(AdvancedSparZinsRechnerVorlage sparZinsRechnerVorlage);



}
