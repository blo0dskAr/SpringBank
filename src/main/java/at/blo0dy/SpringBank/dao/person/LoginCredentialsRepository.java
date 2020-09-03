package at.blo0dy.SpringBank.dao.person;


import at.blo0dy.SpringBank.model.person.mitarbeiter.loginCredentials.LoginCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginCredentialsRepository extends JpaRepository<LoginCredentials, Long> {


}
