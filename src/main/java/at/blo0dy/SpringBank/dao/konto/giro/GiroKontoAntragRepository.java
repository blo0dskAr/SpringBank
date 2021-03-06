package at.blo0dy.SpringBank.dao.konto.giro;

import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GiroKontoAntragRepository extends JpaRepository<GiroKontoAntrag, Long> {

  @Query(value = "select count(*) from girokontoantrag gka, kontoantrag ka where antrag_status=?1 and ka.id = gka.id", nativeQuery = true)
  long countByStatus(String statusEnum);

  @Query(value = "select * from girokontoantrag gka, kontoantrag ka where antrag_status=?1 and ka.id = gka.id", nativeQuery = true)
  List<GiroKontoAntrag> findByStatus(String statusEnum);

  @Query(value = "select * from girokontoantrag gka, kontoantrag ka where ka.kundennummer = ?1 and ka.id = gka.id", nativeQuery = true)
  List<GiroKontoAntrag> findGiroAntraegeByKundennummer(String kundennummer);

  @Query(value = "select * from girokontoantrag gka, kontoantrag ka " +
          " where gka.id = ka.id" +
          "   and gka.id = ?1 " +
          "   and ka.kundennummer = ?2", nativeQuery = true)
  GiroKontoAntrag findGiroAntragByAntragIdAndKundennummer(Long antragId, String kundennummer);


//  @Query(value = "select * from girokontoantrag gka, kontoantrag ka " +
//                  " where gka.id = ka.id" +
//                  "  and ka.konto_id = ?1" ,nativeQuery = true)

  @Query( value = "select * from girokontoantrag gka, kontoantrag ka" +
                  " where gka.id = ka.id" +
                  "   and exists (select 1 from konto k where k.konto_antrag_id = ka.id and k.id = ?1)", nativeQuery = true)
  GiroKontoAntrag findByKontoId(Long kontoId);


  @Query(value = "select count(*) from girokontoantrag gka, kontoantrag ka " +
          "  where gka.id = ka.id" +
          "  and ka.antrag_status = 'EINGEREICHT'" +
          "  and ka.kundennummer = ?1 ", nativeQuery = true)
  int countEingereichteGiroAntraegeByKundennummer(String kundennummer);
}
