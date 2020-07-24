package at.blo0dy.SpringBank.dao.konto.kredit;

import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface KreditKontoRepository extends JpaRepository<KreditKonto, Long> {

  // std. impl.
  @Query(value = "select * from konto ko, kreditkonto kko " +
          "  where ko.kunde_id = (select ku.id from kunde ku " +
          "                        where ku.kundennummer= ?1 )" +
          " and ko.id = kko.id  ", nativeQuery = true)
  List<KreditKonto> findKreditKontenByKundennummer(String kundennummer);

  @Query(value = "select ko.kontonummer from konto ko, kreditkonto kko " +
          "  where ko.kunde_id = (select ku.id from kunde ku " +
          "                        where ku.kundennummer= ?1 )" +
          " and ko.id = kko.id" +
          " and ko.konto_status = 'OFFEN'  ", nativeQuery = true)
  List<String> findKontoNummerOffenerKreditKontenByKundennummer(String kundennummer);

  @Query(value ="select ko.*, kko.id as kko_kont_id, kko.kredit_betrag, kko.laufzeit, kko.rate from konto ko, kreditkonto kko " +
          " where ko.kontonummer = ?1" +
          "   and ko.kunde_id = (select ku.id from kunde ku" +
          " where ku.kundennummer = ?2)" +
          "   and ko.id = kko.id", nativeQuery = true)
  KreditKonto findKreditKontoByKontonummerAndKundennummer(String kontonummer, String kundennummer);

  KreditKonto findByKontonummer(Long tmpKontonummer);

  @Query(value = "select count(*) from kreditkonto kko, konto ko " +
          " where kko.id = ko.id" +
          "   and ko.kunde_id = ?1" +
          "  and ko.konto_status = 'OFFEN' ", nativeQuery = true)
  int countAktiveKontenByKundeId(Long kundeId);
}
