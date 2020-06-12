package at.blo0dy.SpringBank.controller;

import at.blo0dy.SpringBank.service.MitarbeiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {

  MitarbeiterService mitarbeiterService;

  @Autowired
  public HomeController(MitarbeiterService mitarbeiterService) {
    this.mitarbeiterService = mitarbeiterService;
  }

  @RequestMapping({"", "/", "/index"})
  public String getPreIndexPage(Model model) {

    return "index";
  }

  @GetMapping("/access-denied")
  public String showAccessDenied() {

    return "access-denied";
  }
}
