package at.blo0dy.SpringBank.dao;


import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.model.person.rolle.Rolle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RolleRepository extends JpaRepository<Rolle, Long> {

  // standardimpl

 /* @Query("SELECT u FROM User u WHERE u.username=:usernameOrEmail OR u.email=:usernameOrEmail")
  User findByUsernameOrEmail(String usernameOrEmail);*/

  @Query(value = "select count(r.name) from Rolle r" +
         "  where r.id in (select role_id from Map_Mita_Role map, Mitarbeiter m" +
         "                           where map.mita_id = m.id)" +
         "  and r.id = :theId"
         , nativeQuery = true)
  long countByMitarbeiterId(Long theId) ;

  @Query(value = "select m.id from Rolle r, Mitarbeiter m, map_Mita_Role map" +
           " where r.id = map.role_id" +
           " and m.id = map.mita_id" +
           " and r.id = :theId ;",
 nativeQuery = true)
 List<Long> findMitarbeiterIdsByRoleId(Long theId);

  @Query(value = "select m.id from mitarbeiter m" +
                  " where not exists (select 1 from rolle r, map_mita_role map" +
                                     " where r.id = map.role_id" +
                                       " and map.mita_id = m.id" +
                                       " and r.id = :theRoleId) ;" ,
          nativeQuery = true)
  List<Long> findMitarbeiterIdsByRoleIdExeptExisting(Long theRoleId);

  @Modifying
  @Query(value = "delete from map_Mita_Role " +
          " where mita_id = :theMitarbeiterId" +
          "   and role_id = :theId",
  nativeQuery = true)
  void removeRoleFromUser(Long theId, Long theMitarbeiterId);

  @Modifying
  @Query(value = "insert into map_Mita_Role" +
          " (mita_id, role_id)" +
          " values" +
          " (:theMitarbeiterId, :theRoleId) ",
  nativeQuery = true)
  void addRoleToUser(Long theRoleId, Long theMitarbeiterId);


}
