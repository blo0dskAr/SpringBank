package at.blo0dy.SpringBank.service.konto.giro;

import at.blo0dy.SpringBank.dao.konto.giro.GiroKontoRepository;
import at.blo0dy.SpringBank.dao.konto.sparen.SparKontoRepository;
import at.blo0dy.SpringBank.dao.sparZinsDAO.SparZinsRechnerRepository;
import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.produkt.sparen.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class GiroServiceImpl implements GiroService {

  GiroKontoRepository giroKontoRepository;

  @Autowired
  public GiroServiceImpl(GiroKontoRepository giroKontoRepository) {
    this.giroKontoRepository = giroKontoRepository;
  }

  @Override
  @Transactional
  public List<GiroKonto> findAll() {
    // ich schätz das werd ich so modifizieren müssen, dass er nur seine eigene ID's findet?
    return giroKontoRepository.findAll();
  }

  @Override
  @Transactional
  public GiroKonto findById(Long theId) {
    Optional<GiroKonto> result = giroKontoRepository.findById(theId);
    GiroKonto giroKonto;
    giroKonto = result.get();

    return giroKonto ;
  }

  @Override
  @Transactional
  public void save(GiroKonto giroKonto) {
    giroKontoRepository.save(giroKonto);
  }

  @Override
  @Transactional
  public void deleteById(Long theId) {
    giroKontoRepository.deleteById(theId);
  }

  @Override
  @Transactional
  public List<GiroKonto> findGiroKontenByKundennummer(String kundennummer) {
    return giroKontoRepository.findGiroKontenByKundennummer(kundennummer);
  }

}