package at.blo0dy.SpringBank.controller;

import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.service.BankService;
import at.blo0dy.SpringBank.service.MitarbeiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {

  BankService bankService;
  MitarbeiterService mitarbeiterService;

  @Autowired
  public HomeController(BankService bankService, MitarbeiterService mitarbeiterService) {
    this.bankService = bankService;
    this.mitarbeiterService = mitarbeiterService;
  }


  @RequestMapping({"", "/", "/index"})
  public String getIndexPage(Model model) {

     model.addAttribute("bank", bankService.getBank());
     model.addAttribute("mitarbeiter",mitarbeiterService.findAll());
    model.addAttribute("mitarbeitercount",mitarbeiterService.count());

    return "index";
  }


}
