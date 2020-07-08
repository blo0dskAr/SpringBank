package at.blo0dy.SpringBank.service.konto;

public interface KontoService {

  Long countKontenGesamtByKundennummer(String kundennummer);

  Long countOffeneKontenGesamtByKundennummer(String kundennummer);

}
