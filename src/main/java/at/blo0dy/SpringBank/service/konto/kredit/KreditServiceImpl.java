package at.blo0dy.SpringBank.service.konto.kredit;

import at.blo0dy.SpringBank.dao.konto.kredit.KreditKontoRepository;
import at.blo0dy.SpringBank.dao.kreditZinsDAO.KreditRechnerRepository;
import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerVorlage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class KreditServiceImpl implements KreditService {

  KreditRechnerRepository kreditRechnerRepository;
  KreditKontoRepository kreditKontoRepository;

  @Autowired
  public KreditServiceImpl(KreditRechnerRepository kreditRechnerRepository, KreditKontoRepository kreditKontoRepository) {
    this.kreditRechnerRepository = kreditRechnerRepository;
    this.kreditKontoRepository = kreditKontoRepository;
  }


/*  @Override
  @Transactional(readOnly = true)
  public List<KreditKonto> findAll() {
    return kreditKontoRepository.findAll();
  }*/

  @Override
  @Transactional(readOnly = true)
  public KreditKonto findById(Long theId) {
    log.debug("KreditKonto mit ID: " + theId + " wird gesucht.");
    Optional<KreditKonto> result = kreditKontoRepository.findById(theId);
    KreditKonto kreditKonto;
    kreditKonto = result.get();

    log.debug("KreditKonto mit ID: " + theId + " wurde gefunden.");
    return kreditKonto ;
  }

  @Override
  public void save(KreditKonto kreditKonto) {
    log.debug("KreditKonto wird gespeichert.");

    KreditKonto savedKreditKonto = kreditKontoRepository.save(kreditKonto);

    log.debug("Kreditkonto erfolgreich gespeichert. ID: " + savedKreditKonto.getId()) ;
  }

/*  @Override
  public void deleteById(Long theId) {
    kreditKontoRepository.deleteById(theId);
  }*/

/*  @Override
  public BigDecimal getZinssatz() {
    return KreditUtility.getZinssatz();
  }*/


  @Override
  public KreditRechnerErgebnis getKreditRechnerErgebnis(KreditRechnerVorlage kreditRechnerVorlage) {
    log.debug("KreditRechnerErgebnis wird ermittelt");
    return kreditRechnerRepository.getKreditRechnerErgebnis(kreditRechnerVorlage);
  }

  @Override
  @Transactional(readOnly = true)
  public List<KreditKonto> findKreditKontenByKundennummer(String kundennummer) {
    log.debug("Kreditkonten für Kundennummer: " + kundennummer + " werden gesucht");
    return kreditKontoRepository.findKreditKontenByKundennummer(kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public List<String> findKontoNummerOffenerKreditKontenByKundennummer(String kundennummer) {
    log.debug("Kontonummern offener Kreditkonten für Kundennummer: " + kundennummer + " werden gesucht.");
    return kreditKontoRepository.findKontoNummerOffenerKreditKontenByKundennummer(kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public KreditKonto findKreditKontoByKontonummerAndKundennummer(String kontonummer, String kundennummer) {
    log.debug("KreditKonto mit Kontonummer: " + kontonummer + " und Kundennummer: " + kundennummer + " wird gesucht.");
    return kreditKontoRepository.findKreditKontoByKontonummerAndKundennummer(kontonummer, kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public KreditKonto findByKontonummer(Long kontonummer) {
    log.debug("Kreditkonto für Kontonummer: " + kontonummer + " wird gesucht.");
    return kreditKontoRepository.findByKontonummer(kontonummer);
  }

  @Override
  @Transactional(readOnly = true)
  public int countAktiveKontenByKundeId(Long kundeId) {
    log.debug("Anzahl aktiver Kreditkonten für KundeID: " + kundeId + " wird ermittelt.");
    return kreditKontoRepository.countAktiveKontenByKundeId(kundeId);
  }

}
