package at.blo0dy.SpringBank.dao;

import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface KundeRepository extends JpaRepository<Kunde, Long> {

  Kunde findByKundennummer(String kundennummer);

  @Query(value="select max(kontonummer) from kunde ku, konto ko" +
                " where ko.kunde_id = ku.id" +
                "   and ku.kundennummer = ?1 ;",
                nativeQuery = true)
  Long getLatestKontonummerByKundennummer(String kundennummer);


  @Query (value ="select max((ku.kundennummer)+1) from kunde ku  ",
          nativeQuery = true)
  Long getLatestKundennummerPlusOne();

  @Query(value = "select ku.connected_giro from kunde ku where ku.kundennummer = ?1", nativeQuery = true)
  String getConnectedGiroByKundennummer(String kundennummer);

  @Modifying
  @Query(value ="update kunde ku set" +
                " ku.email_adresse = ?2, " +
                " ku.telefon_nummer = ?3, " +
                " ku.connected_giro = ?4, " +
                " ku.has_acceptedagb = ?5 " +
          "where ku.kundennummer = ?1 " , nativeQuery = true)
  void updateChangeableDataByKundennummer(String kundennummer, String email, String tel, String connectedGiro, boolean hasAcceptedAGB);


  @Modifying
  @Query(value = "update kunde ku set " +
                 "       ku.is_legi = ?2" +
                "  where ku.id = ?1 ", nativeQuery = true)
  void updateLegiStatusById(Long kundeId, boolean status);

  @Modifying
  @Query(value = "update kunde ku set " +
                " ku.is_active = ?2" +
                "  where ku.id = ?1 ", nativeQuery = true)
  void updateActiveStatusById(Long kundeId, boolean status);

  @Modifying
  @Query(value = "update kunde ku set " +
                  "      ku.password = ?2 " +
                  " where ku.id = ?1 ", nativeQuery = true)
  void UpdatePasswordByKundeId(Long kundeId, String encodedPassword);
}
