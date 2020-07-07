package at.blo0dy.SpringBank.dao.konto.kredit;

import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
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

}
