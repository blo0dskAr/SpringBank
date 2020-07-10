package at.blo0dy.SpringBank.service.bank;

import at.blo0dy.SpringBank.Bank;
import at.blo0dy.SpringBank.dao.BankRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class BankServiceImplTest {

  @InjectMocks
//  @Autowired
  BankServiceImpl bankService;

  @Mock
//  @MockBean
  BankRepository bankRepository;

  @Test
  void saveBank() {

    bankService.saveBank(new Bank("blo0dy Inc.", "blo0dy", 12345666L));

    verify(bankRepository, times(1)).save(any(Bank.class));
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