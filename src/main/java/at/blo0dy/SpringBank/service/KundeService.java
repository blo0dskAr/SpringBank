package at.blo0dy.SpringBank.service;

import at.blo0dy.SpringBank.model.person.kunde.Kunde;

import java.util.List;

public interface KundeService {

  public List<Kunde> findAll();

  public Kunde findById(Long theId);

  public void save(Kunde kunde);

  public void deleteById(Long theId);



}