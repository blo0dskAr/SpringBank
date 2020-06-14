package at.blo0dy.SpringBank.dao;

import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface KundeRepository extends JpaRepository<Kunde, Long> {

  Kunde findByKundennummer(String kundennummer);

}
