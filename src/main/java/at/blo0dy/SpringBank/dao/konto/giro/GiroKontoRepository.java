package at.blo0dy.SpringBank.dao.konto.giro;

import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface GiroKontoRepository extends JpaRepository<GiroKonto, Long> {

  // std. impl.

  @Query(value = "select * from konto ko, girokonto gko " +
          "  where ko.kunde_id = (select ku.id from kunde ku " +
          "                        where ku.kundennummer= ?1 )" +
          " and ko.id = gko.id  ", nativeQuery = true)
  List<GiroKonto> findGiroKontenByKundennummer(String kundennummer);

  @Query(value = "select ko.kontonummer from konto ko, girokonto gko " +
          "  where ko.kunde_id = (select ku.id from kunde ku " +
          "                        where ku.kundennummer= ?1 )" +
          " and ko.id = gko.id" +
          " and ko.konto_status = 'OFFEN'  ", nativeQuery = true)
  List<String> findKontoNummerOffenerGiroKontenByKundennummer(String kundennummer);

  @Query(value ="select ko.*, gko.id as gko_kont_id, gko.ueberziehungs_rahmen from konto ko, girokonto gko " +
          " where ko.kontonummer = ?1" +
          "   and ko.kunde_id = (select ku.id from kunde ku" +
          " where ku.kundennummer = ?2)" +
          "   and ko.id = gko.id", nativeQuery = true)
  GiroKonto findGiroKontoByKontonummerAndKundennummer(String kontonummer, String kundennummer);

  GiroKonto findByKontonummer(long kontonummer);
}
