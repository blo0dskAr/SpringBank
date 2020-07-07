package at.blo0dy.SpringBank.controller.banking.kredit;


import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.service.konto.kredit.KreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/kunde/banking/kredit")
public class BankingKreditController {

  KreditService kreditService;

  @Autowired
  public BankingKreditController(KreditService kreditService) {
    this.kreditService = kreditService;
  }

  @GetMapping("/kreditkontouebersicht")
  public String viewKreditKontoUebersicht(Model model) {

    List<KreditKonto> kreditKontenListe = kreditService.findAll();

    model.addAttribute("kreditKontenListe",kreditKontenListe);
    model.addAttribute("activeLink", "bankenKreditUebersicht");


    return "kunde/banking/kredit/kreditkontouebersicht";
  }




}
