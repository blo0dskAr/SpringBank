package at.blo0dy.SpringBank.service.bank;

import at.blo0dy.SpringBank.Bank;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;

import java.util.List;

public interface BankService {

  public void saveBank(Bank bank);

  public Bank getBank();

}
