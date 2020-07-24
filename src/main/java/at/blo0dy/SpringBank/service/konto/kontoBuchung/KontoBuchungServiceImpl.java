package at.blo0dy.SpringBank.service.konto.kontoBuchung;

import at.blo0dy.SpringBank.dao.konto.kontoBuchung.KontoBuchungRepository;
import at.blo0dy.SpringBank.model.enums.BuchungsArtEnum;
import at.blo0dy.SpringBank.model.konto.kontoBuchung.KontoBuchung;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
  @Transactional(readOnly = true)
  public List<KontoBuchung> findByKontoId(Long kontoId) {
    log.debug("findByKontoId für KontoId: " + kontoId + " wird durchgeführt");
    return kontoBuchungRepository.findByKontoIdOrderByIdDesc(kontoId);
  }

  @Override
//  @Transactional
  public void save(KontoBuchung kontoBuchung) {
    kontoBuchungRepository.save(kontoBuchung);
  }

  @Override
  public BigDecimal saveNewKontoBuchung(KontoBuchung kontoBuchung) {

    kontoBuchung.setDatAnlage(LocalDateTime.now());

    if (kontoBuchung.getBuchungsArt().equals(BuchungsArtEnum.HABEN)) {
      kontoBuchung.setSaldoNachBuchung(kontoBuchung.getKonto().getAktSaldo().add(kontoBuchung.getBuchungsBetrag()));
    } else {
      kontoBuchung.setSaldoNachBuchung(kontoBuchung.getKonto().getAktSaldo().subtract(kontoBuchung.getBuchungsBetrag()));
    }

    kontoBuchungRepository.save(kontoBuchung);

    return kontoBuchung.getSaldoNachBuchung();

  }
}
