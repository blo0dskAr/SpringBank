package at.blo0dy.SpringBank.service.kunde;

import at.blo0dy.SpringBank.dao.KundeRepository;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Slf4j
@Service
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


  @Override
  @Transactional
  public void setKundeActiveIfRequirementsMetByKundennummer(String kundennummer) {

    log.debug("KundeServiceImpl: setKundeActiveIfRequirementsMetByKundennummer -> Suche kunde mit Kundennummer: " + kundennummer);
      Kunde kunde = kundeRepository.findByKundennummer(kundennummer);
      boolean neuerStatus = kunde.isActive();

      if (kunde.isActive()) {
        if (kunde.isHasAcceptedAGB() && kunde.isLegi()) {
          log.debug("KundeServiceImpl: KundenStatusCheck durchgeführt für " + kundennummer + ": Keine Änderung durchgeführt (bereits aktiv)");
        } else {
          log.debug("KundeServiceImpl: KundenStatusCheck durchgeführt für " + kundennummer + ": Legi oder AGB fehlt, Aktiv-Status entfernt");
          neuerStatus = false;
        }
      } else {
        if (kunde.isHasAcceptedAGB() && kunde.isLegi()) {
          log.debug("KundeServiceImpl: KundenStatusCheck durchgeführt für " + kundennummer + ": Legi und AGB zwischenzeitlich erhalten: Status aktiv gesetzt.");
          neuerStatus = true;
        } else {
          log.debug("KundeServiceImpl: KundenStatusCheck durchgeführt für " + kundennummer + ": Keine Änderung durchgeführt. (warte auf AGB oder Legi)");
        }
      }

        // TODO: da  braucht man ned speichern wenn der status ned verändert wird. hab die methode aber erst um 1 uhr gfunden :D wusste ned dass ich die schon gmacht hab -.- ...
        // TODO: Ausserdem is die obige if abfrage definitv übertrieben .. und methode umbenennnen ;) und von kundennummer auf kundeId wechseln :D
      log.debug("Status von Kunde: " + kundennummer + " Status=" + neuerStatus + "  wird gespeichert");
      kundeRepository.updateActiveStatusById(kunde.getId(), neuerStatus);

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
    log.debug("Neue Kontonummer für Kunde " + kundennummer + " wird generiert");
    Long newKontonummer;
    try {
      newKontonummer = kundeRepository.getLatestKontonummerByKundennummer(kundennummer) + 1;
    } catch (NullPointerException npe) {
      log.debug("Neue Kontonummer für Kunde " + kundennummer + " wird generiert: Ist erstes Konto: 001");
      newKontonummer = Long.valueOf(kundennummer + "001");
    }

    log.debug("Neue Kontonummer für Kunde " + kundennummer + " lautet:" + newKontonummer);
    return newKontonummer;
  }

  @Override
  @Transactional
  public Long getLatestKundennummerPlusOne() {
    log.debug("Neue Kundennummer wird generiert");
    return kundeRepository.getLatestKundennummerPlusOne();
  }

  @Override
  @Transactional
  public String getConnectedGiroByKundennummer(String kundennummer) {
    return kundeRepository.getConnectedGiroByKundennummer(kundennummer);
  }

  @Override
  @Transactional
  public void updateChangeableDataByKundennummer(String kundennummer, String email, String tel, String connectedGiro, boolean hasAcceptedAGB) {
    kundeRepository.updateChangeableDataByKundennummer(kundennummer, email, tel, connectedGiro, hasAcceptedAGB);
  }

  @Override
  @Transactional
  public List<Kunde> findAll(Kunde kunde, ExampleMatcher matcher) {

    return kundeRepository.findAll(Example.of(kunde, matcher));
  }

  @Override
  @Transactional
  public void saveWithoutPassword(Kunde kunde) {

    Optional<Kunde> tmpKunde = kundeRepository.findById(kunde.getId());
    kunde.setPassword(tmpKunde.get().getPassword());

    kundeRepository.save(kunde);
  }

  @Override
  @Transactional
  public void updateLegiStatusById(Long kundeId, boolean status) {
    kundeRepository.updateLegiStatusById(kundeId, status);
    Optional<Kunde> tmpKunde = kundeRepository.findById(kundeId);
    Kunde kunde = tmpKunde.get();
    setKundeActiveIfRequirementsMetByKundennummer(kunde.getKundennummer());
  }

  @Override
  @Transactional
  public void updateActiveStatusById(Long kundeId, boolean status) {
    kundeRepository.updateActiveStatusById(kundeId, status);
  }

}
