package at.blo0dy.SpringBank.service.konto.kredit;

import at.blo0dy.SpringBank.dao.konto.kredit.KreditKontoAntragRepository;
import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class KreditKontoAntragServiceImpl implements KreditKontoAntragService {

  KreditKontoAntragRepository kreditKontoAntragRepository;

  @Autowired
  public KreditKontoAntragServiceImpl(KreditKontoAntragRepository kreditKontoAntragRepository) {
    this.kreditKontoAntragRepository = kreditKontoAntragRepository;
  }

  @Override
  @Transactional
  public List<KreditKontoAntrag> findAll() {
    // ich schätz das werd ich so modifizieren müssen, dass er nur seine eigene ID's findet?
    return kreditKontoAntragRepository.findAll();
  }

  @Override
  @Transactional
  public KreditKontoAntrag findById(Long theId) {
    Optional<KreditKontoAntrag> result = kreditKontoAntragRepository.findById(theId);

    KreditKontoAntrag kreditKontoAntrag;

    kreditKontoAntrag = result.get();

    return kreditKontoAntrag ;
  }

  @Override
  @Transactional
  public void save(KreditKontoAntrag kreditKontoAntrag) {
    kreditKontoAntragRepository.save(kreditKontoAntrag);
  }

  @Override
  @Transactional
  public void deleteById(Long theId) {
    kreditKontoAntragRepository.deleteById(theId);
  }

  @Override
  @Transactional
  public long count() {
    return kreditKontoAntragRepository.count();
  }

  @Override
  public long countByStatus(String statusEnum) {
    return kreditKontoAntragRepository.countByStatus(statusEnum);
  }

  public List<KreditKontoAntrag> findByStatus(String statusEnum) {
    List<KreditKontoAntrag> kreditKontoAntragListe = kreditKontoAntragRepository.findByStatus(statusEnum) ;
    return  kreditKontoAntragListe;
  }

  @Override
  public boolean compareBasicKreditData(KreditKontoAntrag antrag1, KreditKontoAntrag antrag2) {
    if (antrag1.getKreditBetrag().equals(antrag2.getKreditBetrag()) && antrag1.getZinssatz().equals(antrag2.getZinssatz()) && antrag1.getLaufzeit().equals(antrag2.getLaufzeit())) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  @Transactional
  public void setKreditAntragAbgelehntWeilNeuBerechnetById(Long kreditKontoAntragId) {

    kreditKontoAntragRepository.setKreditAntragAbgelehntWeilNeuBerechnetById(kreditKontoAntragId);

  }



}
