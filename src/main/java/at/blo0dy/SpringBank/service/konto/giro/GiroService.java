package at.blo0dy.SpringBank.service.konto.giro;

import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;

import java.util.List;


public interface GiroService {

  // standard jpa crud operations:
  public List<GiroKonto> findAll();

  public GiroKonto findById(Long theId);

  public void save(GiroKonto giroKonto);

  public void deleteById(Long theId);

  List<GiroKonto> findGiroKontenByKundennummer(String kundennummer);

}
