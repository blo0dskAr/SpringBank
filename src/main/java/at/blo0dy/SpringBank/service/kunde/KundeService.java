package at.blo0dy.SpringBank.service.kunde;

import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface KundeService   {

  List<Kunde> findAll();

  Kunde findById(Long theId);

  void save(Kunde kunde);

  void deleteById(Long theId);

  //UserDetails loadUserByUsername(String kundennummer);
}
