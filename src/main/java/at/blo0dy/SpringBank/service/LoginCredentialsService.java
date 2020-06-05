package at.blo0dy.SpringBank.service;

import at.blo0dy.SpringBank.model.person.mitarbeiter.loginCredentials.LoginCredentials;

import java.util.List;

public interface LoginCredentialsService {

  public List<LoginCredentials> findAll();

  public LoginCredentials findById(Long theId);

  public void save(LoginCredentials loginCredentials);

  public void deleteById(Long theId);



}
