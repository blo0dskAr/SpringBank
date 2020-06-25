package at.blo0dy.SpringBank.dao;

import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.User;


import java.util.List;

public interface MitarbeiterRepository extends JpaRepository<Mitarbeiter, Long> {


     // @Query("select m from Mitarbeiter m where m.vorname like %?1 or m.nachname like %?1")
     List<Mitarbeiter> findMitarbeiterByVorname(String theSearchName);

     long count();

//     @Query(value = "select lc.login_name, lc.password from mitarbeiter m, login_credentials lc" +
//             "where lc.mitarbeiter_id = m.id" +
//             "and lc.login_name = :username ;",
//             nativeQuery = true)
//     User findByUsername(String username);
}
