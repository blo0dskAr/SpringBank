package at.blo0dy.SpringBank.service.konto;

import at.blo0dy.SpringBank.dao.konto.KontoAntragRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KontoAntragServiceImpl implements KontoAntragService {

  KontoAntragRepository kontoAntragRepository;


  @Autowired
  public KontoAntragServiceImpl(KontoAntragRepository kontoAntragRepository) {
    this.kontoAntragRepository = kontoAntragRepository;
  }

  @Override
  @Transactional
  public Long countAntraegeGesamtByKundennummer(String kundennummer) {
    return kontoAntragRepository.countAntraegeGesamtByKundennummer(kundennummer);
  }

  @Override
  @Transactional
  public Long countOffeneAntraegeByKundennummer(String kundennummer) {
    return kontoAntragRepository.countOffeneAntraegeByKundennummer(kundennummer);
  }

  @Override
  @Transactional
  public Long countAbgelehnteAntraegeByKundennummer(String kundennummer) {
    return kontoAntragRepository.countAbgelehnteAntraegeByKundennummer(kundennummer);
  }

  @Override
  @Transactional
  public Long countDurchgefuehrteAntraegeByKundennummer(String kundennummer) {
    return kontoAntragRepository.countDurchgefuehrteAntraegeByKundennummer(kundennummer);
  }


}
