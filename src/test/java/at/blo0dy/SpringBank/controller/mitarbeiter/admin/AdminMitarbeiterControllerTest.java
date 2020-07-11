package at.blo0dy.SpringBank.controller.mitarbeiter.admin;

import at.blo0dy.SpringBank.dao.MitarbeiterRepository;
import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.service.MitarbeiterServiceImpl;
import at.blo0dy.SpringBank.service.bank.BankServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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

  List<Mitarbeiter> mitarbeiterList = Arrays.asList(   new Mitarbeiter("testerVorname1", "testerNachname",new Adresse("TestStraße 15", "1234","Wien","Österreich"), "123456","Tester"),
                                                       new Mitarbeiter("testerVorname2", "testerNachname",new Adresse("TestStraße 15", "1234","Wien","Österreich"), "223456","Tester"),
                                                       new Mitarbeiter("testerVorname3", "testerNachname",new Adresse("TestStraße 15", "1234","Wien","Österreich"), "323456","Tester"));

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
                    // funkt auch mit kompletten schrottdaten, hauptsache scheinbar der username passt(und hat die rollen etc)
                    // also falsches passwort kann man nicht testen, aber falsche authority aufgrund username sollt gehn?
                    .with(user("hwurst")))
            .apply(springSecurity())
            .build();
  }

//  @WithMockUser(authorities = "customer")
  @Test
  void getIndexPage() throws Exception {
    mockMvc.perform(get("/mitarbeiter/admin/mitarbeiterAdministration"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attribute("activeLink","AdminMAList"));
  }


  @Test
  void getIndexPage_CheckIfPageCorrectWithMultipleMitarbeiter() throws Exception {

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
            .andReturn();

    assertEquals(mitarbeiterList, result.getRequest().getAttribute("mitarbeiter"));

    verify(mitarbeiterService, times(1)).findAll();

  }

  @Test
  void deleteMitarbeiter() throws Exception {

    // TODO: Wie kann man da negativ testen? (nachtrag: wohl mal exceptions basteln und die werfen lassen (doThrow))

    MvcResult result =  mockMvc.perform(get("/mitarbeiter/admin/mitarbeiterAdministration/delete?mitarbeiterId={id}", 1L))
            .andExpect(status().isFound())
            .andExpect(MockMvcResultMatchers.redirectedUrl("/mitarbeiter/admin/mitarbeiterAdministration/list"))
            .andReturn();
    // TODO: redirectete Seite wird im Test nicht mit Daten befüllt ... eher was für IntegrationTest ?
    // .andExpect(MockMvcResultMatchers.model().attribute("activeLink","AdminMAList"));

    verify(mitarbeiterService, times(1)).deleteById(anyLong());

  }

  @Test
  void saveMitarbeiterWithCorrectData() throws Exception {

    Mitarbeiter mitarbeiter = new Mitarbeiter("testeräÄöÖüÜß-muh", "testerNachname",new Adresse("TestStraße 15", "1234","Wien","Österreich"), "123456","Tester");

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
            .andReturn();

    verify(mitarbeiterService, times(1)).save(mitarbeiter);
  }

  @Test
  void saveMitarbeiterWithEmptyData() throws Exception {

    Mitarbeiter mitarbeiter = new Mitarbeiter("", "",new Adresse("", "","",""), "","");

    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
                    .with(csrf())
                    .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "vorname"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "nachname"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "position"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "mitarbeiterNummer"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.strasse"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.plz"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.ort"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.land"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
  }

  @Test
  void saveMitarbeiterWithNullData() throws Exception {

    Mitarbeiter mitarbeiter = new Mitarbeiter(null, null,new Adresse(null, null,null,null), null,null);

    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "vorname"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "nachname"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "position"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "mitarbeiterNummer"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.strasse"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.plz"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.ort"))
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.land"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
  }

  @Test
  void saveMitarbeiterWithTooLongPLZData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("testerVorname4", "testerNachname",new Adresse("TestStraße 15", "123456","Wien","Österreich"), "123456","Tester");
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.plz"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
  }

  @Test
  void saveMitarbeiterWithTooShortPLZData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("testerVorname4", "testerNachname",new Adresse("TestStraße 15", "123","Wien","Österreich"), "123456","Tester");
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.plz"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
  }


  @Test
  void saveMitarbeiterWithWrongFormat_String_PLZData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("testerVorname4", "testerNachname",new Adresse("TestStraße 15", "hallo1234","Wien","Österreich"), "123456","Tester");
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.plz"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
  }

  @Test
  void saveMitarbeiterWithnegativeNumberPLZData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("testerVorname4", "testerNachname",new Adresse("TestStraße 15", "-1234","Wien","Österreich"), "123456","Tester");
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "adresse.plz"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
  }

  @Test
  void saveMitarbeiterWithTooLargeMitarbeiterNummerData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("testerVorname4", "testerNachname",new Adresse("TestStraße 15", "1236","Wien","Österreich"), "100000000","Tester");
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "mitarbeiterNummer"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
  }

  @Test
  void saveMitarbeiterWithNegativeMitarbeiterNummerData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("testerVorname4", "testerNachname",new Adresse("TestStraße 15", "1236","Wien","Österreich"), "-12345678","Tester");
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "mitarbeiterNummer"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
  }

  @Test
  void saveMitarbeiterWithToSmallMitarbeiterNummerData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("testerVorname4", "testerNachname",new Adresse("TestStraße 15", "1236","Wien","Österreich"), "0","Tester");
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "mitarbeiterNummer"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
  }

  @Test
  void saveMitarbeiterWithWrongFormat_String_MitarbeiterNummerData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("testerVorname4", "testerNachname",new Adresse("TestStraße 15", "1236","Wien","Österreich"), "Serwas","Tester");
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "mitarbeiterNummer"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
  }

  @Test
  void saveMitarbeiterWithWrongFormat_Number_VornameData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("123", "testerNachname",new Adresse("TestStraße 15", "1236","Wien","Österreich"), "12345","Tester");
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "vorname"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
  }

  @Test
  void saveMitarbeiterWithWrongFormat_Number_NachnameData() throws Exception {
    Mitarbeiter mitarbeiter = new Mitarbeiter("testerVorname4", "123",new Adresse("TestStraße 15", "1236","Wien","Österreich"), "12345","Tester");
    doNothing().when(mitarbeiterService).save(any());

    MvcResult result =  mockMvc.perform(post("/mitarbeiter/admin/mitarbeiterAdministration/save")
            .with(csrf())
            .flashAttr("mitarbeiter", mitarbeiter))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mitarbeiter", "nachname"))
            .andReturn();

    verify(mitarbeiterService, times(0)).save(mitarbeiter);
  }








  @Test
  void showFormForAdd() {
  }

  @Test
  void showFormForUpdate() {
  }

//  @Test
//  void searchMitarbeiter() {
//  }
}