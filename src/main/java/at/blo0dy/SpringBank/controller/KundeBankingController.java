package at.blo0dy.SpringBank.controller;

import at.blo0dy.SpringBank.service.kunde.KundeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kunde/banking")
public class KundeBankingController {

  KundeService kundeService;

  @Autowired
  public KundeBankingController(KundeService kundeService) {
    this.kundeService = kundeService;
  }

  @RequestMapping({"", "/", "/index"})
  public String getPreIndexPage(Model model) {
    model.addAttribute("activeLink", "KundeBankingHome");

    return "kunde/banking/index";
  }

}
