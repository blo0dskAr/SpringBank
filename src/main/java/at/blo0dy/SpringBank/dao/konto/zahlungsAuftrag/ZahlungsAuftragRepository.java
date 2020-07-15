package at.blo0dy.SpringBank.dao.konto.zahlungsAuftrag;

import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ZahlungsAuftragRepository extends JpaRepository<ZahlungsAuftrag, Long> {

  ZahlungsAuftrag save(ZahlungsAuftrag zahlungsAuftrag);

  @Query(value = "select * from zahlungs_auftrag za where za.kontonummer = ?1 ", nativeQuery = true)
  List<ZahlungsAuftrag> findZahlungsAuftraegeByKontonummer(String kontonummer);

  @Query(value ="select count(*) from zahlungs_auftrag za " +
                " where za.id = ?1 " +
                "  and za.auftrags_status='ANGELEGT' ", nativeQuery = true)
  Long countOffeneZahlungsAuftraegeByKontoId(Long kontoId);

}
