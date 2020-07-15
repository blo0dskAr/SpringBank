package at.blo0dy.SpringBank.service.konto.kontoBuchung;


import at.blo0dy.SpringBank.model.konto.kontoBuchung.KontoBuchung;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;

import java.util.List;

public interface KontoBuchungService {

  List<KontoBuchung> findByKontoId(Long kontoId);

  void save(KontoBuchung kontoBuchung);


}
