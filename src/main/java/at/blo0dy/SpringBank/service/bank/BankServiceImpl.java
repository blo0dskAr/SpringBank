package at.blo0dy.SpringBank.service.bank;

import at.blo0dy.SpringBank.Bank;
import at.blo0dy.SpringBank.dao.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankServiceImpl implements BankService {
  // Die Bank-Klasse ist  ein Ueberbleibsel aus der Anfangszeit. Ich lass sie aber trotzdem vorerst.

  BankRepository bankRepository;

  @Autowired
  public BankServiceImpl(BankRepository bankRepository) {
    this.bankRepository = bankRepository;
  }

  @Override
  public void saveBank(Bank bank) {
    bankRepository.save(bank);
  }

  @Override
  @Transactional(readOnly = true)
  public Bank getBank() {

    return bankRepository.getOne(1L);
  }

}
