package at.blo0dy.SpringBank.dao.konto.zahlungsAuftrag;

import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragStatusEnum;
import at.blo0dy.SpringBank.model.konto.dauerauftrag.DauerAuftrag;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import at.blo0dy.SpringBank.model.zv.Datentraeger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ZahlungsAuftragRepository extends JpaRepository<ZahlungsAuftrag, Long> {

  ZahlungsAuftrag save(ZahlungsAuftrag zahlungsAuftrag);

  @Query(value = "select * from zahlungs_auftrag za where za.kontonummer = ?1 ", nativeQuery = true)
  List<ZahlungsAuftrag> findZahlungsAuftraegeByKontonummer(String kontonummer);

  @Query(value ="select count(*) from zahlungs_auftrag za " +
                " where za.konto_id = ?1 " +
                "  and za.auftrags_status='ANGELEGT' ", nativeQuery = true)
  Long countOffeneZahlungsAuftraegeByKontoId(Long kontoId);

  @Query(value = "select * from zahlungs_auftrag za, konto k " +
          " where za.konto_id = ?1 " +
          " and za.auftrags_status = 'ANGELEGT' ", nativeQuery = true)
  List<ZahlungsAuftrag> findAktiveZahlungsAuftraegeByKontoId(Long kontoId);

  @Query(value = "select * from zahlungs_auftrag za " +
                 " where za.auftrags_status = 'ANGELEGT' " +
                 "   and za.auftrags_datum = ?1 " +
                 "   and za.auftrags_art = ?2 ", nativeQuery = true)
  List<ZahlungsAuftrag> findAllAngelegteZahlungsAuftraegeByDateAndType(LocalDate auftragsdatum, String type);

  @Modifying
  @Query(value ="update zahlungs_auftrag za set" +
                "       za.dat_aend = ?2 , " +
                "       za.auftrags_status = ?3 , " +
                "       za.datentraeger_id = ?4  " +
                " where za.id = ?1 ", nativeQuery = true)
  void UpdateZahlungsAuftragById(Long zahlungsAuftragId, LocalDateTime datAend, String status, Datentraeger datentraeger);

  @Modifying
  @Query(value ="update zahlungs_auftrag za set" +
          "       za.dat_aend = ?2 , " +
          "       za.auftrags_status = ?3 , " +
          " where za.id = ?1 ", nativeQuery = true)
  void UpdateZahlungsAuftragById(Long zahlungsAuftragId, LocalDateTime datAend, String status);


  int countByDatentraeger(Datentraeger datentraeger);

  @Query(value = "select sum(za.betrag) from zahlungs_auftrag za" +
                "  where za.datentraeger_id = ?1 ", nativeQuery = true)
  BigDecimal sumByDatentraeger(Datentraeger datentraeger);
}
