package at.blo0dy.SpringBank.dao.konto.sparen;

import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SparKontoAntragRepository extends JpaRepository<SparKontoAntrag, Long> {

  @Query(value = "select count(*) from sparkontoantrag ska, kontoantrag ka where antrags_status=?1 and ska.id=ka.id", nativeQuery = true)
  long countByStatus(String statusEnum);

  @Query(value = "select * from sparkontoantrag ska, kontoantrag ka where ka.antrags_status=?1 and ska.id=ka.id", nativeQuery = true)
  List<SparKontoAntrag> findByStatus(String statusEnum);

  @Query(value = "select * from sparkontoantrag ska, kontoantrag ka  where ka.kundennummer = ?1 and ska.id = ka.id", nativeQuery = true)
  List<SparKontoAntrag> findSparAntraegeByKundennummer(String kundennummer);

}
