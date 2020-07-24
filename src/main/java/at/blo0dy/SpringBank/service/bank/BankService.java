package at.blo0dy.SpringBank.service.bank;

import at.blo0dy.SpringBank.Bank;


public interface BankService {

  void saveBank(Bank bank);

  Bank getBank();

}
