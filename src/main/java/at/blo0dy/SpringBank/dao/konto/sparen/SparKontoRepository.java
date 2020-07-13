package at.blo0dy.SpringBank.dao.konto.sparen;

import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface SparKontoRepository extends JpaRepository<SparKonto, Long> {

  // std. impl.

  @Query(value = "select * from konto ko, sparkonto sko " +
          "  where ko.kunde_id = (select ku.id from kunde ku " +
          "                        where ku.kundennummer= ?1 )" +
          " and ko.id = sko.id  ", nativeQuery = true)
  List<SparKonto> findSparKontoByKundennummer(String kundennummer);

  @Query(value = "select ko.kontonummer from konto ko, sparkonto sko " +
          "  where ko.kunde_id = (select ku.id from kunde ku " +
          "                        where ku.kundennummer= ?1 )" +
          " and ko.id = sko.id" +
          " and ko.konto_status = 'OFFEN'  ", nativeQuery = true)
  List<String> findKontoNummerOffenerSparKontenByKundennummer(String kundennummer);

  @Query(value ="select ko.*, sko.id as sko_kont_id, sko.connected_giro from konto ko, sparkonto sko " +
          " where ko.kontonummer = ?1" +
          "   and ko.kunde_id = (select ku.id from kunde ku" +
          " where ku.kundennummer = ?2)" +
          "   and ko.id = sko.id", nativeQuery = true)
  SparKonto findSparKontoByKontonummerAndKundennummer(String kontonummer, String kundennummer);

}
