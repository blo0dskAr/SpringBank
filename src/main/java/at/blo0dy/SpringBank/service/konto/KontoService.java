package at.blo0dy.SpringBank.service.konto;


import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;

import java.math.BigDecimal;
import java.util.List;

public interface KontoService {

  Long countKontenGesamtByKundennummer(String kundennummer);

  Long countOffeneKontenGesamtByKundennummer(String kundennummer);

  BigDecimal getGesamtSaldoOffenerKontenByKundennummer(String kundennummer);

  String findKontonummerById(Long kontoId);

//  Konto findByKontonummer(Long kontonummer);

  List<Konto> findAll(Konto konto);

  Konto findById(Long kontoId);

  void UpdateKontoSaldoById(Long kontoId, BigDecimal neuerSaldo);

  String processKontoStatusById(Long kontoId, KontoStatusEnum neuerKontoStatus, KontoStatusEnum bestMoeglicherStatus);

}
