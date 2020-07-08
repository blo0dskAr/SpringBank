package at.blo0dy.SpringBank.controller.banking.sparen;


import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.sparen.SparService;
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
@RequestMapping("/kunde/banking/sparen")
public class BankingSparenController {

  SparService sparService;
  KundeService kundeService;

  @Autowired
  public BankingSparenController(SparService sparService, KundeService kundeService) {
    this.sparService = sparService;
    this.kundeService = kundeService;
  }

  @GetMapping("/eroeffnung")
  public String viewSparenEroeffnung() {

    return "kunde/banking/sparen/eroeffnungsForm";
  }

  @GetMapping("/sparkontouebersicht")
  public String viewSparKontoUebersicht(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

    String authKundennummer = authentication.getName();

    List<SparKonto> sparKontenListe = sparService.findSparKontoByKundennummer(authKundennummer);
    Kunde kunde = kundeService.findByKundennummer(authKundennummer);

    model.addAttribute("kunde", kunde);
    model.addAttribute("sparKontenListe",sparKontenListe);
    model.addAttribute("activeLink", "bankenSparenUebersicht");


    return "kunde/banking/sparen/sparkontouebersicht";
  }


}
