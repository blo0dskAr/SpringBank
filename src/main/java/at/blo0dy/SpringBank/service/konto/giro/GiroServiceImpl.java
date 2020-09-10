package at.blo0dy.SpringBank.service.konto.giro;

import at.blo0dy.SpringBank.dao.konto.giro.GiroKontoRepository;
import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GiroServiceImpl implements GiroService {

  GiroKontoRepository giroKontoRepository;

  @Autowired
  public GiroServiceImpl(GiroKontoRepository giroKontoRepository) {
    this.giroKontoRepository = giroKontoRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public GiroKonto findById(Long giroKontoId) {
    log.debug("Girokonto mit ID: " + giroKontoId + " wird gesucht.");
    Optional<GiroKonto> result = giroKontoRepository.findById(giroKontoId) ;

    if (result.isEmpty()) {
      log.error("GiroKonto mit ID: " + giroKontoId + " nicht gefunden.");
      // TODO: ExceptionHandling wenn null
//      throw new NotFoundException("GiroKonto mit ID: " + giroKontoId + " nicht gefunden.");
    }

    GiroKonto giroKonto = result.get();
    log.debug("GiroKonto mit ID: " + giroKontoId + " gefunden.");

    return giroKonto ;
  }

  @Override
//  @Transactional
  public void save(GiroKonto giroKonto) {
    log.debug("GiroKonto wird gespeichert.");

    GiroKonto savedGirokonto = giroKontoRepository.save(giroKonto);
    log.debug("GiroKonto wurde erfolgreich gespeichert. ID: " + savedGirokonto.getId());
  }

  @Override
  @Transactional(readOnly = true)
  public List<GiroKonto> findGiroKontenByKundennummer(String kundennummer) {
    log.debug("Girokonten für Kundennummer: " + kundennummer + " werden gesucht");

    List<GiroKonto> giroKontoList = giroKontoRepository.findGiroKontenByKundennummer(kundennummer);
    log.debug(giroKontoList.size() + " Girokonten für Kundennummer: " + kundennummer + " gefunden.");
    return giroKontoList;
  }

  @Override
  @Transactional(readOnly = true)
  public List<String> findKontoNummerOffenerGiroKontenByKundennummer(String kundennummer) {
    log.debug("Kontonummern offener Girokonten für Kundennummer: " + kundennummer + " werden gesucht.");
    return giroKontoRepository.findKontoNummerOffenerGiroKontenByKundennummer(kundennummer) ;
  }

  @Override
  @Transactional(readOnly = true)
  public GiroKonto findGiroKontoByKontonummerAndKundennummer(String kontonummer, String kundennummer) {
    log.debug("Girokonto für Kunde: " + kundennummer + " und Konto: " + kontonummer + " wird gesucht.");
    return giroKontoRepository.findGiroKontoByKontonummerAndKundennummer(kontonummer, kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public GiroKonto findByKontonummer(String kontonummer) {
    log.debug("GiroKonto mit Kontonummer: " + kontonummer + " wird gesucht.");
    return giroKontoRepository.findByKontonummer(kontonummer);
  }

  @Override
  @Transactional(readOnly = true)
  public int countAktiveKontenByKundeId(Long kundeId) {
    log.debug("Anzahl aktiver Konten für Kunde mit ID: " + kundeId + " wird ermittelt.");
    return giroKontoRepository.countAktiveKontenByKundeId(kundeId);
  }

}
