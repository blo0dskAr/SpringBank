package at.blo0dy.SpringBank.service.kunde;

import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import org.springframework.data.domain.ExampleMatcher;

import java.util.List;

public interface KundeService   {

  List<Kunde> findAll();

  Kunde findById(Long theId);

  void save(Kunde kunde);

  void deleteById(Long theId);

  Kunde findByKundennummer(String kundennummer);

  KontoStatusEnum getBestmoeglicherKontoStatusByKundennummer(String kundennummer);

  void setKundeActiveIfRequirementsMetByKundennummer(String kundennummer);

  Long generateNewKontonummerByKundennummer(String kundennummer);

  Long getLatestKundennummerPlusOne();

  String getConnectedGiroByKundennummer(String kundennummer);

  void updateChangeableDataByKundennummer(String kundennummer, String email, String tel, String connectedGiro, boolean hasAcceptedAGB);

  List<Kunde> findAll(Kunde kunde, ExampleMatcher matcher);

  void saveWithoutPassword(Kunde kunde);

  void updateLegiStatusById(Long kundeId, boolean status);

  void updateActiveStatusById(Long kundeId, boolean status);

  void updatePasswordByKundeId(Long id, String encode);
}
