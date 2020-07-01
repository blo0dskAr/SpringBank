package at.blo0dy.SpringBank.dao.konto.sparen;

import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SparKontoAntragRepository extends JpaRepository<SparKontoAntrag, Long> {

  // std. impl

}
