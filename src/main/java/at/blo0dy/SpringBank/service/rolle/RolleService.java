package at.blo0dy.SpringBank.service.rolle;

import at.blo0dy.SpringBank.model.person.rolle.Rolle;

import java.util.List;

public interface RolleService {

  public List<Rolle> findAll();

  public Rolle findById(Long theId);

  public void save(Rolle rolle);

  public void deleteById(Long theId);



}
