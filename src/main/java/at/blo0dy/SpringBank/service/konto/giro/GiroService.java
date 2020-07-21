package at.blo0dy.SpringBank.service.konto.giro;

import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;

import java.util.List;


public interface GiroService {

  // standard jpa crud operations:
  public List<GiroKonto> findAll();

  public GiroKonto findById(Long theId);

  public void save(GiroKonto giroKonto);

  public void deleteById(Long theId);

  List<GiroKonto> findGiroKontenByKundennummer(String kundennummer);

  List<String> findKontoNummerOffenerGiroKontenByKundennummer(String kundennummer);

  GiroKonto findGiroKontoByKontonummerAndKundennummer(String kontonummer, String kundennummer);

  GiroKonto findByKontonummer(Long kontonummer);

  int countAktiveKontenByKundeId(Long id);
}
