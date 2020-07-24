package at.blo0dy.SpringBank.service.konto;

import at.blo0dy.SpringBank.dao.konto.KontoAntragRepository;
import at.blo0dy.SpringBank.model.antrag.KontoAntrag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KontoAntragServiceImpl implements KontoAntragService {

  KontoAntragRepository kontoAntragRepository;


  @Autowired
  public KontoAntragServiceImpl(KontoAntragRepository kontoAntragRepository) {
    this.kontoAntragRepository = kontoAntragRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public Long countAntraegeGesamtByKundennummer(String kundennummer) {
    return kontoAntragRepository.countAntraegeGesamtByKundennummer(kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public Long countOffeneAntraegeByKundennummer(String kundennummer) {
    return kontoAntragRepository.countOffeneAntraegeByKundennummer(kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public Long countAbgelehnteAntraegeByKundennummer(String kundennummer) {
    return kontoAntragRepository.countAbgelehnteAntraegeByKundennummer(kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public Long countDurchgefuehrteAntraegeByKundennummer(String kundennummer) {
    return kontoAntragRepository.countDurchgefuehrteAntraegeByKundennummer(kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public List<KontoAntrag> findAll(KontoAntrag kontoAntrag) {
    return kontoAntragRepository.findAll(Example.of(kontoAntrag));
  }


}
