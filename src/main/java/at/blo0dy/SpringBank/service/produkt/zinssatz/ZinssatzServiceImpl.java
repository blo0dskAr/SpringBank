package at.blo0dy.SpringBank.service.produkt.zinssatz;

import at.blo0dy.SpringBank.dao.produkt.zinssatz.ZinssatzRepository;
import at.blo0dy.SpringBank.model.produkt.zinssatz.Zinssatz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
public class ZinssatzServiceImpl implements ZinssatzService {

  ZinssatzRepository zinssatzRepository;

  @Autowired
  public ZinssatzServiceImpl(ZinssatzRepository zinssatzRepository) {
    this.zinssatzRepository = zinssatzRepository;
  }


  @Override
  @Transactional(readOnly = true)
  public BigDecimal getAktuellerSparZinssatz() {
    log.debug("Aktueller SparkontoZinssatz wird geladen.");
    return zinssatzRepository.getAktuellerSparZinssatz();
  }

  @Override
  @Transactional(readOnly = true)
  public BigDecimal getAktuellerKreditZinssatz() {
    log.debug("Aktueller KreditKontoZinssatz wird geladen.");
    return zinssatzRepository.getAktuellerKreditZinssatz();
  }

  @Override
  @Transactional(readOnly = true)
  public BigDecimal getAktuellerGiroZinssatz() {
    log.debug("Aktueller GirokontoZinssatz wird geladen.");
    return zinssatzRepository.getAktuellerGiroZinssatz();
  }

  @Override
  public void save(Zinssatz zinssatz) {
    log.debug("Zinssatz wird gespeichert.");

    Zinssatz savedZinssatz = zinssatzRepository.save(zinssatz);

    log.debug("Zinssatz erfolgreich gespeichert. ID: " + savedZinssatz.getId());
  }


}
