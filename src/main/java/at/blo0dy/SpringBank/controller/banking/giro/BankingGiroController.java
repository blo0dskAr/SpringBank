package at.blo0dy.SpringBank.controller.banking.giro;


import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.service.konto.giro.GiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/kunde/banking/giro")
public class BankingGiroController {

  GiroService giroService;

  @Autowired
  public BankingGiroController(GiroService giroService) {
    this.giroService = giroService;
  }

  @GetMapping("/girokontouebersicht")
  public String viewGiroKontoUebersicht(Model model) {

    List<GiroKonto> giroKontenListe = giroService.findAll();

    model.addAttribute("giroKontenListe",giroKontenListe);
    model.addAttribute("activeLink", "bankenGiroUebersicht");


    return "kunde/banking/giro/girokontouebersicht";
  }




}
