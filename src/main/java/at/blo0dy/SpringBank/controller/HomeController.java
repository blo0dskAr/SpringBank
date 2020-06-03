package at.blo0dy.SpringBank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

  @RequestMapping({"", "/", "/index"})
  public String getIndexPage(Model model) {

    // model.addAttribute("nix", blablaService.getStuff());

    return "index";
  }

}
