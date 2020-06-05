package at.blo0dy.SpringBank.service.adresse;

import at.blo0dy.SpringBank.model.person.adresse.Adresse;

import java.util.List;

public interface AdresseService  {

  public List<Adresse> findAll();

  public Adresse findById(Long theId);

  public void save(Adresse mitarbeiter);

  public void deleteById(Long theId);



}
