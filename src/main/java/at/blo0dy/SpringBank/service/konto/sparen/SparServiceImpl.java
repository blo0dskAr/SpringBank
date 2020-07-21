package at.blo0dy.SpringBank.service.konto.sparen;

import at.blo0dy.SpringBank.dao.konto.sparen.SparKontoRepository;
import at.blo0dy.SpringBank.dao.sparZinsDAO.SparZinsRechnerRepository;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
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
  }

  @Override
  @Transactional
  public int countAktiveKontenByKundeId(Long kundeId) {
    return sparKontoRepository.countAktiveKontenByKundeId(kundeId);
  }

  @Override
  @Transactional
  public SparKonto findSparKontoByKontonummerAndKundennummer(String kontonummer, String kundennummer) {
    return sparKontoRepository.findSparKontoByKontonummerAndKundennummer(kontonummer, kundennummer);
  }

  @Override
  @Transactional
  public SparKonto findByKontonummer(String kontonummer) {
    return sparKontoRepository.findByKontonummer(Long.valueOf(kontonummer));
  }

  @Override
  @Transactional
  public List<SparKonto> findSparKontoByKundennummer(String kundennummer) {
    return sparKontoRepository.findSparKontoByKundennummer(kundennummer);
  }

  @Override
  @Transactional
  public List<String> findKontoNummerOffenerSparKontenByKundennummer(String kundennummer) {
    return sparKontoRepository.findKontoNummerOffenerSparKontenByKundennummer(kundennummer);
  }


}
