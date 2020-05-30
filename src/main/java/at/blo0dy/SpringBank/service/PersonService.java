package at.blo0dy.SpringBank.service;

import at.blo0dy.SpringBank.model.person.Person;

import java.util.List;

public interface PersonService {

  public List<Person> findAll();

  public Person findById(Long theId);

  public void save(Person person);

  public void deleteById(Long theId);

}
