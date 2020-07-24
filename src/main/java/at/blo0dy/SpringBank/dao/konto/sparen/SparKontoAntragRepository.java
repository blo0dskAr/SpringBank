package at.blo0dy.SpringBank.dao.konto.sparen;

import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SparKontoAntragRepository extends JpaRepository<SparKontoAntrag, Long> {

  @Query(value = "select count(*) from sparkontoantrag ska, kontoantrag ka where antrag_status=?1 and ska.id=ka.id", nativeQuery = true)
  long countByStatus(String statusEnum);

  @Query(value = "select * from sparkontoantrag ska, kontoantrag ka where ka.antrag_status=?1 and ska.id=ka.id", nativeQuery = true)
  List<SparKontoAntrag> findByStatus(String statusEnum);

  @Query(value = "select * from sparkontoantrag ska, kontoantrag ka  where ka.kundennummer = ?1 and ska.id = ka.id", nativeQuery = true)
  List<SparKontoAntrag> findSparAntraegeByKundennummer(String kundennummer);

  @Query(value = "select * from sparkontoantrag ska, kontoantrag ka " +
                 " where ska.id = ka.id" +
                 "   and ska.id = ?1 " +
                 "   and ka.kundennummer = ?2", nativeQuery = true)
  SparKontoAntrag findSparAntragByAntragIdAndKundennummer(Long antragId, String authKundennummer);

  @Query( value = "select * from sparkontoantrag ska, kontoantrag ka" +
          " where ska.id = ka.id" +
          "   and exists (select 1 from konto k where k.konto_antrag_id = ka.id and k.id = ?1)", nativeQuery = true)
  SparKontoAntrag findByKontoId(Long kontoId);

  @Query(value = "select count(*) from sparkontoantrag ska, kontoantrag ka " +
                "  where ska.id = ka.id" +
                  "  and ka.antrag_status = 'EINGEREICHT'" +
                  "  and ka.kundennummer = ?1 ", nativeQuery = true)
  int countEingereichteSparAntraegeByKundennummer(String kundennummer);
}
