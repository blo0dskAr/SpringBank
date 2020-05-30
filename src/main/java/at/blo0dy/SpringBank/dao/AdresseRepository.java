package at.blo0dy.SpringBank.dao;

import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdresseRepository extends JpaRepository<Adresse, Long> {

  // standardimpl

}
