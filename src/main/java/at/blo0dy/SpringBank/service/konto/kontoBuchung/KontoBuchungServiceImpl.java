package at.blo0dy.SpringBank.service.konto.kontoBuchung;

import at.blo0dy.SpringBank.dao.konto.kontoBuchung.KontoBuchungRepository;
import at.blo0dy.SpringBank.model.konto.kontoBuchung.KontoBuchung;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class KontoBuchungServiceImpl implements KontoBuchungService {

  KontoBuchungRepository kontoBuchungRepository;

  @Autowired
  public KontoBuchungServiceImpl(KontoBuchungRepository kontoBuchungRepository) {
    this.kontoBuchungRepository = kontoBuchungRepository;
  }

  @Override
  @Transactional
  public List<KontoBuchung> findByKontoId(Long kontoId) {
    log.debug("findByKontoId für KontoId: " + kontoId + " wird durchgeführt");
    return kontoBuchungRepository.findByKontoIdOrderByIdDesc(kontoId);
  }

  @Override
  @Transactional
  public void save(KontoBuchung kontoBuchung) {
    kontoBuchungRepository.save(kontoBuchung);
  }


}
