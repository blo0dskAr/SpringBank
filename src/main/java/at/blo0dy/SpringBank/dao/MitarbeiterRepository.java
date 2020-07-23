package at.blo0dy.SpringBank.dao;

import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface MitarbeiterRepository extends JpaRepository<Mitarbeiter, Long> {

  List<Mitarbeiter> findMitarbeiterByVorname(String theSearchName);

  long count();

  @Query(value ="select max((m.mitarbeiternummer)+1) from mitarbeiter m ",
         nativeQuery = true)
  String getLatestMitarbeiterNummerPlusOne();

  @Query(value = "select * from mitarbeiter m, person p, login_credentials l " +
                  " where m.id = p.id " +
                    " and l.mitarbeiter_id = m.id" +
                    " and l.login_name = ?1  ", nativeQuery = true)
  Mitarbeiter findByUsername(String tmpUser);

  @Modifying
  @Query(value = "update login_credentials lc set " +
                       " lc.password =  ?2" +
                  " where lc.mitarbeiter_id = ?1 ",nativeQuery = true)
  void updatePasswordByMitarbeiterId(Long mitarbeiterId, String encodedPassword);
}
