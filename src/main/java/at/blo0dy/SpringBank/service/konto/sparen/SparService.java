package at.blo0dy.SpringBank.service.konto.sparen;

import at.blo0dy.SpringBank.dao.konto.sparen.SparKontoRepository;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.model.produkt.sparen.AdvancedSparZinsRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.sparen.AdvancedSparZinsRechnerVorlage;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerVorlage;

import java.util.List;


public interface SparService {

  // standard jpa crud operations:
  public List<SparKonto> findAll();

  public SparKonto findById(Long theId);

  public void save(SparKonto sparKonto);

  public void deleteById(Long theId);

  // das liegt mir noch immer im magen
  double getZinssatz();

  List<SparKonto> findSparKontoByKundennummer(String kundennummer);

  List<String> findKontoNummerOffenerSparKontenByKundennummer(String kundennummer);


  // Zinsenrechner
  SparZinsRechnerErgebnis getSparZinsRechnerEregebnis(SparZinsRechnerVorlage sparZinsRechnerVorlage);

  List<AdvancedSparZinsRechnerErgebnis> getAdvancedSparZinsRechnerErgebnis(AdvancedSparZinsRechnerVorlage sparZinsRechnerVorlage);

  SparKonto findSparKontoByKontonummerAndKundennummer(String kontonummer, String kundennummer);



}
