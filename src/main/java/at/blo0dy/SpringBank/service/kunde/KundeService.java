package at.blo0dy.SpringBank.service.kunde;

import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.math.BigDecimal;
import java.util.List;

public interface KundeService   {

  List<Kunde> findAll();

  Kunde findById(Long theId);

  void save(Kunde kunde);

  void deleteById(Long theId);

  //UserDetails loadUserByUsername(String kundennummer);

  Kunde findByKundennummer(String kundennummer);

  KontoStatusEnum getBestmoeglicherKontoStatusByKundennummer(String kundennummer);

  void setKundeActiveIfRequirementsMetByKundennummer(String kundennummer);

  Long generateNewKontonummerByKundennummer(String kundennummer);

  Long getLatestKundennummerPlusOne();

  String getConnectedGiroByKundennummer(String kundenummer);

  void updateChangeableDataByKundennummer(String kundennummer, String email, String tel, String connectedGiro);

  List<Kunde> findAll(Kunde kunde, ExampleMatcher matcher);


}
