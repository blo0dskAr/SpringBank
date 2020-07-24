package at.blo0dy.SpringBank.service;

import at.blo0dy.SpringBank.dao.MitarbeiterRepository;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    return mitarbeiterRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Mitarbeiter findById(Long theId) {

    Optional<Mitarbeiter> result = mitarbeiterRepository.findById(theId);

    Mitarbeiter mitarbeiter;
    mitarbeiter = result.get();

    return mitarbeiter ;
  }

  @Override
//  @Transactional
  public void save(Mitarbeiter mitarbeiter) {
      mitarbeiterRepository.save(mitarbeiter);

  }

  @Override
//  @Transactional
  public void deleteById(Long theId) {
    mitarbeiterRepository.deleteById(theId);

  }

  @Override
  @Transactional(readOnly = true)
  public List<Mitarbeiter> findMitarbeiterByVorAndNachName(String theSearchName) {
    return mitarbeiterRepository.findMitarbeiterByVorname(theSearchName);
  }

  @Override
  @Transactional(readOnly = true)
  public long count() {
    return mitarbeiterRepository.count();
  }

  @Override
  @Transactional(readOnly = true)
  public String getLatestMitarbeiterNummerPlusOne() {

    String neueMitarbeiternummer = mitarbeiterRepository.getLatestMitarbeiterNummerPlusOne();

    if (neueMitarbeiternummer == null) {
      neueMitarbeiternummer = "1";
    }

    return neueMitarbeiternummer;
  }

  @Override
  @Transactional(readOnly = true)
  public Mitarbeiter findByUserName(String tmpUser) {
    return mitarbeiterRepository.findByUsername(tmpUser);
  }

  // TODO : gut prüfen ob das geht ...
  @Override
  @Transactional
  public void updatePasswordByMitarbeiterId(Long mitarbeiterId, String encodedPassword) {
    Mitarbeiter mitarbeiter = mitarbeiterRepository.findById(mitarbeiterId).get();
    mitarbeiter.setPassword(encodedPassword);

//    mitarbeiterRepository.updatePasswordByMitarbeiterId(mitarbeiterId, encodedPassword);
  }

}
