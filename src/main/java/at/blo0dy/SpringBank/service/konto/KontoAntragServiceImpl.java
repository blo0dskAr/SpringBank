package at.blo0dy.SpringBank.service.konto;

import at.blo0dy.SpringBank.dao.konto.KontoAntragRepository;
import at.blo0dy.SpringBank.model.antrag.KontoAntrag;
import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class KontoAntragServiceImpl implements KontoAntragService {

  KontoAntragRepository kontoAntragRepository;


  @Autowired
  public KontoAntragServiceImpl(KontoAntragRepository kontoAntragRepository) {
    this.kontoAntragRepository = kontoAntragRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public Long countAntraegeGesamtByKundennummer(String kundennummer) {
    log.debug("Anzahl aller Anträge für Kundennummer: " + kundennummer + " wird ermittelt.");
    return kontoAntragRepository.countAntraegeGesamtByKundennummer(kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public Long countOffeneAntraegeByKundennummer(String kundennummer) {
    log.debug("Anzahl offener Anträge für Kundennummer: " + kundennummer + " wird ermittelt.");
    return kontoAntragRepository.countOffeneAntraegeByKundennummer(kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public Long countAbgelehnteAntraegeByKundennummer(String kundennummer) {
    log.debug("Anzahl abgelehnter Anträge für Kundennummer: " + kundennummer + " wird ermittelt");
    return kontoAntragRepository.countAbgelehnteAntraegeByKundennummer(kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public Long countDurchgefuehrteAntraegeByKundennummer(String kundennummer) {
    log.debug("Anzahl durchgeführter Anträge für Kundennummer: " + kundennummer + " wird ermittelt");
    return kontoAntragRepository.countDurchgefuehrteAntraegeByKundennummer(kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public List<KontoAntrag> findAll(KontoAntrag kontoAntrag) {
    log.debug("Kontoanträge werden gesucht.");
    return kontoAntragRepository.findAll(Example.of(kontoAntrag));
  }


}
