package at.blo0dy.SpringBank.dao.konto.sparen;

import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SparKontoRepository extends JpaRepository<SparKonto, Long> {

  // std. impl.

}
