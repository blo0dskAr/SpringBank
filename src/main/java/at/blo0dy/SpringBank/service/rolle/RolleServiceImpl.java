package at.blo0dy.SpringBank.service.rolle;

import at.blo0dy.SpringBank.dao.MitarbeiterRepository;
import at.blo0dy.SpringBank.dao.RolleRepository;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.model.person.rolle.Rolle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
  @Transactional
  public List<Rolle> findAll() {
    return rolleRepository.findAll();
  }

  @Override
  @Transactional
  public Rolle findById(Long theId) {

    Optional<Rolle> result = rolleRepository.findById(theId);

    Rolle rolle;

    rolle = result.get();

    return rolle ;
  }

  @Override
  @Transactional
  public void save(Rolle rolle) {
    System.out.println("Called Method: " + getClass().getSimpleName() + "save(Rolle rolle)");
      rolleRepository.save(rolle);
  }

  @Override
  @Transactional
  public void deleteById(Long theId) {
    System.out.println(countByMitarbeiterId(theId));
//    if (countByMitarbeiterId(theId) == 0) {
      rolleRepository.deleteById(theId);
/*    } else {
      System.err.println("fehler beim löschen");
    }*/
// TODO: Funkt nicht weils an einer stelle weiter unten hinterm repo kracht und die exception nicht mitkommt.
    // ToDo: da muss ein exceptionhandler oder sowas dazwischen?
/*    try {
      rolleRepository.deleteById(theId);
    } catch (Exception ex) {
      System.out.println("Exception caught: " + getClass().getSimpleName() + "deleteById(Long theId)");
      ex.toString();
    }*/
  }

  @Override
  @Transactional
  public long countByMitarbeiterId(Long theId) {
    return rolleRepository.countByMitarbeiterId(theId);
  }

  public List<Mitarbeiter> findMitarbeiterIdsByRoleId(Long theId) {
    List<Long> mitarbeiterIdListe = rolleRepository.findMitarbeiterIdsByRoleId(theId) ;
    return mitarbeiterRepository.findAllById(mitarbeiterIdListe);
  }

  @Override
  @Transactional
  public List<Mitarbeiter> findMitarbeiterIdsByRoleIdExeptExisting(Long theRoleId) {
    List<Long> mitarbeiterIdListe = rolleRepository.findMitarbeiterIdsByRoleIdExeptExisting(theRoleId) ;
    return mitarbeiterRepository.findAllById(mitarbeiterIdListe);

  }

  @Override
  @Transactional
  public void removeRoleFromUser(Long theId, Long theMitarbeiterId) {
    rolleRepository.removeRoleFromUser(theId, theMitarbeiterId);
  }

  @Override
  @Transactional
  public void addRoleToUser(Long theRoleId, Long theMitarbeiterId) {
    rolleRepository.addRoleToUser(theRoleId,theMitarbeiterId);
  }




}
