package at.blo0dy.SpringBank.service.konto;


import java.math.BigDecimal;

public interface KontoService {

  Long countKontenGesamtByKundennummer(String kundennummer);

  Long countOffeneKontenGesamtByKundennummer(String kundennummer);

  BigDecimal getGesamtSaldoOffenerKontenByKundennummer(String kundennummer);
}
