package at.blo0dy.SpringBank.controller.banking.sparen;


import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.service.konto.sparen.SparService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/kunde/banking/sparen")
public class BankingSparenController {

  SparService sparService;

  @Autowired
  public BankingSparenController(SparService sparService) {
    this.sparService = sparService;
  }

  @GetMapping("/eroeffnung")
  public String viewSparenEroeffnung() {

    return "kunde/sparen/eroeffnungsForm";
  }

  @GetMapping("/sparkontouebersicht")
  public String viewSparKontoUebersicht(Model model) {

    List<SparKonto> sparKontenListe = sparService.findAll();

    model.addAttribute("sparKontenListe",sparKontenListe);
    model.addAttribute("activeLink", "bankenSparenUebersicht");


    return "kunde/banking/sparen/sparkontouebersicht";
  }


}
