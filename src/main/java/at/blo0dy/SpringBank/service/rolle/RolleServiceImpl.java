package at.blo0dy.SpringBank.service.rolle;

import at.blo0dy.SpringBank.dao.RolleRepository;
import at.blo0dy.SpringBank.model.person.rolle.Rolle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RolleServiceImpl implements RolleService {

  private RolleRepository rolleRepository;

  @Autowired
  public RolleServiceImpl(RolleRepository rolleRepository) {
    this.rolleRepository = rolleRepository;
  }


  @Override
  @Transactional
  public List<Rolle> findAll() {
    return rolleRepository.findAll();
  }

  @Override
  @Transactional
  public Rolle findById(Long theId) {

    Optional<Rolle> result = rolleRepository.findById(theId);

    Rolle rolle;

    rolle = result.get();

    return rolle ;
  }

  @Override
  @Transactional
  public void save(Rolle rolle) {
    rolleRepository.save(rolle);
    System.out.println("RolleServiceImpl:Rolle: " + rolle);
  }

  @Override
  @Transactional
  public void deleteById(Long theId) {
    rolleRepository.deleteById(theId);

  }
}
