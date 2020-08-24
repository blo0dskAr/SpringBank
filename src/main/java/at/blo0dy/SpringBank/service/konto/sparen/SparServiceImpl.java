package at.blo0dy.SpringBank.service.konto.sparen;

import at.blo0dy.SpringBank.dao.konto.sparen.SparKontoRepository;
import at.blo0dy.SpringBank.dao.sparZinsDAO.SparZinsRechnerRepository;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.produkt.sparen.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SparServiceImpl implements SparService {

  SparZinsRechnerRepository sparZinsRechnerRepository;
  SparKontoRepository sparKontoRepository;

  @Autowired
  public SparServiceImpl(SparZinsRechnerRepository sparZinsRechnerRepository, SparKontoRepository sparKontoRepository) {
    this.sparZinsRechnerRepository = sparZinsRechnerRepository;
    this.sparKontoRepository = sparKontoRepository;
  }

/*  @Override
  @Transactional(readOnly = true)
  public List<SparKonto> findAll() {
    return sparKontoRepository.findAll();
  }*/

  @Override
  @Transactional(readOnly = true)
  public SparKonto findById(Long theId) {

    log.debug("Sparkonto mit ID: " + theId + " wird gesucht.");
    Optional<SparKonto> result = sparKontoRepository.findById(theId);
    SparKonto sparKonto;
    sparKonto = result.get();

    log.debug("Sparkonto mit ID: " + theId + " wurde gefunden.");
    return sparKonto ;
  }

  @Override
//  @Transactional
  public void save(SparKonto sparKonto) {
    log.debug("Sparkonto wird gespeichert");

    SparKonto savedSparKonto = sparKontoRepository.save(sparKonto);

    log.debug("Sparkonto wurde erfolgreich gespeichert. ID: " + savedSparKonto.getId());
  }

/*  @Override
//  @Transactional
  public void deleteById(Long theId) {
    sparKontoRepository.deleteById(theId);
  }*/

/*  @Override
  public double getZinssatz() {
    return SparenUtility.getZinssatz();
  }*/



  @Override
  public SparZinsRechnerErgebnis getSparZinsRechnerEregebnis(SparZinsRechnerVorlage sparZinsRechnerVorlage) {
    log.debug("SparZinsRechner ergebnis wird ermittelt.");
    return sparZinsRechnerRepository.getSparZinsRechnerEregebnis(sparZinsRechnerVorlage);
  }

  @Override
  public List<AdvancedSparZinsRechnerErgebnis> getAdvancedSparZinsRechnerErgebnis(AdvancedSparZinsRechnerVorlage sparZinsRechnerVorlage) {
    log.debug("Advanced SparZinsRechner Ergebnis wird ermittelt.");
    return sparZinsRechnerRepository.getAdvancedSparZinsRechnerErgebnis(sparZinsRechnerVorlage);
  }

  @Override
  @Transactional(readOnly = true)
  public int countAktiveKontenByKundeId(Long kundeId) {
    log.debug("Anzahl aktiver Konten für KundeId: " + kundeId + " wird ermittelt.");
    return sparKontoRepository.countAktiveKontenByKundeId(kundeId);
  }

  @Override
  @Transactional(readOnly = true)
  public SparKonto findSparKontoByKontonummerAndKundennummer(String kontonummer, String kundennummer) {
    log.debug("Sparkonto für Kundennummer: " + kundennummer + " und Kontonummer: " + kontonummer + " wird gesucht.");
    return sparKontoRepository.findSparKontoByKontonummerAndKundennummer(kontonummer, kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public SparKonto findByKontonummer(String kontonummer) {
    log.debug("Konto für Kontonummer: " + kontonummer + " wird gesucht.");
    return sparKontoRepository.findByKontonummer(kontonummer);
  }

  @Override
  @Transactional(readOnly = true)
  public List<SparKonto> findSparKontoByKundennummer(String kundennummer) {
    log.debug("Sparkonten für Kundennummer: " + kundennummer + " werden gesucht.");
    return sparKontoRepository.findSparKontoByKundennummer(kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public List<String> findKontoNummerOffenerSparKontenByKundennummer(String kundennummer) {
    log.debug("Kontonummern offener SParkonten für Kundennummer: " + kundennummer + " werden ermittelt.");
    return sparKontoRepository.findKontoNummerOffenerSparKontenByKundennummer(kundennummer);
  }


}
