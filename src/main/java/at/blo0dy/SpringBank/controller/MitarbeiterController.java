package at.blo0dy.SpringBank.controller;

import at.blo0dy.SpringBank.service.MitarbeiterService;
import at.blo0dy.SpringBank.service.bank.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mitarbeiter")
public class MitarbeiterController {

  private MitarbeiterService mitarbeiterService;
  private BankService bankservice;

  @Autowired
  public MitarbeiterController(MitarbeiterService mitarbeiterService, BankService bankservice) {
    this.mitarbeiterService = mitarbeiterService;
    this.bankservice = bankservice;
  }

  @RequestMapping({"", "/", "/index"})
  public String getIndexPage(Model model) {

    model.addAttribute("bank", bankservice.getBank());
    model.addAttribute("mitarbeiter",mitarbeiterService.findAll());
    model.addAttribute("mitarbeitercount",mitarbeiterService.count());
    model.addAttribute("activeLink", "MitarbeiterHome");

    return "mitarbeiter/index";
  }

}
