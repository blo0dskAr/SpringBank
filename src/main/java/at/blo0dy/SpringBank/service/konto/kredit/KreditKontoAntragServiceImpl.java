package at.blo0dy.SpringBank.service.konto.kredit;

import at.blo0dy.SpringBank.dao.konto.kredit.KreditKontoAntragRepository;
import at.blo0dy.SpringBank.model.antrag.KontoAntrag;
import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import at.blo0dy.SpringBank.model.enums.AntragStatusEnum;
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
  @Transactional(readOnly = true)
  public List<KreditKontoAntrag> findAll() {
    return kreditKontoAntragRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public KreditKontoAntrag findById(Long theId) {
    Optional<KreditKontoAntrag> result = kreditKontoAntragRepository.findById(theId);

    KreditKontoAntrag kreditKontoAntrag;

    kreditKontoAntrag = result.get();

    return kreditKontoAntrag ;
  }

  @Override
//  @Transactional
  public void save(KreditKontoAntrag kreditKontoAntrag) {
    kreditKontoAntragRepository.save(kreditKontoAntrag);
  }

  @Override
//  @Transactional
  public void deleteById(Long theId) {
    kreditKontoAntragRepository.deleteById(theId);
  }

  @Override
  @Transactional(readOnly = true)
  public KreditKontoAntrag getOne(Long aLong) {
    return kreditKontoAntragRepository.getOne(aLong);
  }

  @Override
  @Transactional(readOnly = true)
  public long count() {
    return kreditKontoAntragRepository.count();
  }

  @Override
  @Transactional(readOnly = true)
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

    KontoAntrag kontoAntrag = kreditKontoAntragRepository.findById(kreditKontoAntragId).get();

    kontoAntrag.setAntragStatus(AntragStatusEnum.ABGELEHNT_WEIL_NEU_BERECHNET);

//    kreditKontoAntragRepository.setKreditAntragAbgelehntWeilNeuBerechnetById(kreditKontoAntragId);

  }

  @Override
  @Transactional(readOnly = true)
  public List<KreditKontoAntrag> findKreditAntraegeByKundennummer(String kundennummer) {
    return kreditKontoAntragRepository.findKreditAntraegeByKundennummer(kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public KreditKontoAntrag findKreditAntragByAntragIdAndKundennummer(Long antragId, String kundennummer) {
    return kreditKontoAntragRepository.findKreditAntragByAntragIdAndKundennummer(antragId, kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public int countEingereichteKreditAntraegeByKundennummer(String kundennummer) {
    return kreditKontoAntragRepository.countEingereichteKreditAntraegeByKundennummer(kundennummer);
  }


}
