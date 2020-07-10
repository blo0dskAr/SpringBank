package at.blo0dy.SpringBank.service.bank;

import at.blo0dy.SpringBank.Bank;
import at.blo0dy.SpringBank.dao.BankRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class BankServiceImplTest {

  @InjectMocks
  BankServiceImpl bankService;

  @Mock
  BankRepository bankRepository;

  @Test
  void saveBank() {
    when(bankRepository.save(new Bank("blo0dy Inc.", "blo0dy", 12345666L))).thenReturn(new Bank("blo0dy Inc.", "blo0dy", 12345666L));

    Bank bank = new Bank("blo0dy Inc.", "blo0dy", 12345666L);
    bankService.saveBank(bank);

    assertEquals("blo0dy Inc.", bank.getFirmenName());
    assertEquals("blo0dy", bank.getFirmenChef());
    assertEquals(12345666L, bank.getSteuerNummer());
    assertEquals(1L, bank.getId());

  }

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