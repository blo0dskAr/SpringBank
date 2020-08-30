package at.blo0dy.SpringBank.service.rolle;

import at.blo0dy.SpringBank.dao.person.MitarbeiterRepository;
import at.blo0dy.SpringBank.dao.person.RolleRepository;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.model.person.rolle.Rolle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RolleServiceImpl implements RolleService {

  private RolleRepository rolleRepository;
  private MitarbeiterRepository mitarbeiterRepository;

  @Autowired
  public RolleServiceImpl(RolleRepository rolleRepository, MitarbeiterRepository mitarbeiterRepository) {
    this.rolleRepository = rolleRepository;
    this.mitarbeiterRepository = mitarbeiterRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Rolle> findAll() {
    log.debug("Alle Rollen werden gesucht.");
    return rolleRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Rolle findById(Long theId) {
    log.debug("Rolle mit RolleId: " + theId + " wird gesucht.");

    Optional<Rolle> result = rolleRepository.findById(theId);
    Rolle rolle;
    rolle = result.get();

    log.debug("Rolle mit RolleId: " + theId + " wurde gefunden.");
    return rolle ;
  }

  @Override
//  @Transactional
  public void save(Rolle rolle) {
    log.debug("Rolle wird gespeichert.");
    Rolle savedRolle = rolleRepository.save(rolle);
    log.debug("Rolle wurde erfolgreich gespeichert. ID: " + savedRolle.getId());
  }

  @Override
//  @Transactional
  public void deleteById(Long theId) {
    log.debug("Rolle mit RolleId: " + theId + " wird gelöscht.");
    rolleRepository.deleteById(theId);
    log.debug("Rolle mit der RolleId: " + theId + " wurde erfolgreich gelöscht.");
  }

  @Override
  @Transactional(readOnly = true)
  public List<Mitarbeiter> findMitarbeiterIdsByRoleId(Long theRoleId) {
    log.debug("Mitarbeiter für RolleId: " + theRoleId + " werden gesucht.");
    List<Long> mitarbeiterIdListe = rolleRepository.findMitarbeiterIdsByRoleId(theRoleId) ;
    log.debug(mitarbeiterIdListe.size() + " Mitarbeiter für RolleId: " + theRoleId + " gefunden.");
    return mitarbeiterRepository.findAllById(mitarbeiterIdListe);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Mitarbeiter> findMitarbeiterIdsByRoleIdExeptExisting(Long theRoleId) {
    log.debug("Mitarbeiter für RolleId: " + theRoleId + " werden gesucht. (bereits bestehende ausgenommen");
    List<Long> mitarbeiterIdListe = rolleRepository.findMitarbeiterIdsByRoleIdExeptExisting(theRoleId) ;
    log.debug(mitarbeiterIdListe.size() + " Mitarbeiter für RolleId: " + theRoleId + " gefunden.");
    return mitarbeiterRepository.findAllById(mitarbeiterIdListe);

  }

  @Override
  @Transactional
  public void removeRoleFromUser(Long theRoleId, Long theMitarbeiterId) {
    log.debug("Rolle mit der RolleId: " + theRoleId + " wird von MitarbeiterId: " + theMitarbeiterId + " entfernt.");
    Mitarbeiter mitarbeiter = mitarbeiterRepository.findById(theMitarbeiterId).get();
    Rolle rolle = rolleRepository.findById(theRoleId).get();
    mitarbeiter.getRollen().remove(rolle);
    log.debug("Rolle mit der RolleId: " + theRoleId + " erfolgreich von MitarbeiterId: " +theMitarbeiterId + " entfernt.");
//    rolleRepository.removeRoleFromUser(theRoleId, theMitarbeiterId);
  }

  @Override
  @Transactional
  public void addRoleToUser(Long theRoleId, Long theMitarbeiterId) {
    log.debug("Mitarbeiter mit der ID: " + theMitarbeiterId + " wird der RolleId: " + theRoleId + " hinzugefügt.");
    Mitarbeiter mitarbeiter = mitarbeiterRepository.findById(theMitarbeiterId).get();
    Rolle rolle = rolleRepository.findById(theRoleId).get();
    mitarbeiter.addRolle(rolle);
    log.debug("Mitarbeiter mit der ID: " + theMitarbeiterId + " erfolgreich der RolleId: " + theRoleId + " hinzugefügt.");
//    rolleRepository.addRoleToUser(theRoleId,theMitarbeiterId);
  }

}
