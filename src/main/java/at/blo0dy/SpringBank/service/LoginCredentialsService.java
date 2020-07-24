package at.blo0dy.SpringBank.service;

import at.blo0dy.SpringBank.model.person.mitarbeiter.loginCredentials.LoginCredentials;

import java.util.List;

public interface LoginCredentialsService {

  List<LoginCredentials> findAll();

  LoginCredentials findById(Long theId);

  void save(LoginCredentials loginCredentials);

  void deleteById(Long theId);



}
