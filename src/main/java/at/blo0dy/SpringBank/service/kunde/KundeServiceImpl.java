package at.blo0dy.SpringBank.service.kunde;

import at.blo0dy.SpringBank.dao.KundeRepository;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
//public class KundeServiceImpl implements KundeService {
public class KundeServiceImpl implements KundeService, UserDetailsService {

  private KundeRepository kundeRepository;

  @Autowired
  public KundeServiceImpl(KundeRepository KundeRepository) {
    this.kundeRepository = KundeRepository;
  }


  @Override
  @Transactional
  public List<Kunde> findAll() {
    return kundeRepository.findAll();
  }

  @Override
  @Transactional
  public Kunde findById(Long theId) {

    Optional<Kunde> result = kundeRepository.findById(theId);

    Kunde kunde;

    kunde = result.get();

    return kunde ;
  }

  @Override
  @Transactional
  public void save(Kunde kunde) {
    kundeRepository.save(kunde);
  }

  @Override
  @Transactional
  public void deleteById(Long theId) {
    kundeRepository.deleteById(theId);
  }


  //  @Override
//  public UserDetails loadUserByUsername(String username)
//          throws UsernameNotFoundException {
//    User user = userRepository.findByUsername(username);
//    if (user != null) {
//      return user;
//    }
//    throw new UsernameNotFoundException("User '" + username + "' not found");
//  }

  @Override
  public UserDetails loadUserByUsername(String kundennummer) throws UsernameNotFoundException {
    final Kunde kunde = kundeRepository.findByKundennummer(kundennummer);
//    System.out.println(kunde);
    if (kunde != null) {
      return kunde;
    }
    throw new UsernameNotFoundException("User + " + kunde.getKundennummer() + " not found");
  }

}
