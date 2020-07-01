package at.blo0dy.SpringBank.service.konto.sparen;

import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;

import java.util.List;


public interface SparKontoAntragService {

  // standard jpa crud operations:
  public List<SparKontoAntrag> findAll();

  public SparKontoAntrag findById(Long theId);

  public void save(SparKontoAntrag sparKontoAntrag);

  public void deleteById(Long theId);

}
