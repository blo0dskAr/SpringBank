package at.blo0dy.SpringBank.dao.konto.kredit;

import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KreditKontoAntragRepository extends JpaRepository<KreditKontoAntrag, Long> {

  @Query(value = "select count(*) from kreditkontoantrag kka, kontoantrag ka where ka.antrag_status=?1 and ka.id = kka.id", nativeQuery = true)
  long countByStatus(String statusEnum);

  @Query(value = "select * from kreditkontoantrag kka, kontoantrag ka where ka.antrag_status=?1 and ka.id = kka.id", nativeQuery = true)
  List<KreditKontoAntrag> findByStatus(String statusEnum);

/*  @Modifying
  @Query(value = "update kontoantrag ka set" +
          "  ka.antrag_status = 'ABGELEHNT_WEIL_NEU_BERECHNET'" +
          "  where ka.id = ?1 ;",
          nativeQuery = true)
  void setKreditAntragAbgelehntWeilNeuBerechnetById(Long kreditKontoAntragId);*/

  @Query(value = "select * from kreditkontoantrag kka, kontoantrag ka where ka.kundennummer = ?1 and kka.id = ka.id", nativeQuery = true)
  List<KreditKontoAntrag> findKreditAntraegeByKundennummer(String kundennummer);

  KreditKontoAntrag getOne(Long aLong);

  @Query(value = "select * from kreditkontoantrag kka, kontoantrag ka " +
          " where kka.id = ka.id" +
          "   and kka.id = ?1 " +
          "   and ka.kundennummer = ?2", nativeQuery = true)
  KreditKontoAntrag findKreditAntragByAntragIdAndKundennummer(Long antragId, String kundennummer);

  @Query( value = "select * from kreditkontoantrag kka, kontoantrag ka" +
          " where kka.id = ka.id" +
          "   and exists (select 1 from konto k where k.konto_antrag_id = ka.id and k.id = ?1)", nativeQuery = true)
  KreditKontoAntrag findByKontoId(Long kontoId);

  @Query(value = "select count(*) from kreditkontoantrag kka, kontoantrag ka " +
          "  where kka.id = ka.id" +
          "  and ka.antrag_status = 'EINGEREICHT'" +
          "  and ka.kundennummer = ?1 ", nativeQuery = true)
  int countEingereichteKreditAntraegeByKundennummer(String kundennummer);
}

