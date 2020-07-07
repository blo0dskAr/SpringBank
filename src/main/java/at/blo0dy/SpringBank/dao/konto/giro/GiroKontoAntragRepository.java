package at.blo0dy.SpringBank.dao.konto.giro;

import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GiroKontoAntragRepository extends JpaRepository<GiroKontoAntrag, Long> {

  @Query(value = "select count(*) from girokontoantrag where antrags_status=?1", nativeQuery = true)
  long countByStatus(String statusEnum);

  @Query(value = "select * from girokontoantrag where antrags_status=?1", nativeQuery = true)
  List<GiroKontoAntrag> findByStatus(String statusEnum);

  @Query(value = "select * from girokontoantrag ka where ka.kundennummer = ?1", nativeQuery = true)
  List<GiroKontoAntrag> findGiroAntraegeByKundennummer(String kundennummer);

}
