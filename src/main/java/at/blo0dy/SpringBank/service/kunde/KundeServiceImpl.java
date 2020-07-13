package at.blo0dy.SpringBank.service.kunde;

import at.blo0dy.SpringBank.dao.KundeRepository;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.TransactionScoped;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
//public class KundeServiceImpl implements KundeService {
public class KundeServiceImpl implements KundeService, UserDetailsService {

  private KundeRepository kundeRepository;

  @Autowired
  public KundeServiceImpl(KundeRepository KundeRepository) {
    this.kundeRepository = KundeRepository;
  }


  @Override
  @Transactional
  public List<Kunde> findAll() {
    return kundeRepository.findAll();
  }

  @Override
  @Transactional
  public Kunde findById(Long theId) {

    Optional<Kunde> result = kundeRepository.findById(theId);

    Kunde kunde;

    kunde = result.get();

    return kunde ;
  }

  @Override
  @Transactional
  public void save(Kunde kunde) {
    kundeRepository.save(kunde);
  }

  @Override
  @Transactional
  public void deleteById(Long theId) {
    kundeRepository.deleteById(theId);
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String kundennummer) throws UsernameNotFoundException {
      final Kunde kunde = kundeRepository.findByKundennummer(kundennummer);
    if (kunde != null) {
      return kunde;
    }
    throw new UsernameNotFoundException("User + " + kunde.getKundennummer() + " not found");
  }

  public void setKundeActiveIfRequirementsMetByKundennummer(String kundennummer) {

    log.debug("KundeServiceImpl: setKundeActiveIfRequirementsMetByKundennummer -> Suche kunde mit Kundennummer: " + kundennummer);
      Kunde kunde = kundeRepository.findByKundennummer(kundennummer);

      if (kunde.isActive()) {
        if (kunde.isHasAcceptedAGB() && kunde.isLegi()) {
          log.debug("KundeServiceImpl: KundenStatusCheck durchgeführt für " + kundennummer + ": Keine Änderung durchgeführt (bereits aktiv)");
        } else {
          log.debug("KundeServiceImpl: KundenStatusCheck durchgeführt für " + kundennummer + ": Legi oder AGB fehlt, Aktiv-Status entfernt");
          kunde.setActive(false);
        }
        if (!kunde.isActive()) {
          if (kunde.isHasAcceptedAGB() && kunde.isLegi()) {
            log.debug("KundeServiceImpl: KundenStatusCheck durchgeführt für " + kundennummer + ": Legi und AGB zwischenzeitlich erhalten: Status aktiv gesetzt.");
            kunde.setActive(true);
          } else {
            log.debug("KundeServiceImpl: KundenStatusCheck durchgeführt für " + kundennummer + ": Keine Änderung durchgeführt. (warte auf AGB oder Legi)");
          }
        }
    }
      log.debug("Kunde " + kundennummer + " wird gespeichert.");
      kundeRepository.save(kunde);
  }

  @Override
  @Transactional
  public Kunde findByKundennummer(String kundennummer) {
    return kundeRepository.findByKundennummer(kundennummer);
  }

  @Override
  @Transactional
  public KontoStatusEnum getBestmoeglicherKontoStatusByKundennummer(String kundennummer) {

    log.debug("KundeServiceImpl: getBestmoeglicherKontoStatusByKundennummer -> Suche Kunde mit Kundennummer: " + kundennummer);
    final Kunde kunde = kundeRepository.findByKundennummer(kundennummer);

    // Vorm returnieren nochmal prüfen
    log.debug("KundeServiceImpl: getBestmoeglicherKontoStatusByKundennummer: Setzt ggf. neuen KundenStatus für Kundennummer: " + kundennummer);
    setKundeActiveIfRequirementsMetByKundennummer(kundennummer);

    if (kunde.isActive()) {
      return KontoStatusEnum.OFFEN;
    } else {
      return KontoStatusEnum.IN_EROEFFNUNG;
    }
  }

  @Override
  @Transactional
  public Long generateNewKontonummerByKundennummer(String kundennummer) {
    log.debug("KundeServiceImpl: Neue Kontonummer für Kunde " + kundennummer + " wird generiert");
    Long newKontonummer = kundeRepository.getLatestKontonummerByKundennummer(kundennummer)+1 ;

    log.debug("KundeServiceImpl: Neue Kontonummer für Kunde " + kundennummer + " lautet:" + newKontonummer);
    return newKontonummer;
  }

  @Override
  @Transactional
  public Long getLatestKundennummerPlusOne() {
    log.debug("KundeServiceImpl: Neue Kundennummer wird generiert");
    return kundeRepository.getLatestKundennummerPlusOne();
  }

  @Override
  @Transactional
  public String getConnectedGiroByKundennummer(String kundenummer) {
    return kundeRepository.getConnectedGiroByKundennummer(kundenummer);
  }













}
