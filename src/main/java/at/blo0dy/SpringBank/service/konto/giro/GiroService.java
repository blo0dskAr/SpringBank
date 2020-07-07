package at.blo0dy.SpringBank.service.konto.giro;

import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.produkt.sparen.AdvancedSparZinsRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.sparen.AdvancedSparZinsRechnerVorlage;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerVorlage;

import java.util.List;


public interface GiroService {

  // standard jpa crud operations:
  public List<GiroKonto> findAll();

  public GiroKonto findById(Long theId);

  public void save(GiroKonto giroKonto);

  public void deleteById(Long theId);

  List<GiroKonto> findGiroKontenByKundennummer(String kundennummer);

}
