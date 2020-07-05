package at.blo0dy.SpringBank.dao.konto.kredit;

import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KreditKontoAntragRepository extends JpaRepository<KreditKontoAntrag, Long> {

  @Query(value = "select count(*) from kreditkontoantrag where antrags_status=?1", nativeQuery = true)
  long countByStatus(String statusEnum);

  @Query(value = "select * from kreditkontoantrag where antrags_status=?1", nativeQuery = true)
  List<KreditKontoAntrag> findByStatus(String statusEnum);

}
