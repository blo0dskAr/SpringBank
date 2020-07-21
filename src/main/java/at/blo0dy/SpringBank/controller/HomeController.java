package at.blo0dy.SpringBank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {

  @RequestMapping({"", "/", "/index"})
  public String getPreIndexPage(Model model) {

    return "index";
  }

  @GetMapping("/access-denied")
  public String showAccessDenied() {

    return "access-denied";
  }
}
