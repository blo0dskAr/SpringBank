package at.blo0dy.SpringBank.service.konto;


public interface KontoAntragService {

  Long countAntraegeGesamtByKundennummer(String kundennummer);

  Long countOffeneAntraegeByKundennummer(String kundennummer);

  Long countAbgelehnteAntraegeByKundennummer(String kundennummer);

  Long countDurchgefuehrteAntraegeByKundennummer(String kundennummer);


}
