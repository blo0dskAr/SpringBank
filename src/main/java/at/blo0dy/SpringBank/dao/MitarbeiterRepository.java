package at.blo0dy.SpringBank.dao;

import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface MitarbeiterRepository extends JpaRepository<Mitarbeiter, Long> {


     // @Query("select m from Mitarbeiter m where m.vorname like %?1 or m.nachname like %?1")
     List<Mitarbeiter> findMitarbeiterByVorname(String theSearchName);

     long count();
}
