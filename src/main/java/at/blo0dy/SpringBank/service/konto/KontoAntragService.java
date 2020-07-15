package at.blo0dy.SpringBank.service.konto;


import at.blo0dy.SpringBank.model.antrag.KontoAntrag;

import java.util.List;

public interface KontoAntragService {

  Long countAntraegeGesamtByKundennummer(String kundennummer);

  Long countOffeneAntraegeByKundennummer(String kundennummer);

  Long countAbgelehnteAntraegeByKundennummer(String kundennummer);

  Long countDurchgefuehrteAntraegeByKundennummer(String kundennummer);

  List<KontoAntrag> findAll(KontoAntrag kontoAntrag);


}
