package at.blo0dy.SpringBank.dao;

import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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



}
