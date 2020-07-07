package at.blo0dy.SpringBank.service.konto.giro;

import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;

import java.util.List;


public interface GiroKontoAntragService {

  List<GiroKontoAntrag> findAll();

  GiroKontoAntrag findById(Long theId);

  void save(GiroKontoAntrag giroKontoAntrag);

  void deleteById(Long theId);

  long count();

  long countByStatus(String statusEnum);

  List<GiroKontoAntrag> findByStatus(String statusEnum);

  List<GiroKontoAntrag> findGiroAntraegeByKundennummer(String kundennummer);



}
