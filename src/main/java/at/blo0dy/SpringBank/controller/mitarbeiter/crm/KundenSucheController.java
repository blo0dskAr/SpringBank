package at.blo0dy.SpringBank.controller.mitarbeiter.crm;

import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/mitarbeiter/kunde")
public class KundenSucheController {

  KundeService kundeService;

  @Autowired
  public KundenSucheController(KundeService kundeService) {
    this.kundeService = kundeService;
  }

  @GetMapping("/kundensuche")
  public String showKundenSucheForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                    Model model) {

    Kunde kunde = new Kunde();

    model.addAttribute("kunde", kunde);

    log.debug("Showing KundenSuche Page for Mitarbeiter: " + authentication.getName());
    return "mitarbeiter/crm/kundensuche";
  }

  @PostMapping("/kundensuche")
  public String processKundenSucheForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                       @ModelAttribute Kunde kunde, Model model) {

    log.debug("Loading Results on KundeSuche Page for Mitarbeiter: " + authentication.getName());

    ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues()
            .withMatcher("kundennummer", match -> match.contains().ignoreCase())
            .withMatcher("vorname", match -> match.contains().ignoreCase())
            .withMatcher("nachname", match -> match.contains().ignoreCase())
            .withIgnorePaths("isLegi","hasAcceptedAGB","isActive","firstLoginDone");

    List<Kunde> ergebnis = kundeService.findAll(kunde, matcher);

    model.addAttribute("ergebnis", ergebnis);
    log.debug("Showing Results on KundeSuche Page for Mitarbeiter: " + authentication.getName() + " " + ergebnis.size() + " results returned.");

    return "mitarbeiter/crm/kundensuche";
  }





}
