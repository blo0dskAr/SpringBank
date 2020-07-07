package at.blo0dy.SpringBank.dao.konto.sparen;

import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SparKontoAntragRepository extends JpaRepository<SparKontoAntrag, Long> {

  @Query(value = "select count(*) from sparkontoantrag where antrags_status=?1", nativeQuery = true)
  long countByStatus(String statusEnum);

  @Query(value = "select * from sparkontoantrag where antrags_status=?1", nativeQuery = true)
  List<SparKontoAntrag> findByStatus(String statusEnum);

  @Query(value = "select * from sparkontoantrag ka where ka.kundennummer = ?1", nativeQuery = true)
  List<SparKontoAntrag> findSparAntraegeByKundennummer(String kundennummer);

}
