package at.blo0dy.SpringBank.service.konto.kredit;

import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;

import java.util.List;


public interface KreditKontoAntragService {

  List<KreditKontoAntrag> findAll();

  KreditKontoAntrag findById(Long theId);

  void save(KreditKontoAntrag kreditKontoAntrag);

  void deleteById(Long theId);

  KreditKontoAntrag getOne(Long aLong);

  long count();

  long countByStatus(String statusEnum);

  List<KreditKontoAntrag> findByStatus(String statusEnum);

  boolean compareBasicKreditData(KreditKontoAntrag antrag1, KreditKontoAntrag antrag2);

  void setKreditAntragAbgelehntWeilNeuBerechnetById(Long kreditKontoAntragId);

  List<KreditKontoAntrag> findKreditAntraegeByKundennummer(String kundennummer);

}
