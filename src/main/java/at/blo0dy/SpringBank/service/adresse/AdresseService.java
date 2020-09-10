package at.blo0dy.SpringBank.service.adresse;

import at.blo0dy.SpringBank.model.person.adresse.Adresse;

import java.util.List;

public interface AdresseService  {

  List<Adresse> findAll();

  Adresse findById(Long theId);

  void save(Adresse mitarbeiter);

  void deleteById(Long theId);



}
