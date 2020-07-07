package at.blo0dy.SpringBank.service.konto.giro;

import at.blo0dy.SpringBank.dao.konto.giro.GiroKontoAntragRepository;
import at.blo0dy.SpringBank.dao.konto.sparen.SparKontoAntragRepository;
import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class GiroKontoAntragServiceImpl implements GiroKontoAntragService {

  GiroKontoAntragRepository giroKontoAntragRepository;

  @Autowired
  public GiroKontoAntragServiceImpl(GiroKontoAntragRepository giroKontoAntragRepository) {
    this.giroKontoAntragRepository = giroKontoAntragRepository;
  }

  @Override
  @Transactional
  public List<GiroKontoAntrag> findAll() {
    // TODO: ich schätz das werd ich so modifizieren müssen, dass er nur seine eigene ID's findet?
    return giroKontoAntragRepository.findAll();
  }

  @Override
  @Transactional
  public GiroKontoAntrag findById(Long theId) {
    Optional<GiroKontoAntrag> result = giroKontoAntragRepository.findById(theId);

    GiroKontoAntrag giroKontoAntrag;

    giroKontoAntrag = result.get();

    return giroKontoAntrag ;
  }

  @Override
  @Transactional
  public void save(GiroKontoAntrag giroKontoAntrag) {
    giroKontoAntragRepository.save(giroKontoAntrag);
  }

  @Override
  @Transactional
  public void deleteById(Long theId) {
    giroKontoAntragRepository.deleteById(theId);
  }

  @Override
  @Transactional
  public long count() {
    return giroKontoAntragRepository.count();
  }

  @Override
  @Transactional
  public long countByStatus(String statusEnum) {
    return giroKontoAntragRepository.countByStatus(statusEnum);
  }

  public List<GiroKontoAntrag> findByStatus(String statusEnum) {
    List<GiroKontoAntrag> giroKontoAntragListe = giroKontoAntragRepository.findByStatus(statusEnum) ;
    return  giroKontoAntragListe;
  }

  @Override
  @Transactional
  public List<GiroKontoAntrag> findGiroAntraegeByKundennummer(String kundennummer) {
    return giroKontoAntragRepository.findGiroAntraegeByKundennummer(kundennummer);

  }

}
