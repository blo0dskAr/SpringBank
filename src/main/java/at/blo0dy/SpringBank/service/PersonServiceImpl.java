package at.blo0dy.SpringBank.service;

import at.blo0dy.SpringBank.dao.PersonRepository;
import at.blo0dy.SpringBank.model.person.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

  private PersonRepository personRepository;

  @Autowired
  public PersonServiceImpl(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  @Override
  @Transactional
  public List<Person> findAll() {
    return personRepository.findAll();
  }

  @Override
  @Transactional
  public Person findById(Long theId) {

    Optional<Person> result = personRepository.findById(theId);

    Person person;

    person = result.get();

    return person ;
  }

  @Override
  @Transactional
  public void save(Person person) {
      personRepository.save(person);
  }

  @Override
  @Transactional
  public void deleteById(Long theId) {
    personRepository.deleteById(theId);

  }

}
