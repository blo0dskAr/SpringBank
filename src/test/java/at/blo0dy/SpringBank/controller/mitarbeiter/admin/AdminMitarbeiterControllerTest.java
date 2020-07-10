package at.blo0dy.SpringBank.controller.mitarbeiter.admin;

import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.service.MitarbeiterServiceImpl;
import at.blo0dy.SpringBank.service.bank.BankServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AdminMitarbeiterController.class)
//@SpringBootTest
class AdminMitarbeiterControllerTest {

//  @InjectMocks
//  AdminMitarbeiterController adminMitarbeiterController;

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
  void getIndexPageWithAuthorizedUser() throws Exception {
    mockMvc.perform(get("/mitarbeiter/admin/mitarbeiterAdministration"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attribute("activeLink","AdminMAList"));
  }

  @Test
  void getIndexPageWithAuthorizedUserCheckView() throws Exception {

    when(mitarbeiterService.findAll()).thenReturn(mitarbeiterList);

    MvcResult result = mockMvc.perform(get("/mitarbeiter/admin/mitarbeiterAdministration"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attribute("activeLink","AdminMAList"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("mitarbeiter"))
            .andExpect(MockMvcResultMatchers.view().name("admin/list-mitarbeiter"))
            .andExpect(MockMvcResultMatchers.content().string(containsString("Admin - Mitarbeiter Directory")))
            .andExpect(MockMvcResultMatchers.content().string(containsString("testerVorname1")))
            .andReturn();

    assertEquals(mitarbeiterList, result.getRequest().getAttribute("mitarbeiter"));

    verify(mitarbeiterService, times(1)).findAll();

  }

  @Test
  void listMitarbeiter() throws Exception {
    mockMvc.perform(get("/mitarbeiter/admin/mitarbeiterAdministration"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attribute("activeLink","AdminMAList"));
  }

  @Test
  void deleteMitarbeiter() {
  }

  @Test
  void saveMitarbeiter() {
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