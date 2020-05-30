package at.blo0dy.SpringBank.dao;

import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MitarbeiterRepository extends JpaRepository<Mitarbeiter, Long> {

  // Standard JpaRepository Implementation


}
