package at.blo0dy.SpringBank.service.konto.kredit;

import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerVorlage;

import java.math.BigDecimal;
import java.util.List;

public interface KreditService {

  // standard jpa crud operations:
  public List<KreditKonto> findAll();

  public KreditKonto findById(Long theId);

  public void save(KreditKonto kreditKonto);

  public void deleteById(Long theId);

//  BigDecimal getZinssatz();

  KreditRechnerErgebnis getKreditRechnerErgebnis(KreditRechnerVorlage kreditRechnerVorlage);

  List<KreditKonto> findKreditKontenByKundennummer(String kundennummer);

  List<String> findKontoNummerOffenerKreditKontenByKundennummer(String kundennummer);

  KreditKonto findKreditKontoByKontonummerAndKundennummer(String kontonummer, String kundennummer);

  KreditKonto findByKontonummer(Long kontonummer);


  int countAktiveKontenByKundeId(Long id);
}
