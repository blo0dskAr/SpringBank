package at.blo0dy.SpringBank.dao.konto.dauerauftrag;

import at.blo0dy.SpringBank.model.konto.dauerauftrag.DauerAuftrag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DauerAuftragRepository extends JpaRepository<DauerAuftrag, Long> {

  @Query(value = "select count(*) from dauerauftrag da" +
                  " where da.kontonummer = ?1 " +
                  "  and da.auftrags_status = 'ANGELEGT' ", nativeQuery = true)
  Long countAktiveDauerAuftraegeByKontonummer(Long Kontonummer);


  @Query(value = "select * from dauerauftrag da " +
                  " where da.konto_id = ?1 " +
                    " and da.auftrags_status = 'ANGELEGT' ", nativeQuery = true)
  List<DauerAuftrag> findAktiveDauerAuftraegeByKontoId(Long kontoId);


}
