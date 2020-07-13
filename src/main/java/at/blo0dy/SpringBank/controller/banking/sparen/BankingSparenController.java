package at.blo0dy.SpringBank.controller.banking.sparen;


import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.kontoBuchung.KontoBuchungService;
import at.blo0dy.SpringBank.service.konto.sparen.SparService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/kunde/banking/sparen")
public class BankingSparenController {

  SparService sparService;
  KundeService kundeService;
  KontoBuchungService kontoBuchungService;

  @Autowired
  public BankingSparenController(SparService sparService, KundeService kundeService, KontoBuchungService kontoBuchungService) {
    this.sparService = sparService;
    this.kundeService = kundeService;
    this.kontoBuchungService = kontoBuchungService;
  }

  @GetMapping("/sparkontouebersicht")
  public String viewSparKontoUebersicht(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

    String authKundennummer = authentication.getName();
    log.debug("Showing viewSparKontoUebersicht for Kunde: " + authKundennummer);

    List<SparKonto> sparKontenListe = sparService.findSparKontoByKundennummer(authKundennummer);
    Kunde kunde = kundeService.findByKundennummer(authKundennummer);
    sparKontenListe.forEach(sparKonto -> sparKonto.setKontoBuchungList(kontoBuchungService.findByKontoId(sparKonto.getId())));
    log.debug("loaded SparKonten: " + sparKontenListe);
    log.debug("loaded Kunde: " + kunde.getKundennummer() + "(auth=" + authKundennummer + ")");

    model.addAttribute("kunde", kunde);
    model.addAttribute("sparKontenListe",sparKontenListe);
    model.addAttribute("activeLink", "kundeBankingSparenUebersicht");


    return "kunde/banking/sparen/sparkontouebersicht";
  }


}
