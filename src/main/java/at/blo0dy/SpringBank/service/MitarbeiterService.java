package at.blo0dy.SpringBank.service;

import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;

import java.util.List;

public interface MitarbeiterService {

  List<Mitarbeiter> findAll();

  Mitarbeiter findById(Long theId);

  void save(Mitarbeiter mitarbeiter);

  void deleteById(Long theId);

  List<Mitarbeiter> findMitarbeiterByVorAndNachName(String theSearchName);

  long count();

  String getLatestMitarbeiterNummerPlusOne();
}
