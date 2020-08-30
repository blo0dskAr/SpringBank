package at.blo0dy.SpringBank.service.bank;

import at.blo0dy.SpringBank.Bank;
import at.blo0dy.SpringBank.dao.BankRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class BankServiceImplTest {


  @InjectMocks
  BankServiceImpl bankService;

  @Mock
  BankRepository bankRepository;

  @DisplayName("Test saving a Bank")
  @Test
  void saveBank() {

    bankService.saveBank(new Bank("blo0dy Inc.", "blo0dy", 12345666L));

    verify(bankRepository, times(1)).save(any(Bank.class));
  }

  @DisplayName("Test getting a Bank Data")
  @Test
  void getAllBankDaten() {

    when(bankRepository.getOne(anyLong())).thenReturn(new Bank("blo0dy Inc.","blo0dy",12345666L));

    Bank bank = bankService.getBank();

    assertEquals("blo0dy Inc.", bank.getFirmenName());
    assertEquals("blo0dy", bank.getFirmenChef());
    assertEquals(12345666L, bank.getSteuerNummer());
    assertEquals(1L, bank.getId());

  }
}