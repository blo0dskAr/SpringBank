package at.blo0dy.SpringBank.service;

import at.blo0dy.SpringBank.dao.KundeRepository;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class KundeServiceImpl implements KundeService {

  private KundeRepository KundeRepository;

  @Autowired
  public KundeServiceImpl(KundeRepository KundeRepository) {
    this.KundeRepository = KundeRepository;
  }


  @Override
  @Transactional
  public List<Kunde> findAll() {
    return KundeRepository.findAll();
  }

  @Override
  @Transactional
  public Kunde findById(Long theId) {

    Optional<Kunde> result = KundeRepository.findById(theId);

    Kunde kunde;

    kunde = result.get();

    return kunde ;
  }

  @Override
  @Transactional
  public void save(Kunde kunde) {
    KundeRepository.save(kunde);
    System.out.println("MAServiceImpl:Mitarbeiter: " + kunde);
  }

  @Override
  @Transactional
  public void deleteById(Long theId) {
    KundeRepository.deleteById(theId);

  }
}
