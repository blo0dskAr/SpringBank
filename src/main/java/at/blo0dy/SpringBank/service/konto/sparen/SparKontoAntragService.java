package at.blo0dy.SpringBank.service.konto.sparen;

import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;

import java.util.List;


public interface SparKontoAntragService {

  List<SparKontoAntrag> findAll();

  SparKontoAntrag findById(Long theId);

  void save(SparKontoAntrag sparKontoAntrag);

  void deleteById(Long theId);

  long count();

  long countByStatus(String statusEnum);

  List<SparKontoAntrag> findByStatus(String statusEnum);

  List<SparKontoAntrag> findSparAntraegeByKundennummer(String kundennummer);

  SparKontoAntrag findSparAntragByAntragIdAndKundennummer(Long antragId, String authKundennummer);

}
