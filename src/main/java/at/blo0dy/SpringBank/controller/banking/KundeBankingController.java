package at.blo0dy.SpringBank.controller.banking;

import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.giro.GiroKontoAntragService;
import at.blo0dy.SpringBank.service.konto.giro.GiroService;
import at.blo0dy.SpringBank.service.konto.kredit.KreditKontoAntragService;
import at.blo0dy.SpringBank.service.konto.kredit.KreditService;
import at.blo0dy.SpringBank.service.konto.sparen.SparKontoAntragService;
import at.blo0dy.SpringBank.service.konto.sparen.SparService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/kunde/banking")
public class KundeBankingController {

  GiroService giroService;
  KreditService kreditService;
  SparService sparService;
  GiroKontoAntragService giroKontoAntragService;
  KreditKontoAntragService kreditKontoAntragService;
  SparKontoAntragService sparKontoAntragService;
  KundeService kundeService;


  @Autowired
  public KundeBankingController(GiroService giroService, KreditService kreditService, SparService sparService, GiroKontoAntragService giroKontoAntragService, KreditKontoAntragService kreditKontoAntragService, SparKontoAntragService sparKontoAntragService, KundeService kundeService) {
    this.giroService = giroService;
    this.kreditService = kreditService;
    this.sparService = sparService;
    this.giroKontoAntragService = giroKontoAntragService;
    this.kreditKontoAntragService = kreditKontoAntragService;
    this.sparKontoAntragService = sparKontoAntragService;
    this.kundeService = kundeService;
  }

  @RequestMapping({"", "/", "/index"})
  public String getIndexPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

    // Login Workaround, irgendwie billig, aber es funktioniert (null pointer exception fliegt, scheinbar passt was ned mit der authenticationPage und dem loginForwardingUrl config ned...
    if (authentication==null) {
      return "redirect:/kunde/banking/index";
    }

    String authKundennummer = authentication.getName();

    BigDecimal summeOffenerKonten = kundeService.getSummeOffenerKontenByKundennummer(authKundennummer);


    List<SparKontoAntrag> sparKontoAntragListe = sparKontoAntragService.findSparAntraegeByKundennummer(authKundennummer);
    List<KreditKontoAntrag> kreditKontoAntragListe = kreditKontoAntragService.findKreditAntraegeByKundennummer(authKundennummer);
    List<GiroKontoAntrag> giroKontoAntragListe = giroKontoAntragService.findGiroAntraegeByKundennummer(authKundennummer);
    List<SparKonto> sparKontenListe = sparService.findSparKontoByKundennummer(authKundennummer);
/*    List<KreditKonto> kreditKontenListe = kreditService.findKreditKontenByKundennummer(authKundennummer);
    List<GiroKonto> giroKontenListe = giroService.findGiroKontenByKundennummer(authKundennummer);*/



    Kunde kunde = kundeService.findByKundennummer(authKundennummer);

//    List<SparKonto> sparKontenListe = sparService.findSparKontoByKunde(kunde);

    log.debug("SPARKONTENLISTE: ------> " + sparKontenListe);

    model.addAttribute("activeLink", "KundeBankingHome");
    model.addAttribute("kunde", kunde);
    model.addAttribute("sparkontoantragliste", sparKontoAntragListe);
    model.addAttribute("kreditkontoantragliste", kreditKontoAntragListe);
    model.addAttribute("girokontoantragliste", giroKontoAntragListe);
    model.addAttribute("sparkontenliste", sparKontenListe);
/*    model.addAttribute("kreditkontenliste", kreditKontenListe);
    model.addAttribute("girokontenliste", giroKontenListe);*/
    model.addAttribute("summeoffenerkonten", summeOffenerKonten);

    return "kunde/banking/index";
  }

}
