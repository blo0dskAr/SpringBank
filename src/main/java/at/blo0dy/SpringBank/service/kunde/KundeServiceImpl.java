package at.blo0dy.SpringBank.service.kunde;

import at.blo0dy.SpringBank.dao.person.KundeRepository;
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

  // TODO: DA MUSS ICH NOCH DAS TRANSACTIONAL ORDENTLICH UMSETZEN, EINIGE REPOMETHODEN WERDEN DA SICHER NICHT GEBRAUCHT.

  private KundeRepository kundeRepository;

  @Autowired
  public KundeServiceImpl(KundeRepository KundeRepository) {
    this.kundeRepository = KundeRepository;
  }

  @Override
  @Transactional
  public Kunde findById(Long theId) {

    log.debug("Kunde mit KundeId: " + theId + " wird gesucht.");

    Optional<Kunde> result = kundeRepository.findById(theId);
    Kunde kunde;
    kunde = result.get();

    log.debug("Kunde mit KundeId: " + theId + " wurde gefunden.");

    return kunde ;
  }

  @Override
  public void save(Kunde kunde) {
    log.debug("Kunde wird gespeichert.");
    Kunde savedKunde = kundeRepository.save(kunde);
    log.debug("Kunde erfolgreich gespeichert. ID: " + savedKunde.getId() + " Kundennummmer: " + savedKunde.getKundennummer());

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

    log.debug("KundenStatusCheck mit Kundennummer: " + kundennummer + " wird durchgeführt");
      Kunde tmpKunde = kundeRepository.findByKundennummer(kundennummer);
      boolean neuerStatus = tmpKunde.isActive();

      if (neuerStatus) {
        if (tmpKunde.isHasAcceptedAGB() && tmpKunde.isLegi()) {
          log.debug("KundenStatusCheck durchgeführt für " + kundennummer + ": Keine Änderung durchgeführt (bereits aktiv)");
        } else {
          log.debug("KundenStatusCheck durchgeführt für " + kundennummer + ": Legi oder AGB fehlt, Aktiv-Status entfernt");
          neuerStatus = false;
        }
      } else {
        if (tmpKunde.isHasAcceptedAGB() && tmpKunde.isLegi()) {
          log.debug("KundenStatusCheck durchgeführt für " + kundennummer + ": Legi und AGB zwischenzeitlich erhalten: Status aktiv gesetzt.");
          neuerStatus = true;
        } else {
          log.debug("KundenStatusCheck durchgeführt für " + kundennummer + ": Keine Änderung durchgeführt. (warte auf AGB oder Legi)");
        }
      }

        // TODO: da  braucht man ned speichern wenn der status ned verändert wird. hab die methode aber erst um 1 uhr gfunden :D wusste ned dass ich die schon gmacht hab -.- ...
        // TODO: Ausserdem is die obige if abfrage definitv übertrieben .. und methode umbenennnen ;) und von kundennummer auf kundeId wechseln :D
      log.debug("Status von Kunde: " + kundennummer + " Status=" + neuerStatus + "  wird gespeichert");
      tmpKunde.setActive(neuerStatus);
  }

  @Override
  @Transactional(readOnly = true)
  public Kunde findByKundennummer(String kundennummer) {
    log.debug("Kunde mit Kundennummer: " + kundennummer + " wird gesucht.");

    return kundeRepository.findByKundennummer(kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public KontoStatusEnum getBestmoeglicherKontoStatusByKundennummer(String kundennummer) {

    log.debug("Bestmöglicher KontoStatus für Kundennummer: " + kundennummer + " wird ermittelt.");
    final Kunde kunde = kundeRepository.findByKundennummer(kundennummer);

    // Vorm returnieren nochmal prüfen
    log.debug("KundenStatusCheck wird angestoßen.");
    setKundeActiveIfRequirementsMetByKundennummer(kundennummer);

    if (kunde.isActive()) {
      log.debug("Kunde ist aktiv. Bestmöglicher Status: Offen");
      return KontoStatusEnum.OFFEN;
    } else {
      log.debug("Kunde noch nicht aktiv: Bestmöglicher Status: In Eröffnung ");
      return KontoStatusEnum.IN_EROEFFNUNG;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public String generateNewKontonummerByKundennummer(String kundennummer) {
    log.debug("Neue Kontonummer für Kunde " + kundennummer + " wird generiert");
    String newKontonummer;
    try {
      // Das hin und her mit der Kontonummer is bissi schiach nach dem Wechsel zu String)
      Integer latestKontonummer = Integer.parseInt( kundeRepository.getLatestKontonummerByKundennummer(kundennummer))+1;
      newKontonummer = latestKontonummer.toString() ;
    } catch (NullPointerException npe) {
      // npe brauch ich theoretisch nicht mehr ? . parseInt nun ne numberformat exception wirft, wenn ergebnis == null)
      log.debug("Neue Kontonummer für Kunde " + kundennummer + " wird generiert: Ist erstes Konto: 001");
      newKontonummer = kundennummer + "001" ;
    } catch (NumberFormatException nfe) {
      log.debug("Neue Kontonummer für Kunde " + kundennummer + " wird generiert: Ist erstes Konto: 001");
      newKontonummer = kundennummer + "001" ;
    }

    log.debug("Neue Kontonummer für Kunde " + kundennummer + " lautet:" + newKontonummer);
    return newKontonummer;
  }

  @Override
  @Transactional(readOnly = true)
  public Long getLatestKundennummerPlusOne() {
    log.debug("Neue Kundennummer wird generiert");
    return kundeRepository.getLatestKundennummerPlusOne();
  }

  @Override
  @Transactional(readOnly = true)
  public String getConnectedGiroByKundennummer(String kundennummer) {
    log.debug("ConnectedGiro Konto für Kundennummer " + kundennummer + " wird ermittelt.");
    return kundeRepository.getConnectedGiroByKundennummer(kundennummer);
  }

  @Override
  @Transactional
  public void updateChangeableDataByKundennummer(String kundennummer, String email, String tel, String connectedGiro, boolean hasAcceptedAGB) {
    log.debug("Kunde mit Kundennummer " + kundennummer + " wird aktualisiert:");
    kundeRepository.updateChangeableDataByKundennummer(kundennummer, email, tel, connectedGiro, hasAcceptedAGB);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Kunde> findAll(Kunde kunde, ExampleMatcher matcher) {
    log.debug("Kundensuche wird durchgeführt.");
    return kundeRepository.findAll(Example.of(kunde, matcher));
  }

  @Override
  @Transactional
  public void saveWithoutPassword(Kunde kunde) {
    String tmpKundennummer = kunde.getKundennummer();
    log.debug("Kundendaten für Kunde: " + tmpKundennummer + " werden aktualisiert ");
    Optional<Kunde> tmpKunde = kundeRepository.findById(kunde.getId());
    kunde.setPassword(tmpKunde.get().getPassword());
    kundeRepository.save(kunde);
    log.debug("Kundendaten für Kunde: " + tmpKundennummer + " erfolgreich aktualsiert.");
  }

  @Override
//  @Transactional
  public void updateLegiStatusById(Long kundeId, boolean status) {
    log.debug("LegiStatus für KundeId: " + kundeId + " wird aktualisiert.");
    kundeRepository.updateLegiStatusById(kundeId, status);
    Optional<Kunde> tmpKunde = kundeRepository.findById(kundeId);
    Kunde kunde = tmpKunde.get();
    setKundeActiveIfRequirementsMetByKundennummer(kunde.getKundennummer());
    log.debug("LegiSTatus für KundeId: " + kundeId + " wurde erfolgreich aktualisiert.");
  }

  @Override
  @Transactional
  public void updatePasswordByKundeId(Long kundeId, String encodedPassword) {
    log.debug("Password für KundeId: " + kundeId + " wird aktualisiert.");
    kundeRepository.UpdatePasswordByKundeId(kundeId, encodedPassword);
  }

}
