package at.blo0dy.SpringBank.dao.konto;

import at.blo0dy.SpringBank.model.antrag.KontoAntrag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KontoAntragRepository extends JpaRepository<KontoAntrag, Long> {

  @Query(value = "select count(*) from kontoantrag ka where ka.kundennummer=?1", nativeQuery = true)
  Long countAntraegeGesamtByKundennummer(String kundennummer);

  @Query(value = "select count(*) from kontoantrag ka where ka.kundennummer=?1" +
                  "  and ka.antrag_status like 'EINGEREICHT%'", nativeQuery = true)
  Long countOffeneAntraegeByKundennummer(String kundennummer);

  @Query(value = "select count(*) from kontoantrag ka where ka.kundennummer=?1" +
                  "  and ka.antrag_status like 'ABGELEHNT%'", nativeQuery = true)
  Long countAbgelehnteAntraegeByKundennummer(String kundennummer);

  @Query(value = "select count(*) from kontoantrag ka where ka.kundennummer=?1" +
                  "  and ka.antrag_status like 'GENEHMIGT%'", nativeQuery = true)
  Long countDurchgefuehrteAntraegeByKundennummer(String kundennummer);

}
