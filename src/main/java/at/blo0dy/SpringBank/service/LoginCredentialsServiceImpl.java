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

  // Nur für Bootstrap Config
  @Override
  public void save(LoginCredentials loginCredentials) {
    loginCredentialsRepository.save(loginCredentials);
  }

}
