package at.blo0dy.SpringBank.service.konto.kontoBuchung;

import at.blo0dy.SpringBank.dao.konto.KontoRepository;
import at.blo0dy.SpringBank.dao.konto.kontoBuchung.KontoBuchungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class KontoBuchungServiceImpl implements KontoBuchungService {

  KontoBuchungRepository kontoBuchungRepository;

  @Autowired
  public KontoBuchungServiceImpl(KontoBuchungRepository kontoBuchungRepository) {
    this.kontoBuchungRepository = kontoBuchungRepository;
  }






}
