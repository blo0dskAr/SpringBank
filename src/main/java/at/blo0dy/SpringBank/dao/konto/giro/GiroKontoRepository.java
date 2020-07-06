package at.blo0dy.SpringBank.dao.konto.giro;

import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GiroKontoRepository extends JpaRepository<GiroKonto, Long> {

  // std. impl.

}
