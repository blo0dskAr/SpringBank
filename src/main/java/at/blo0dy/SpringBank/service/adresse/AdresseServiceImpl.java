package at.blo0dy.SpringBank.service.adresse;

import at.blo0dy.SpringBank.dao.AdresseRepository;
import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class AdresseServiceImpl implements AdresseService {

  AdresseRepository adresseRepository;

  @Autowired
  public AdresseServiceImpl(AdresseRepository adresseRepository) {
    this.adresseRepository = adresseRepository;
  }

  @Override
  @Transactional
  public List<Adresse> findAll() {
    return adresseRepository.findAll();
  }

  @Override
  @Transactional
  public Adresse findById(Long theId) {
    Optional<Adresse> result = adresseRepository.findById(theId);

    Adresse adresse;

    adresse = result.get();

    return adresse ;
  }

  @Override
  @Transactional
  public void save(Adresse adresse) {
    adresseRepository.save(adresse) ;

  }

  @Override
  @Transactional
  public void deleteById(Long theId) {
    adresseRepository.deleteById(theId);

  }
}
