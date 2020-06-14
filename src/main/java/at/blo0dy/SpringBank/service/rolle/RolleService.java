package at.blo0dy.SpringBank.service.rolle;

import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.model.person.rolle.Rolle;

import java.sql.SQLException;
import java.util.List;

public interface RolleService {

  List<Rolle> findAll();

  Rolle findById(Long theId);

  void save(Rolle rolle);

  void deleteById(Long theId);

  long countByMitarbeiterId(Long theId) ;

  List<Mitarbeiter> findMitarbeiterIdsByRoleId(Long theId);

  void removeRoleFromUser(Long theId, Long theMitarbeiterId);

  void addRoleToUser(Long theRoleId, Long theMitarbeiterId);

  List<Mitarbeiter> findMitarbeiterIdsByRoleIdExeptExisting(Long theRoleId);

}
