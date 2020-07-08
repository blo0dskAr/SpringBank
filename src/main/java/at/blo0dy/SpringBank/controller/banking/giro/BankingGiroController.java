package at.blo0dy.SpringBank.controller.banking.giro;


import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.giro.GiroService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/kunde/banking/giro")
public class BankingGiroController {

  GiroService giroService;
  KundeService kundeService;

  @Autowired
  public BankingGiroController(GiroService giroService, KundeService kundeService) {
    this.giroService = giroService;
    this.kundeService = kundeService;
  }

  @GetMapping("/girokontouebersicht")
  public String viewGiroKontoUebersicht(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

    String authKundennummer = authentication.getName();

    List<GiroKonto> giroKontenListe = giroService.findGiroKontenByKundennummer(authKundennummer);
    Kunde kunde = kundeService.findByKundennummer(authKundennummer);

    model.addAttribute("kunde", kunde);
    model.addAttribute("giroKontenListe",giroKontenListe);
    model.addAttribute("activeLink", "bankenGiroUebersicht");


    return "kunde/banking/giro/girokontouebersicht";
  }




}
