package at.blo0dy.SpringBank.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = HomeController.class)
class HomeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
  }

  @WithMockUser
  @Test
  void getPreIndexPage() throws Exception {
    mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"));
  }


  @Test
  void getPreIndexPageAnonymousGets401() throws Exception {
    mockMvc.perform(get("/"))
            .andExpect(status().is(401))
            .andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  void showAccessDenied() throws Exception {
    mockMvc.perform(get("/access-denied"))
            .andExpect(status().isOk())
            .andExpect(view().name("access-denied"))
            .andExpect(content().string(containsString("blo0dy Access Denied Page - You are not authorized to access this resource.")));
  }
}