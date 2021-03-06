package at.blo0dy.SpringBank.controller.bank;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = BankController.class)
class BankControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @WithMockUser
  @Test
  void bankPhilosophieViewAttributeExist() throws Exception {
    mockMvc.perform(get("/kunde/bank/philosophie"))
            .andExpect(status().isOk())
            .andExpect(view().name("kunde/bank/philosophie"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("activeLink"))
            .andExpect(MockMvcResultMatchers.model().hasNoErrors());
  }

  @WithMockUser
  @Test
  void bankPhilosophieView() throws Exception {
    mockMvc.perform(get("/kunde/bank/philosophie"))
            .andExpect(status().isOk())
            .andExpect(view().name("kunde/bank/philosophie"))
            .andExpect(MockMvcResultMatchers.model().attribute("activeLink","kundeBankPhilosophie"))
            .andExpect(MockMvcResultMatchers.model().hasNoErrors());;
  }

  @WithMockUser
  @Test
  void bankKarriereView() throws Exception {
    mockMvc.perform(get("/kunde/bank/karriere"))
            .andExpect(status().isOk())
            .andExpect(view().name("kunde/bank/karriere"))
            .andExpect(MockMvcResultMatchers.model().attribute("activeLink","kundeBankKarriere"))
            .andExpect(MockMvcResultMatchers.model().hasNoErrors());;
  }

  @WithMockUser
  @Test
  void bankKontaktView() throws Exception {
    mockMvc.perform(get("/kunde/bank/kontakt"))
            .andExpect(status().isOk())
            .andExpect(view().name("kunde/bank/kontakt"))
            .andExpect(MockMvcResultMatchers.model().attribute("activeLink","kundeBankKontakt"))
            .andExpect(MockMvcResultMatchers.model().hasNoErrors());;
  }

  @WithMockUser
  @Test
  void bankImpressumView() throws Exception {
    mockMvc.perform(get("/kunde/bank/impressum"))
            .andExpect(status().isOk())
            .andExpect(view().name("kunde/bank/impressum"))
            .andExpect(MockMvcResultMatchers.model().attribute("activeLink","kundeBankImpressum"))
            .andExpect(MockMvcResultMatchers.model().hasNoErrors());;
  }

  @WithMockUser
  @Test
  public void testList() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("index"))
            .andExpect(MockMvcResultMatchers.model().hasNoErrors());;
//            .andExpect(MockMvcResultMatchers.model().attributeExists("requestURI"));
//            .andExpect(MockMvcResultMatchers.model().attribute("products",
//                    Matchers.is(Matchers.empty())));

  }

  @WithMockUser(username = "123", password = "test", authorities = "customer")
  @Test
  public void anotherTest() throws Exception {
    mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(content().string(
                    containsString("Neuer Kunde werden")))
            .andExpect(MockMvcResultMatchers.model().hasNoErrors());;
  }

  @WithMockUser
  @Test
  public void anotherTestStyle() throws Exception {

    RequestBuilder request = MockMvcRequestBuilders
            .get("/");
//            .accept(MediaType.APPLICATION_JSON)

    MvcResult result = mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Neuer Kunde werden")))
            .andExpect(MockMvcResultMatchers.model().hasNoErrors())
            .andReturn();

  }

}