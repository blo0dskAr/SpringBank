package at.blo0dy.SpringBank.dao;

import at.blo0dy.SpringBank.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long>  {


}
