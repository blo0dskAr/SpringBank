package at.blo0dy.SpringBank.controller.mitarbeiter.admin;

import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.service.MitarbeiterServiceImpl;
import at.blo0dy.SpringBank.service.bank.BankServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = AdminMitarbeiterController.class)
//@SpringBootTest
class AdminMitarbeiterControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  protected WebApplicationContext wac;

  @Autowired
  private FilterChainProxy springSecurityFilterChain;

  @MockBean
  private MitarbeiterServiceImpl mitarbeiterService;

  @MockBean
  private BankServiceImpl bankService;

  List<Mitarbeiter> mitarbeiterList = Arrays.asList(   new Mitarbeiter("testerVorname1", "testerNachname",new Adresse("TestStraße 15", "1234","Wien","Österreich"), "123456","Tester", LocalDate.of(1984,1,1)),
                                                       new Mitarbeiter("testerVorname2", "testerNachname",new Adresse("TestStraße 15", "1234","Wien","Österreich"), "223456","Tester", LocalDate.of(1984,1,1)),
                                                       new Mitarbeiter("testerVorname3", "testerNachname",new Adresse("TestStraße 15", "1234","Wien","Österreich"), "323456","Tester", LocalDate.of(1984,1,1)));

  @BeforeEach
  void setUp() {

/*    this.mockMvc = webAppContextSetup(wac)
            .apply(springSecurity(springSecurityFilterChain))
            .build();*/

    // Ids an Mitarbeiter vergeben
    for (int i = 1 ; i < mitarbeiterList.size(); i++) {
      mitarbeiterList.get(i).setId(Long.valueOf(i));
      mitarbeiterList.get(i).setVorname("testerVorname" + i);
    }

    mockMvc = MockMvcBuilders
            .webAppContextSetup(wac)
            .defaultRequest(get("/")
//                    // funkt auch mit kompletten schrottdaten, hauptsache scheinbar der username passt(und hat die rollen etc)
//                    // also falsches passwort kann man nicht testen, aber falsche authority aufgrund username sollt gehn?
                    .with(user("wuascht")))
            .apply(springSecurity())
            .build();
  }

  @Test
  void getIndexPage_Empty() throws Exception {
    mockMvc.perform(get("/mitarbeiter/admin/mitarbeiterAdministration"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attribute("activeLink","AdminMAList"))
            .andExpect(MockMvcResultMatchers.model().attribute("mitarbeiter", hasSize(0)))
            .andExpect(MockMvcResultMatchers.model().hasNoErrors());

    verify(mitarbeiterService, times(1)).findAll();
    verifyNoMoreInteractions(mitarbeiterService);
  }


  @Test
  void getIndexPage_WithMultipleMitarbeiter() throws Exception {

    when(mitarbeiterService.findAll()).thenReturn(mitarbeiterList);

    MvcResult result = mockMvc.perform(get("/mitarbeiter/admin/mitarbeiterAdministration"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attribute("activeLink","AdminMAList"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("mitarbeiter"))
            .andExpect(MockMvcResultMatchers.model().attribute("mitarbeiter", hasSize(3)))
            .andExpect(MockMvcResultMatchers.model().attribute("mitarbeiter", hasItem(allOf(
                    hasProperty("id", is(1L)),
                    hasProperty("vorname", is("testerVorname1"))
            ))))
            .andExpect(MockMvcResultMatchers.view().name("admin/list-mitarbeiter"))
            .andExpect(MockMvcResultMatchers.content().string(containsString("Admin - Mitarbeiter Directory")))
            .andExpect(MockMvcResultMatchers.content().string(containsString("testerVorname1")))
            .andExpect(MockMvcResultMatchers.model().hasNoErrors())
            .andReturn();

    assertEquals(mitarbeiterList, result.getRequest().getAttribute("mitarbeiter"));

    verify(mitarbeiterService, times(1)).findAll();
    verifyNoMoreInteractions(mitarbeiterService);

  }

  @Test
  void deleteMitarbeiter() throws Exception {

    // TODO: Wie kann man da negativ testen? (nachtrag: wohl mal exceptions basteln und die werfen lassen (doThrow))

    MvcResult result =  mockMvc.perform(get("/mitarbeiter/admin/mitarbeiterAdministration/delete?mitarbeiterId={id}", 1L))
            .andExpect(status().isFound())
            .andExpect(MockMvcResultMatchers.redirectedUrl("/mitarbeiter/admin/mitarbeiterAdministration/list"))
            .andExpect(MockMvcResultMatchers.model().hasNoErrors())
            .andReturn();
    // TODO: redirectete Seite wird im Test nicht mit Daten befüllt ... eher was für IntegrationTest ?
    // .andExpect(MockMvcResultMatchers.model().attribute("activeLink","AdminMAList"));

    verify(mitarbeiterService, times(1)).deleteById(anyLong());
    verifyNoMoreInteractions(mitarbeiterService);

  }

  @Test
  void saveMitarbeiterWithCorrectData() throws Exception {

    Mitarbeiter mitarbeiter = new Mitarbeiter("testeräÄöÖüÜß-muh", "testerNachname",new Adresse("TestStraße 15", "1234","Wien","Österreich"), "123456","Tester", LocalDate.of(1984,1,1));

    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
                    .with(csrf())
                    .flashAttr("mitarbeiter", mitarbeiter)
//            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//            .param("id", "0")
//            .param("adresse.id","")
//            .param("vorname", "testerVorname4")
//            .param("nachname", "testerNachname")
//            .param("adresse.strasse", "Teststraße 12")
//            .param("adresse.plz", "1234")
//            .param("adresse.ort", "Wien")
//            .param("adresse.land", "Österreich")
//            .param("mitarbeiterNummer", "123456")
//            .param("position", "Tester")
    )
            .andExpect(status().isFound())
            .andExpect(MockMvcResultMatchers.redirectedUrl("/mitarbeiter/admin/mitarbeiterAdministration/list"))
            .andExpect(MockMvcResultMatchers.model().hasNoErrors())
            .andReturn();

    verify(mitarbeiterService, times(1)).save(mitarbeiter);
    verify(mitarbeiterService,times(1)).getLatestMitarbeiterNummerPlusOne();
    verifyNoMoreInteractions(mitarbeiterService);
  }

  @Test
  void saveMitarbeiterWithEmptyData() throws Exception {

    Mitarbeiter mitarbeiter = new Mitarbeiter("", "",new Adresse("", "","",""), "","",null);

    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
                    .with(csrf())
                    .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "vorname"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "nachname"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "position"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.strasse"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.plz"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.ort"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.land"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
    verify(mitarbeiterService,times(1)).getLatestMitarbeiterNummerPlusOne();
    verifyNoMoreInteractions(mitarbeiterService);
  }

  @Test
  void saveMitarbeiterWithNullData() throws Exception {

    Mitarbeiter mitarbeiter = new Mitarbeiter(null, null,new Adresse(null, null,null,null), null,null, null);

    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "vorname"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "nachname"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "position"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.strasse"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.plz"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.ort"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.land"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "geburtsDatum"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
    verify(mitarbeiterService,times(1)).getLatestMitarbeiterNummerPlusOne();
    verifyNoMoreInteractions(mitarbeiterService);
  }

  @Test
  void saveMitarbeiterWithTooLongPLZData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("testerVorname4", "testerNachname",new Adresse("TestStraße 15", "123456","Wien","Österreich"), "123456","Tester", LocalDate.of(1984,1,1));
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.plz"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
    verify(mitarbeiterService,times(1)).getLatestMitarbeiterNummerPlusOne();
    verifyNoMoreInteractions(mitarbeiterService);
  }

  @Test
  void saveMitarbeiterWithTooShortPLZData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("testerVorname4", "testerNachname",new Adresse("TestStraße 15", "123","Wien","Österreich"), "123456","Tester", LocalDate.of(1984,1,1));
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.plz"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
    verify(mitarbeiterService,times(1)).getLatestMitarbeiterNummerPlusOne();
    verifyNoMoreInteractions(mitarbeiterService);
  }


  @Test
  void saveMitarbeiterWithWrongFormat_String_PLZData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("testerVorname", "testerNachname",new Adresse("TestStraße 15", "hallo1234","Wien","Österreich"), "123456","Tester", LocalDate.of(1984,1,1));
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.plz"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
    verify(mitarbeiterService,times(1)).getLatestMitarbeiterNummerPlusOne();
    verifyNoMoreInteractions(mitarbeiterService);
  }

  @Test
  void saveMitarbeiterWithnegativeNumberPLZData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("testerVorname4", "testerNachname",new Adresse("TestStraße 15", "-1234","Wien","Österreich"), "123456","Tester", LocalDate.of(1984,1,1));
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.plz"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
    verify(mitarbeiterService,times(1)).getLatestMitarbeiterNummerPlusOne();
    verifyNoMoreInteractions(mitarbeiterService);
  }

  @Test
  void saveMitarbeiterWithTooLargeMitarbeiterNummerData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("testerVorname4", "testerNachname",new Adresse("TestStraße 15", "1236","Wien","Österreich"), "100000000","Tester", LocalDate.of(1984,1,1));
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
    verify(mitarbeiterService,times(1)).getLatestMitarbeiterNummerPlusOne();
    verifyNoMoreInteractions(mitarbeiterService);
  }

  @Test
  void saveMitarbeiterWithNegativeMitarbeiterNummerData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("testerVorname4", "testerNachname",new Adresse("TestStraße 15", "1236","Wien","Österreich"), "-12345678","Tester", LocalDate.of(1984,1,1));
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
    verify(mitarbeiterService,times(1)).getLatestMitarbeiterNummerPlusOne();
    verifyNoMoreInteractions(mitarbeiterService);
  }

  @Test
  void saveMitarbeiterWithToSmallMitarbeiterNummerData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("testerVorname4", "testerNachname",new Adresse("TestStraße 15", "1236","Wien","Österreich"), "0","Tester", LocalDate.of(1984,1,1));
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
    verify(mitarbeiterService,times(1)).getLatestMitarbeiterNummerPlusOne();
    verifyNoMoreInteractions(mitarbeiterService);
  }

  @Test
  void saveMitarbeiterWithWrongFormat_String_MitarbeiterNummerData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("testerVorname4", "testerNachname",new Adresse("TestStraße 15", "1236","Wien","Österreich"), "Serwas","Tester", LocalDate.of(1984,1,1));
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
    verify(mitarbeiterService,times(1)).getLatestMitarbeiterNummerPlusOne();
    verifyNoMoreInteractions(mitarbeiterService);
  }

  @Test
  void saveMitarbeiterWithWrongFormat_Number_VornameData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("123", "testerNachname",new Adresse("TestStraße 15", "1236","Wien","Österreich"), "12345","Tester", LocalDate.of(1984,1,1));
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "vorname"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
    verify(mitarbeiterService,times(1)).getLatestMitarbeiterNummerPlusOne();
    verifyNoMoreInteractions(mitarbeiterService);
  }

  @Test
  void saveMitarbeiterWithWrongFormat_Number_NachnameData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("testerVorname4", "123",new Adresse("TestStraße 15", "1236","Wien","Österreich"), "12345","Tester", LocalDate.of(1984,1,1));
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "nachname"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
    verify(mitarbeiterService,times(1)).getLatestMitarbeiterNummerPlusOne();
    verifyNoMoreInteractions(mitarbeiterService);
  }


  @Test
  void showFormForAdd() throws Exception {

    MvcResult result =  mockMvc.perform(get("/mitarbeiter/admin/mitarbeiterAdministration/showFormForAdd"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeExists("mitarbeiter"))
            .andExpect(MockMvcResultMatchers.model().attribute("mitarbeiter", hasProperty("id", Matchers.equalTo(0L))))
            .andExpect(MockMvcResultMatchers.model().attribute("mitarbeiter", hasProperty("vorname", Matchers.equalTo(null))))
            .andExpect(MockMvcResultMatchers.model().attribute("mitarbeiter", hasProperty("nachname", Matchers.equalTo(null))))
            .andExpect(MockMvcResultMatchers.model().attribute("mitarbeiter", hasProperty("position", Matchers.equalTo(null))))
            .andExpect(MockMvcResultMatchers.model().attribute("mitarbeiter", hasProperty("mitarbeiterNummer", Matchers.equalTo(null))))
            .andExpect(MockMvcResultMatchers.content().string(containsString("name=\"adresse.land\"")))
            .andExpect(MockMvcResultMatchers.content().string(containsString("name=\"adresse.plz\"")))
            .andExpect(MockMvcResultMatchers.content().string(containsString("name=\"adresse.strasse\"")))
            .andExpect(MockMvcResultMatchers.content().string(containsString("name=\"adresse.ort\"")))
            .andExpect(MockMvcResultMatchers.content().string(containsString("name=\"position\"")))
            .andExpect(MockMvcResultMatchers.content().string(containsString("name=\"mitarbeiterNummer\"")))
            .andExpect(MockMvcResultMatchers.content().string(containsString("name=\"vorname\"")))
            .andExpect(MockMvcResultMatchers.content().string(containsString("name=\"nachname\"")))
            .andExpect(MockMvcResultMatchers.content().string(containsString("<input type=\"hidden\" id=\"id\" name=\"id\" value=\"0\"")))
            .andExpect(MockMvcResultMatchers.content().string(containsString("<input type=\"hidden\" id=\"adresse.id\" name=\"adresse.id\"")))
            .andExpect(MockMvcResultMatchers.model().hasNoErrors())
            .andReturn();

    verifyNoMoreInteractions(mitarbeiterService);

  }

  @Test
  void showFormForUpdate() throws Exception{

    Mitarbeiter mitarbeiter = new Mitarbeiter("testerVornamexx", "testerNachname",new Adresse("TestStraße 15", "1236","Wien","Österreich"), "12345","Tester", LocalDate.of(1984,1,1));
    mitarbeiter.setId(1L);
    when(mitarbeiterService.findById(1L)).thenReturn(mitarbeiter);

    MvcResult result =  mockMvc.perform(get("/mitarbeiter/admin/mitarbeiterAdministration/showFormForUpdate?mitarbeiterId={id}", 1L))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeExists("mitarbeiter"))
            .andExpect(MockMvcResultMatchers.model().attribute("mitarbeiter", hasProperty("id", Matchers.equalTo(1L))))
            .andExpect(MockMvcResultMatchers.model().attribute("mitarbeiter", hasProperty("vorname", Matchers.equalTo("testerVornamexx"))))
            .andExpect(MockMvcResultMatchers.model().attribute("mitarbeiter", hasProperty("nachname", Matchers.equalTo("testerNachname"))))
            .andExpect(MockMvcResultMatchers.model().attribute("mitarbeiter", hasProperty("position", Matchers.equalTo("Tester"))))
            .andExpect(MockMvcResultMatchers.model().attribute("mitarbeiter", hasProperty("mitarbeiterNummer", Matchers.equalTo("12345"))))
            .andExpect(MockMvcResultMatchers.model().attribute("mitarbeiter", hasProperty("adresse",Matchers.hasToString("TestStraße 15, 1236 Wien, Österreich"))))
            .andExpect(MockMvcResultMatchers.model().hasNoErrors())
            .andReturn();

    verify(mitarbeiterService, times(1)).findById(any());
    verifyNoMoreInteractions(mitarbeiterService);

  }

//  @Test
//  void searchMitarbeiter() {
//  }
}