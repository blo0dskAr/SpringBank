package at.blo0dy.SpringBank.service.konto;

import at.blo0dy.SpringBank.model.konto.Konto;

import java.math.BigDecimal;
import java.util.List;

public interface KontoService {

  Long countKontenGesamtByKundennummer(String kundennummer);

  Long countOffeneKontenGesamtByKundennummer(String kundennummer);

  BigDecimal getGesamtSaldoOffenerKontenByKundennummer(String kundennummer);
}
