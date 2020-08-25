package at.blo0dy.SpringBank.service;

import at.blo0dy.SpringBank.dao.person.LoginCredentialsRepository;
import at.blo0dy.SpringBank.model.person.mitarbeiter.loginCredentials.LoginCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginCredentialsServiceImpl implements LoginCredentialsService {

  private LoginCredentialsRepository loginCredentialsRepository;

  @Autowired
  public LoginCredentialsServiceImpl(LoginCredentialsRepository loginCredentialsRepository) {
    this.loginCredentialsRepository = loginCredentialsRepository;
  }


/*  @Override
  @Transactional(readOnly = true)
  public List<LoginCredentials> findAll() {
    return loginCredentialsRepository.findAll();
  }*/

/*  @Override
  @Transactional(readOnly = true)
  public LoginCredentials findById(Long theId) {

    Optional<LoginCredentials> result = loginCredentialsRepository.findById(theId);
    LoginCredentials loginCredentials;
    loginCredentials = result.get();

    return loginCredentials ;
  }*/

  // Nur für Bootstrap Config
  @Override
//  @Transactional
  public void save(LoginCredentials loginCredentials) {
    loginCredentialsRepository.save(loginCredentials);
  }

/*  @Override
//  @Transactional
  public void deleteById(Long theId) {
    loginCredentialsRepository.deleteById(theId);
  }*/

}
