package at.blo0dy.SpringBank.dao.konto.kontoBuchung;

import at.blo0dy.SpringBank.model.konto.kontoBuchung.KontoBuchung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KontoBuchungRepository extends JpaRepository<KontoBuchung, Long> {


  List<KontoBuchung> findByKontoIdOrderByIdDesc(Long kontoId);


}
