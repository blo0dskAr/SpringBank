package at.blo0dy.SpringBank.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class HomeController {

  @RequestMapping({"", "/", "/index"})
  public String getPreIndexPage(Model model) {
    log.debug("Allgemeine TestIndex Page wird aufgerufen.");

    return "index";
  }

  @GetMapping("/access-denied")
  public String showAccessDenied() {
    log.debug("Access Denied Page wird ausgeliefert.");

    return "access-denied";
  }
}
