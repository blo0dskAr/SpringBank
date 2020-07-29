package at.blo0dy.SpringBank.service.konto.sparen;


import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.produkt.sparen.AdvancedSparZinsRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.sparen.AdvancedSparZinsRechnerVorlage;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerVorlage;

import java.util.List;


public interface SparService {

//  public List<SparKonto> findAll();

  public SparKonto findById(Long theId);

  public void save(SparKonto sparKonto);

//  public void deleteById(Long theId);


  List<SparKonto> findSparKontoByKundennummer(String kundennummer);

  List<String> findKontoNummerOffenerSparKontenByKundennummer(String kundennummer);

  SparKonto findSparKontoByKontonummerAndKundennummer(String kontonummer, String kundennummer);

  SparKonto findByKontonummer(String kontonummer);


  // Zinsenrechner
  SparZinsRechnerErgebnis getSparZinsRechnerEregebnis(SparZinsRechnerVorlage sparZinsRechnerVorlage);

  List<AdvancedSparZinsRechnerErgebnis> getAdvancedSparZinsRechnerErgebnis(AdvancedSparZinsRechnerVorlage sparZinsRechnerVorlage);

  int countAktiveKontenByKundeId(Long kundeId);

}
