package at.blo0dy.SpringBank.service.kunde;

import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface KundeService   {

  public List<Kunde> findAll();

  public Kunde findById(Long theId);

  public void save(Kunde kunde);

  public void deleteById(Long theId);

  //UserDetails loadUserByUsername(String kundennummer);
}
