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


  @Query(value = "select * from dauerauftrag da " +
          " where da.auftrags_status = 'ANGELEGT' " +
          "   and da.tag_im_monat = ?1 " +
          "   and da.auftrags_art = ?2 ", nativeQuery = true)
  List<DauerAuftrag> findAllAngelegteDauerAuftraegeByDateAndType(int tagImMonat, String type);


//  @Modifying
//  @Query(value = "update dauerauftrag da set " +
//                    "    da.auftrags_status = 'STORNIERT'" +
//                  " where da.id = ?1 ", nativeQuery = true)
//  void storniereDauerAuftragById(Long dauerAuftragId);
}
