package at.blo0dy.SpringBank.dao.person;


import at.blo0dy.SpringBank.model.person.rolle.Rolle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RolleRepository extends JpaRepository<Rolle, Long> {

  @Query(value = "select count(r.name) from rolle r" +
         "  where r.id in (select role_id from map_mita_role map, mitarbeiter m" +
         "                           where map.mita_id = m.id)" +
         "  and r.id = :theId"
         , nativeQuery = true)
  long countByMitarbeiterId(Long theId) ;

  @Query(value = "select m.id from rolle r, mitarbeiter m, map_mita_role map" +
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

//  @Modifying
//  @Query(value = "delete from map_mita_role " +
//          " where mita_id = :theMitarbeiterId" +
//          "   and role_id = :theId",
//  nativeQuery = true)
//  void removeRoleFromUser(Long theId, Long theMitarbeiterId);

//  @Modifying
//  @Query(value = "insert into map_mita_role" +
//          " (mita_id, role_id)" +
//          " values" +
//          " (:theMitarbeiterId, :theRoleId) ",
//  nativeQuery = true)
//  void addRoleToUser(Long theRoleId, Long theMitarbeiterId);


}
