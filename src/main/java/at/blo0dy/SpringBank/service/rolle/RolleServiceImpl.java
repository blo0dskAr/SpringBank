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
  @Transactional(readOnly = true)
  public List<Rolle> findAll() {
    return rolleRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Rolle findById(Long theId) {

    Optional<Rolle> result = rolleRepository.findById(theId);

    Rolle rolle;

    rolle = result.get();

    return rolle ;
  }

  @Override
//  @Transactional
  public void save(Rolle rolle) {
      rolleRepository.save(rolle);
  }

  // Fuer delete im postmapping
  @Override
  public void delete(Rolle rolle) {
    rolleRepository.delete(rolle);
  }

  @Override
//  @Transactional
  public void deleteById(Long theId) {
      rolleRepository.deleteById(theId);
  }

  @Override
  @Transactional(readOnly = true)
  public long countByMitarbeiterId(Long theId) {
    return rolleRepository.countByMitarbeiterId(theId);
  }

  public List<Mitarbeiter> findMitarbeiterIdsByRoleId(Long theId) {
    List<Long> mitarbeiterIdListe = rolleRepository.findMitarbeiterIdsByRoleId(theId) ;
    return mitarbeiterRepository.findAllById(mitarbeiterIdListe);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Mitarbeiter> findMitarbeiterIdsByRoleIdExeptExisting(Long theRoleId) {
    List<Long> mitarbeiterIdListe = rolleRepository.findMitarbeiterIdsByRoleIdExeptExisting(theRoleId) ;
    return mitarbeiterRepository.findAllById(mitarbeiterIdListe);

  }

  @Override
  @Transactional
  public void removeRoleFromUser(Long theRoleId, Long theMitarbeiterId) {
    Mitarbeiter mitarbeiter = mitarbeiterRepository.findById(theMitarbeiterId).get();
    Rolle rolle = rolleRepository.findById(theRoleId).get();
    mitarbeiter.getRollen().remove(rolle);
//    rolleRepository.removeRoleFromUser(theRoleId, theMitarbeiterId);
  }

  @Override
  @Transactional
  public void addRoleToUser(Long theRoleId, Long theMitarbeiterId) {
    Mitarbeiter mitarbeiter = mitarbeiterRepository.findById(theMitarbeiterId).get();
    Rolle rolle = rolleRepository.findById(theRoleId).get();
    mitarbeiter.addRolle(rolle);
//    rolleRepository.addRoleToUser(theRoleId,theMitarbeiterId);
  }

}
