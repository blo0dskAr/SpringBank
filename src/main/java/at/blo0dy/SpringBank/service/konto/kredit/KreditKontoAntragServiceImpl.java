package at.blo0dy.SpringBank.service.konto.kredit;

import at.blo0dy.SpringBank.dao.konto.kredit.KreditKontoAntragRepository;
import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class KreditKontoAntragServiceImpl implements KreditKontoAntragService {

  KreditKontoAntragRepository kreditKontoAntragRepository;

  @Autowired
  public KreditKontoAntragServiceImpl(KreditKontoAntragRepository kreditKontoAntragRepository) {
    this.kreditKontoAntragRepository = kreditKontoAntragRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public KreditKontoAntrag findById(Long theId) {
    log.debug("KreditAntrag mit ID: " + theId + " wird gesucht.");

    Optional<KreditKontoAntrag> result = kreditKontoAntragRepository.findById(theId);
    KreditKontoAntrag kreditKontoAntrag;
    kreditKontoAntrag = result.get();

    log.debug("KreditAntrag mit ID: " + theId + " wurde gefunden.");
    return kreditKontoAntrag ;
  }

  @Override
//  @Transactional
  public void save(KreditKontoAntrag kreditKontoAntrag) {
    log.debug("KreditAntrag wird gespeichert.");

    KreditKontoAntrag savedKreditAntrag = kreditKontoAntragRepository.save(kreditKontoAntrag);

    log.debug("KreditAntrag wurde erfolgreich gespeichert. ID: " + savedKreditAntrag.getId());
  }

  @Override
  public boolean compareBasicKreditData(KreditKontoAntrag antrag1, KreditKontoAntrag antrag2) {
    if (antrag1.getKreditBetrag().equals(antrag2.getKreditBetrag()) && antrag1.getZinssatz().equals(antrag2.getZinssatz()) && antrag1.getLaufzeit().equals(antrag2.getLaufzeit())) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<KreditKontoAntrag> findKreditAntraegeByKundennummer(String kundennummer) {
    log.debug("KreditAntrag für Kundennummer: " + kundennummer + " wird gesucht.");
    return kreditKontoAntragRepository.findKreditAntraegeByKundennummer(kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public KreditKontoAntrag findKreditAntragByAntragIdAndKundennummer(Long antragId, String kundennummer) {
    log.debug("KreditAntrag für Kundennummer: " + kundennummer + " und AntragId: " + antragId + " wird gesucht.");
    return kreditKontoAntragRepository.findKreditAntragByAntragIdAndKundennummer(antragId, kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public int countEingereichteKreditAntraegeByKundennummer(String kundennummer) {
    log.debug("Anzahl eingereichter KreditAnträge für Kundennummer: " + kundennummer + " wird ermittelt." );
    return kreditKontoAntragRepository.countEingereichteKreditAntraegeByKundennummer(kundennummer);
  }


}
