package at.blo0dy.SpringBank.dao;

import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface MitarbeiterRepository extends JpaRepository<Mitarbeiter, Long> {

  List<Mitarbeiter> findMitarbeiterByVorname(String theSearchName);

  long count();

  @Query(value ="select max((m.mitarbeiternummer)+1) from mitarbeiter m ",
         nativeQuery = true)
  String getLatestMitarbeiterNummerPlusOne();

}
