package at.blo0dy.SpringBank.controller.banking.giro;


import at.blo0dy.SpringBank.dao.KundeRepository;
import at.blo0dy.SpringBank.dao.konto.giro.GiroKontoAntragRepository;
import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoRegistrationForm;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.giro.GiroKontoAntragService;
import at.blo0dy.SpringBank.service.konto.giro.GiroService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Controller
@RequestMapping("/kunde/banking/giro")
public class GiroKontoAntragRegistrationController {

  private GiroKontoAntragService giroKontoAntragService;
  private GiroService giroService;
  private KundeService kundeService ;


  @Autowired
  public GiroKontoAntragRegistrationController(GiroKontoAntragService giroKontoAntragService, GiroService giroService, KundeService kundeService) {
    this.giroKontoAntragService = giroKontoAntragService;
    this.giroService = giroService;
    this.kundeService = kundeService;
  }

  @GetMapping("/register")
  public String registerForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication , Model model) {

    String kundennummer = authentication.getName();
    model.addAttribute("kundennummer", kundennummer);

    GiroKontoAntrag giroKontoAntrag = new GiroKontoAntrag();

    model.addAttribute("girokontoantrag", giroKontoAntrag);
    model.addAttribute("activeLink", "kundeBankingGiroForm");

    return "/kunde/banking/giro/registration";
  }

  @PostMapping("/register")
  public String processRegistration(@ModelAttribute("girokontoantrag") GiroKontoRegistrationForm form, RedirectAttributes redirectAttrs) {

    log.debug("GiroKontoRegistrationForm erhalten: " + form);
    Kunde tmpKunde = kundeService.findByKundennummer(form.getKundennummer().toString());

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    form.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));

    log.debug("GiroKontoAntragRegistrationController: GiroKontoRegistrationForm wird als GirokontoAntrag gespeichert");

    // Anzahl der Konten und Anträge prüfen
    int anzahlAktiveGiroKonten = giroService.countAktiveKontenByKundeId(tmpKunde.getId());
    int anzahlEingereichtGiroAntraege = giroKontoAntragService.countEingereichteGiroAntraegeByKundennummer(tmpKunde.getKundennummer());

    if (anzahlAktiveGiroKonten + anzahlEingereichtGiroAntraege >= 2) {
      redirectAttrs.addFlashAttribute("zuVieleAktive", true);
      log.debug("GiroKontoRegistrationForm Konnte nicht gespeichert werden, bereits zu viele Aktive Aäntrge");
    } else {
      giroKontoAntragService.save(form.toGiroKontoAntrag());
      log.debug("GiroKontoRegistrationForm wurde erfolgreich als GirokontoAntrag gespeichert");
      redirectAttrs.addFlashAttribute("antragGespeichert", true);
    }

    return "redirect:/kunde/banking/index";
  }

}
