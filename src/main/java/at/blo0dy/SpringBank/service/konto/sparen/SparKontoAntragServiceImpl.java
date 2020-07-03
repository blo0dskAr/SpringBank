package at.blo0dy.SpringBank.service.konto.sparen;

import at.blo0dy.SpringBank.dao.konto.sparen.SparKontoAntragRepository;
import at.blo0dy.SpringBank.dao.konto.sparen.SparKontoRepository;
import at.blo0dy.SpringBank.dao.sparZinsDAO.SparZinsRechnerRepository;
import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.produkt.sparen.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class SparKontoAntragServiceImpl implements SparKontoAntragService {

  SparKontoAntragRepository sparKontoAntragRepository;

  @Autowired
  public SparKontoAntragServiceImpl(SparKontoAntragRepository sparKontoAntragRepository) {
    this.sparKontoAntragRepository = sparKontoAntragRepository;
  }

  @Override
  @Transactional
  public List<SparKontoAntrag> findAll() {
    // ich schätz das werd ich so modifizieren müssen, dass er nur seine eigene ID's findet?
    return sparKontoAntragRepository.findAll();
  }

  @Override
  @Transactional
  public SparKontoAntrag findById(Long theId) {
    Optional<SparKontoAntrag> result = sparKontoAntragRepository.findById(theId);

    SparKontoAntrag sparKontoAntrag;

    sparKontoAntrag = result.get();

    return sparKontoAntrag ;
  }

  @Override
  @Transactional
  public void save(SparKontoAntrag sparKontoAntrag) {
    sparKontoAntragRepository.save(sparKontoAntrag);
  }

  @Override
  @Transactional
  public void deleteById(Long theId) {
    sparKontoAntragRepository.deleteById(theId);
  }

  @Override
  @Transactional
  public long count() {
    return sparKontoAntragRepository.count();
  }

  @Override
  public long countByStatus(String statusEnum) {
    return sparKontoAntragRepository.countByStatus(statusEnum);
  }

  public List<SparKontoAntrag> findByStatus(String statusEnum) {
    List<SparKontoAntrag> sparKontoAntragListe = sparKontoAntragRepository.findByStatus(statusEnum) ;
    return  sparKontoAntragListe;
  }

}
