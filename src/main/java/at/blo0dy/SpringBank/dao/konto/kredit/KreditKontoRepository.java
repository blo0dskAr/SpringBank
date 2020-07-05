package at.blo0dy.SpringBank.dao.konto.kredit;

import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import org.springframework.data.jpa.repository.JpaRepository;


public interface KreditKontoRepository extends JpaRepository<KreditKonto, Long> {

  // std. impl.

}
