package at.blo0dy.SpringBank.service;

import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;

import java.util.List;

public interface MitarbeiterService {

  public List<Mitarbeiter> findAll();

  public Mitarbeiter findById(Long theId);

  public void save(Mitarbeiter mitarbeiter);

  public void deleteById(Long theId);

}
