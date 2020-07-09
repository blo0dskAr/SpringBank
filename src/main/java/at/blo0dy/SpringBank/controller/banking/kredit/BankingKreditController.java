package at.blo0dy.SpringBank.controller.banking.kredit;


import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.kredit.KreditService;
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
@RequestMapping("/kunde/banking/kredit")
public class BankingKreditController {

  KreditService kreditService;
  KundeService kundeService;

  @Autowired
  public BankingKreditController(KreditService kreditService, KundeService kundeService) {
    this.kreditService = kreditService;
    this.kundeService = kundeService;
  }

  @GetMapping("/kreditkontouebersicht")
  public String viewKreditKontoUebersicht(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

    String authKundennummer = authentication.getName();

    List<KreditKonto> kreditKontenListe = kreditService.findKreditKontenByKundennummer(authKundennummer);
    Kunde kunde = kundeService.findByKundennummer(authKundennummer);

    model.addAttribute("kunde", kunde);
    model.addAttribute("kreditKontenListe",kreditKontenListe);
    model.addAttribute("activeLink", "kundeBankingKreditUebersicht");


    return "kunde/banking/kredit/kreditkontouebersicht";
  }




}
