package at.blo0dy.SpringBank.service;

import at.blo0dy.SpringBank.dao.person.MitarbeiterRepository;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MitarbeiterServiceImpl implements MitarbeiterService {

  private MitarbeiterRepository mitarbeiterRepository;

//  @Autowired
//  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public MitarbeiterServiceImpl(MitarbeiterRepository mitarbeiterRepository) {
    this.mitarbeiterRepository = mitarbeiterRepository;
  }


  @Override
  @Transactional(readOnly = true)
  public List<Mitarbeiter> findAll() {
    log.debug("Liste aller Mitarbeiter wird ausgegeben");
    return mitarbeiterRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Mitarbeiter findById(Long theId) {
    log.debug("Mitarbeiter mit der ID: " + theId + " wird gesucht.");

    Optional<Mitarbeiter> result = mitarbeiterRepository.findById(theId);
    Mitarbeiter mitarbeiter;
    mitarbeiter = result.get();

    log.debug("Mitarbeiter mit der ID: " + theId + " wurde gefunden.");
    return mitarbeiter ;
  }

  @Override
//  @Transactional
  public void save(Mitarbeiter mitarbeiter) {
    log.debug("Mitarbeiter wird gespeichert");

    Mitarbeiter savedMitarbeiter = mitarbeiterRepository.save(mitarbeiter);

    log.debug("Mitarbeiter erfolgreich gespeichert. ID: " + savedMitarbeiter.getId());
  }

  @Override
//  @Transactional
  public void deleteById(Long theId) {
    log.debug("Mitarbeiter mit ID: " + theId + " wird gelöscht.");
    mitarbeiterRepository.deleteById(theId);
    log.debug("Mitarbeiter mit ID: " + theId + " wurde erfolgreich gelöscht.");

  }

  @Override
  @Transactional(readOnly = true)
  public List<Mitarbeiter> findMitarbeiterByVorAndNachName(String theSearchName) {
    log.debug("Mitarbeitersuche mit Name(" + theSearchName + ") wird durchgeführt.");
    return mitarbeiterRepository.findMitarbeiterByVorname(theSearchName);
  }

  @Override
  @Transactional(readOnly = true)
  public long count() {
    log.debug("Anzahl aller Mitarbeiter wird ermittelt");
    return mitarbeiterRepository.count();
  }

  @Override
  @Transactional(readOnly = true)
  public String getLatestMitarbeiterNummerPlusOne() {
    log.debug("Neue Mitarbeiternummer wird ermittelt ");
    String neueMitarbeiternummer = mitarbeiterRepository.getLatestMitarbeiterNummerPlusOne();

    if (neueMitarbeiternummer == null) {
      neueMitarbeiternummer = "1";
    }

    log.debug("Mitarbeiter nummer erfolgreich ermittelt: " + neueMitarbeiternummer);
    return neueMitarbeiternummer;
  }

  @Override
  @Transactional(readOnly = true)
  public Mitarbeiter findByUserName(String tmpUser) {
    log.debug("Mitarbeiter mit Loginname: " + tmpUser + " wird gesucht.");
    return mitarbeiterRepository.findByUsername(tmpUser);
  }

  @Override
  @Transactional
  public void updatePasswordByMitarbeiterId(Long mitarbeiterId, String encodedPassword) {
    log.debug("Password für MitarbeiterId: " + mitarbeiterId + " wird gespeichert.");
    Mitarbeiter mitarbeiter = mitarbeiterRepository.findById(mitarbeiterId).get();
    mitarbeiter.getLoginCredentials().setPassword(encodedPassword);
    log.debug("Password für MitarbeiterId: " + mitarbeiterId + " wurde erfolgreich gespeichert.");
//    mitarbeiterRepository.updatePasswordByMitarbeiterId(mitarbeiterId, encodedPassword);
  }

}
