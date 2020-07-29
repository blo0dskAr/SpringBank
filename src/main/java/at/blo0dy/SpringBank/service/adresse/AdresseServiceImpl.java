package at.blo0dy.SpringBank.service.adresse;

import at.blo0dy.SpringBank.dao.person.AdresseRepository;
import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AdresseServiceImpl implements AdresseService {

  AdresseRepository adresseRepository;

  @Autowired
  public AdresseServiceImpl(AdresseRepository adresseRepository) {
    this.adresseRepository = adresseRepository;
  }

  // TODO: Löschen wenn nicht gebraucht
  @Override
  @Transactional(readOnly = true)
  public List<Adresse> findAll() {
    log.debug("Adressen werden gesucht");

    return adresseRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Adresse findById(Long theId) {
    log.debug("Adresse mit ID: " + theId + " wird gesucht.");

    Optional<Adresse> result = adresseRepository.findById(theId);
    Adresse adresse;
    adresse = result.get();

    log.debug("Folgende Adresse für ID: " + theId + " wurde gefunden: (" + adresse + ")");
    return adresse ;
  }

  @Override
//  @Transactional
  public void save(Adresse adresse) {
    log.debug("Adresse(" + adresse + ") wird gespeichert.");

    adresseRepository.save(adresse) ;
  }

  // TODO: löschen wenn nicht gebraucht"
  @Override
//  @Transactional
  public void deleteById(Long theId) {
    log.debug("Adresse mit ID: " + theId + " wird gelöscht.");

    adresseRepository.deleteById(theId);
  }
}
