package at.blo0dy.SpringBank.service.konto.sparen;

import at.blo0dy.SpringBank.dao.konto.sparen.SparKontoAntragRepository;
import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SparKontoAntragServiceImpl implements SparKontoAntragService {

  SparKontoAntragRepository sparKontoAntragRepository;

  @Autowired
  public SparKontoAntragServiceImpl(SparKontoAntragRepository sparKontoAntragRepository) {
    this.sparKontoAntragRepository = sparKontoAntragRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public SparKontoAntrag findById(Long theId) {
    log.debug("SparAntrag mit ID: " + theId + " wird gesucht.");

    Optional<SparKontoAntrag> result = sparKontoAntragRepository.findById(theId);
    SparKontoAntrag sparKontoAntrag;
    sparKontoAntrag = result.get();

    log.debug("SparAntrag mit ID: " + theId + " wurde gefunden.");
    return sparKontoAntrag ;
  }

  @Override
  public void save(SparKontoAntrag sparKontoAntrag) {
    log.debug("SparAntrag wird gespeichert.");

    SparKontoAntrag savedSparAntrag = sparKontoAntragRepository.save(sparKontoAntrag);

    log.debug("SparAntrag wurde erfolgreich gespeichert. ID: " + savedSparAntrag.getId());
  }

  @Override
  @Transactional(readOnly = true)
  public List<SparKontoAntrag> findSparAntraegeByKundennummer(String kundennummer) {
    log.debug("SparAnträge für Kundennummer " + kundennummer + " werden gesucht");
    return sparKontoAntragRepository.findSparAntraegeByKundennummer(kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public SparKontoAntrag findSparAntragByAntragIdAndKundennummer(Long antragId, String authKundennummer) {
    log.debug("SparAntrag für Kundennummer: " + authKundennummer + "und AntragId: " + antragId + " wird gesucht.");
    return sparKontoAntragRepository.findSparAntragByAntragIdAndKundennummer(antragId, authKundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public int countEingereichteSparAntraegeByKundennummer(String kundennummer) {
    log.debug("Anzahl eingereichter SparAnträge für Kundennummer: " + kundennummer + " wird ermittelt.");
    return sparKontoAntragRepository.countEingereichteSparAntraegeByKundennummer(kundennummer);
  }

}
