package at.blo0dy.SpringBank.dao;

import at.blo0dy.SpringBank.model.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

  // StandardImpl

}
