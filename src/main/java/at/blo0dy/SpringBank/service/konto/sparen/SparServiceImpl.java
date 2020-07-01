package at.blo0dy.SpringBank.service.konto.sparen;

import at.blo0dy.SpringBank.dao.konto.sparen.SparKontoRepository;
import at.blo0dy.SpringBank.dao.sparZinsDAO.SparZinsRechnerRepository;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.produkt.sparen.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class SparServiceImpl implements SparService {

  SparZinsRechnerRepository sparZinsRechnerRepository;
  SparKontoRepository sparKontoRepository;

  @Autowired
  public SparServiceImpl(SparZinsRechnerRepository sparZinsRechnerRepository, SparKontoRepository sparKontoRepository) {
    this.sparZinsRechnerRepository = sparZinsRechnerRepository;
    this.sparKontoRepository = sparKontoRepository;
  }

  @Override
  @Transactional
  public List<SparKonto> findAll() {
    // ich schätz das werd ich so modifizieren müssen, dass er nur seine eigene ID's findet?
    return sparKontoRepository.findAll();
  }

  @Override
  @Transactional
  public SparKonto findById(Long theId) {
    Optional<SparKonto> result = sparKontoRepository.findById(theId);

    SparKonto sparKonto;

    sparKonto = result.get();

    return sparKonto ;
  }

  @Override
  @Transactional
  public void save(SparKonto sparKonto) {
    sparKontoRepository.save(sparKonto);
  }

  @Override
  @Transactional
  public void deleteById(Long theId) {
    sparKontoRepository.deleteById(theId);
  }

  @Override
  @Transactional
  public double getZinssatz() {
    return SparenUtility.getZinssatz();
  }

//  @Override
//  public SparZinsRechnerErgebnis getSparZinsRechnerEregebnis(LocalDate datum, double betrag) {
//    return sparZinsRechnerRepository.getSparZinsRechnerEregebnis(datum, betrag);
//  }

  @Override
  public SparZinsRechnerErgebnis getSparZinsRechnerEregebnis(SparZinsRechnerVorlage sparZinsRechnerVorlage) {
    return sparZinsRechnerRepository.getSparZinsRechnerEregebnis(sparZinsRechnerVorlage);
  }

  @Override
  public List<AdvancedSparZinsRechnerErgebnis> getAdvancedSparZinsRechnerErgebnis(AdvancedSparZinsRechnerVorlage sparZinsRechnerVorlage) {
    return sparZinsRechnerRepository.getAdvancedSparZinsRechnerErgebnis(sparZinsRechnerVorlage);
  };





}
