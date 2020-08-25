package at.blo0dy.SpringBank.service.konto.giro;

import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;

import java.util.List;


public interface GiroService {

//  List<GiroKonto> findAll();

  GiroKonto findById(Long theId);

  void save(GiroKonto giroKonto);

//  void deleteById(Long theId);

  List<GiroKonto> findGiroKontenByKundennummer(String kundennummer);

  List<String> findKontoNummerOffenerGiroKontenByKundennummer(String kundennummer);

  GiroKonto findGiroKontoByKontonummerAndKundennummer(String kontonummer, String kundennummer);

  GiroKonto findByKontonummer(String kontonummer);

  int countAktiveKontenByKundeId(Long id);
}
