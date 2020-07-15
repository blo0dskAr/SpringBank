package at.blo0dy.SpringBank.dao.konto;

import at.blo0dy.SpringBank.model.konto.Konto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface KontoRepository extends JpaRepository<Konto, Long> {

  @Query(value = "select count(*) from konto ko, kunde ku" +
                  " where ko.kunde_id = ku.id" +
                  "   and ku.kundennummer=?1", nativeQuery = true)
  Long countKontenGesamtByKundennummer(String kundennummer);

  @Query(value = "select count(*) from konto ko, kunde ku" +
          " where ko.kunde_id = ku.id" +
          "   and ku.kundennummer=?1" +
          "   and ko.konto_status = 'OFFEN' ", nativeQuery = true)
  Long countOffeneKontenGesamtByKundennummer(String kundennummer);

  @Query(value = " select sum(akt_saldo) from konto ko" +
          "  where ko.kunde_id = (select ku.id from kunde ku " +
          "  where ku.kundennummer= ?1 ) ", nativeQuery = true)
  BigDecimal getGesamtSaldoOffenerKontenByKundennummer(String kundennummer);

  @Query(value = "select ko.kontonummer from konto ko " +
                 " where ko.id = ?1 ", nativeQuery = true)
  String findKontonummerById(Long kontoId);

}
