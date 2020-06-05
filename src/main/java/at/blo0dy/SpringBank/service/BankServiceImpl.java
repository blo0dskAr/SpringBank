package at.blo0dy.SpringBank.service;

import at.blo0dy.SpringBank.Bank;
import at.blo0dy.SpringBank.dao.BankRepository;
import at.blo0dy.SpringBank.dao.MitarbeiterRepository;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BankServiceImpl implements BankService {

  BankRepository bankRepository;

  @Autowired
  public BankServiceImpl(BankRepository bankRepository) {
    this.bankRepository = bankRepository;
  }

  @Override
  @Transactional
  public void saveBank(Bank bank) {
    bankRepository.save(bank);
  }

  @Override
  @Transactional
  public Bank getBank() {
    return bankRepository.getOne(1L);
  }

}
