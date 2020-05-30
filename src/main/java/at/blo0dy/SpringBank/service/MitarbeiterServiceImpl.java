package at.blo0dy.SpringBank.service;

import at.blo0dy.SpringBank.dao.AdresseRepository;
import at.blo0dy.SpringBank.dao.MitarbeiterRepository;
import at.blo0dy.SpringBank.dao.PersonRepository;
import at.blo0dy.SpringBank.model.person.Person;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MitarbeiterServiceImpl implements MitarbeiterService {

  private MitarbeiterRepository mitarbeiterRepository;
  private PersonRepository personRepository;

  @Autowired
  public MitarbeiterServiceImpl(MitarbeiterRepository mitarbeiterRepository, PersonRepository personRepository) {
    this.mitarbeiterRepository = mitarbeiterRepository;
    this.personRepository = personRepository ;
  }


  @Override
  @Transactional
  public List<Mitarbeiter> findAll() {
    return mitarbeiterRepository.findAll();
  }

  @Override
  @Transactional
  public Mitarbeiter findById(Long theId) {

    Optional<Mitarbeiter> result = mitarbeiterRepository.findById(theId);

    Mitarbeiter mitarbeiter;

    mitarbeiter = result.get();

    return mitarbeiter ;
  }

  @Override
  @Transactional
  public void save(Mitarbeiter mitarbeiter) {
      mitarbeiterRepository.save(mitarbeiter);
      // mitarbeiter.setPerson();
    System.out.println("Mitarbeiter: " + mitarbeiter);

  }

  @Transactional
  public void update(Mitarbeiter mitarbeiter) {
    Mitarbeiter mitarbeiterToUpdate = mitarbeiterRepository.getOne(mitarbeiter.getId());
   // mitarbeiterToUpdate.setPerson(mitarbeiter.getPerson());
    mitarbeiterRepository.save(mitarbeiterToUpdate);
  }

  @Override
  @Transactional
  public void deleteById(Long theId) {
    mitarbeiterRepository.deleteById(theId);

  }
}
