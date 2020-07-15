package at.blo0dy.SpringBank.dao;

import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface KundeRepository extends JpaRepository<Kunde, Long> {

  Kunde findByKundennummer(String kundennummer);

/*  // ToDo: gibts ne moeglichkeit, ohne eine extra klasse dafür zu erstellen, um nicht alle infos aus dem Kunden rauszuholen? (v.a. password)
  @Query(value = "select k.id, k.kundennummer, p.nachname, p.vorname, k.email_adresse, k.first_login_done, " +
                  "k.has_acceptedagb, k.is_active, k.is_legi, a.strasse, a.ort, a.plz, a.land  " +
                  "from person p, kunde k, adresse a\n" +
                  " where  k.id = p.id\n" +
                  "  and p.adresse_id = a.id\n" +
                  "  and k.kundennummer=?1;",
          nativeQuery = true)
  Kunde findByKundennummerShort(String kundennummer);*/


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
                " ku.connected_giro = ?4 " +
          "where ku.kundennummer = ?1 " , nativeQuery = true)
  void updateChangeableDataByKundennummer(String kundennummer, String email, String tel, String connectedGiro);

//  List<Kunde> findAll(Kunde kunde);



}
