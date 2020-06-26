package at.blo0dy.SpringBank.service.kunde;

import at.blo0dy.SpringBank.dao.KundeRepository;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
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
//  public UserDetails loadUserByKundennummer(String kundennummer) throws UsernameNotFoundException {
//    final Kunde kunde = kundeRepository.findByKundennummer(kundennummer);
//    if (kunde == null) {
//      throw new UsernameNotFoundException(kundennummer);
//    }
//    UserDetails user = User.withUsername(kunde.getKundennummer()).password(kunde.getPassword()).authorities(kunde.getRolle()).build();
//    return user;
//  }

  @Override
  public UserDetails loadUserByUsername(String kundennummer) throws UsernameNotFoundException {
    final Kunde kunde = kundeRepository.findByKundennummer(kundennummer);
    if (kunde == null) {
      throw new UsernameNotFoundException(kundennummer);
    }
    UserDetails user = User.withUsername(kunde.getKundennummer()).password(kunde.getPassword()).authorities(kunde.getRolle()).build();
    return user;
  }


//  @Override
//  public Kunde loadUserByKundennummer(String kundennummer)
//          throws UsernameNotFoundException {
//    Kunde kunde = kundeRepository.findByKundennummer(kundennummer);
//    if (kunde != null) {
//      return kunde;
//    }
//    throw new UsernameNotFoundException("Kunde: " + kundennummer + " not found");
//
//  }

//  @Override
//  public UserDetails loadUserByKundennummer(String kundennummer) throws UsernameNotFoundException {
//    Kunde kunde = kundeRepository.findByKundennummer(s);
//    if (user == null) {
//      throw new UsernameNotFoundException(username);
//    }
//    return new MyUserPrincipal(user);
//  }
}
