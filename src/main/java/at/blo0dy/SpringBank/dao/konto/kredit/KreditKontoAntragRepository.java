package at.blo0dy.SpringBank.dao.konto.kredit;

import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KreditKontoAntragRepository extends JpaRepository<KreditKontoAntrag, Long> {

  @Query(value = "select count(*) from kreditkontoantrag where antrags_status=?1", nativeQuery = true)
  long countByStatus(String statusEnum);

  @Query(value = "select * from kreditkontoantrag where antrags_status=?1", nativeQuery = true)
  List<KreditKontoAntrag> findByStatus(String statusEnum);

  @Modifying
  @Query(value ="update kreditkontoantrag ka set" +
                "  ka.antrags_status = 'ABGELEHNT_WEIL_NEU_BERECHNET'" +
                "  where ka.id = ?1 ;",
                nativeQuery = true)
  void setKreditAntragAbgelehntWeilNeuBerechnetById(Long kreditKontoAntragId);

}
