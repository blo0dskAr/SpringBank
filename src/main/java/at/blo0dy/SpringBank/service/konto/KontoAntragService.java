package at.blo0dy.SpringBank.service.konto;

import org.springframework.data.jpa.repository.Query;

public interface KontoAntragService {

  Long countAntraegeGesamtByKundennummer(String kundennummer);

  Long countOffeneAntraegeByKundennummer(String kundennummer);

  Long countAbgelehnteAntraegeByKundennummer(String kundennummer);

  Long countDurchgefuehrteAntraegeByKundennummer(String kundennummer);


}
