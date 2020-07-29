package at.blo0dy.SpringBank.service.konto.giro;

import at.blo0dy.SpringBank.dao.konto.giro.GiroKontoAntragRepository;
import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class GiroKontoAntragServiceImpl implements GiroKontoAntragService {

  GiroKontoAntragRepository giroKontoAntragRepository;

  @Autowired
  public GiroKontoAntragServiceImpl(GiroKontoAntragRepository giroKontoAntragRepository) {
    this.giroKontoAntragRepository = giroKontoAntragRepository;
  }

  // sollte nicht gebraucht werden
//  @Override
//  @Transactional(readOnly = true)
//  public List<GiroKontoAntrag> findAll() {
//    return giroKontoAntragRepository.findAll();
//  }

  @Override
  @Transactional(readOnly = true)
  public GiroKontoAntrag findById(Long giroAntragId) {
    log.debug("GiroKontoAntrag mit ID: " + giroAntragId + " wird gesucht.");

    GiroKontoAntrag giroKontoAntrag;

    Optional<GiroKontoAntrag> result = giroKontoAntragRepository.findById(giroAntragId);
    if (result.isEmpty()) {
      log.error("GiroAntrag mit ID: " + giroAntragId + " nicht gefunden.");
      // TODO: ExceptionHandling wenn null
//      throw new NotFoundException("GiroAntrag mit ID: " + giroAntragId + " nicht gefunden.");
    }

    giroKontoAntrag = result.get();
    log.debug("GiroAntrag mit ID: " + giroAntragId + " gefunden.");

    return giroKontoAntrag ;
  }

  @Override
//  @Transactional
  public void save(GiroKontoAntrag giroKontoAntrag) {
    log.debug("GirokontoAntrag wird gespeichert:");

    GiroKontoAntrag savedGiroAntrag = giroKontoAntragRepository.save(giroKontoAntrag);

    log.debug("GirokontoAntrag wurde erfolgreich gespeichert. ID: " + savedGiroAntrag.getId());
  }

//  @Override
////  @Transactional
//  public void deleteById(Long theId) {
//    log.debug("GirokontoAntrag wird geloescht:");
//    giroKontoAntragRepository.deleteById(theId);
//  }

//  @Override
//  @Transactional(readOnly = true)
//  public long count() {
//    return giroKontoAntragRepository.count();
//  }

//  @Override
//  @Transactional(readOnly = true)
//  public long countByStatus(String statusEnum) {
//    return giroKontoAntragRepository.countByStatus(statusEnum);
//  }

//  public List<GiroKontoAntrag> findByStatus(String statusEnum) {
//    List<GiroKontoAntrag> giroKontoAntragListe = giroKontoAntragRepository.findByStatus(statusEnum) ;
//    return  giroKontoAntragListe;
//  }

  @Override
  @Transactional(readOnly = true)
  public List<GiroKontoAntrag> findGiroAntraegeByKundennummer(String kundennummer) {
    log.debug("GiroKontoAnträge für Kundennummer: " + kundennummer + " werden gesucht.");

    List<GiroKontoAntrag> giroKontoAntragList = giroKontoAntragRepository.findGiroAntraegeByKundennummer(kundennummer);
    log.debug("GiroKontoAntrag für Kundennummer: "+ kundennummer + " wurden gefunden. Anzahl: " + giroKontoAntragList.size());

    return giroKontoAntragList;
  }

  @Override
  @Transactional(readOnly = true)
  public GiroKontoAntrag findGiroAntragByAntragIdAndKundennummer(Long antragId, String kundennummer) {
    log.debug("GiroKontoAntrag für Kundennummer: " + kundennummer + " und AntragId: " + antragId + " wird gesucht.");
    return giroKontoAntragRepository.findGiroAntragByAntragIdAndKundennummer(antragId, kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public int countEingereichteGiroAntraegeByKundennummer(String kundennummer) {
    log.debug("Anzahl eingereichter GiroAnträge für Kundennummer: " + kundennummer + " wird ermittelt.");
    return giroKontoAntragRepository.countEingereichteGiroAntraegeByKundennummer(kundennummer);
  }

}
