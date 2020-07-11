package at.blo0dy.SpringBank.service;

import at.blo0dy.SpringBank.dao.MitarbeiterRepository;
import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class MitarbeiterServiceImplTest {

  @InjectMocks
  MitarbeiterServiceImpl mitarbeiterService;

  @Mock
  MitarbeiterRepository mitarbeiterRepository;

  Mitarbeiter mitarbeiter1 =  new Mitarbeiter("testerVornamex", "testerNachname",new Adresse("TestStraße 15", "1234","Wien","Österreich"), "123456","Tester") ;
  Mitarbeiter mitarbeiter2 = new Mitarbeiter("testerVornamex", "testerNachname",new Adresse("TestStraße 25", "1234","Wien","Österreich"), "223456","Tester")   ;
  Mitarbeiter mitarbeiter3 =  new Mitarbeiter("testerVornamex", "testerNachname",new Adresse("TestStraße 35", "1234","Wien","Österreich"), "323456","Tester")  ;

  List<Mitarbeiter> mitarbeiterList = Arrays.asList(mitarbeiter1, mitarbeiter2, mitarbeiter3);

  @BeforeEach
  void setUp() {

    // Ids vergeben
    for (int i = 0 ; i < mitarbeiterList.size(); i++) {
      mitarbeiterList.get(i).setId(Long.valueOf(i+1));
      mitarbeiterList.get(i).setMitarbeiterNummer((i+1)+"23456");
    }
  }

  @Test
  void findAllWithEntries() {

    when(mitarbeiterRepository.findAll()).thenReturn(mitarbeiterList);

    List<Mitarbeiter> testMitarbeiterList = mitarbeiterService.findAll();

    assertEquals(3, testMitarbeiterList.size());
    assertEquals("123456",testMitarbeiterList.get(0).getMitarbeiterNummer());
    assertEquals("223456",testMitarbeiterList.get(1).getMitarbeiterNummer());
    assertEquals("323456",testMitarbeiterList.get(2).getMitarbeiterNummer());
    assertThat(testMitarbeiterList).hasSize(3);
  }

  @Test
  void findAllWithNullEntries() {

    when(mitarbeiterRepository.findAll()).thenReturn(null);

    List<Mitarbeiter> testMitarbeiterList = mitarbeiterService.findAll();

    assertNull(testMitarbeiterList);
  }

  @Test
  void findByExistingId() {
    when(mitarbeiterRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(mitarbeiter1));

    Mitarbeiter testMitarbeiter = mitarbeiterService.findById(1L);

    assertEquals("testerVornamex",testMitarbeiter.getVorname());
    verify(mitarbeiterRepository, times(1)).findById(any());

  }

  // TODO: mal exceptionHandler basteln, damit notFounds abgefangen werden, damits auch was sinnvolles zum testen gibt.
/*  @Test
  void findByNotExistingId() {
    when(mitarbeiterRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(null));
    Mitarbeiter testMitarbeiter = mitarbeiterService.findById(1L);
  }*/

//
  @Test
  void save() {

    when(mitarbeiterRepository.save(any())).thenReturn(mitarbeiter1);

    mitarbeiterService.save(mitarbeiter1);

    verify(mitarbeiterRepository, times(1)).save(any());

  }

  @Test
  void deleteByExistingId() {
    doNothing().when(mitarbeiterRepository).deleteById(any());
    mitarbeiterService.deleteById(1L);
    verify(mitarbeiterRepository, times(1)).deleteById(anyLong());
  }

  // gibts noch nicht funktionierend
/*  @Test
  void findMitarbeiterByVorAndNachName() {
  }*/

//  @Test
//  void count() {
//  }
}