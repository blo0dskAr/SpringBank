package at.blo0dy.SpringBank.controller.mitarbeiter.crm;

import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.service.konto.KontoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/mitarbeiter/kunde/")
public class KontoSucheController {

  KontoService kontoService;

  @Autowired
  public KontoSucheController(KontoService kontoService) {
    this.kontoService = kontoService;
  }


  @GetMapping("/kontosuche")
  public String showKontoSucheForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

    Konto konto = new Konto();

    model.addAttribute("konto",konto);

    log.debug("Showing KontoSuche Page for Mitarbeiter: " + authentication.getName());
    return "mitarbeiter/crm/kontosuche";
  }


@PostMapping("/kontosuche")
  public String processKontoSucheForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                      @ModelAttribute Konto konto, Model model) {

  log.debug("Loading Results on KontoSuche Page for Mitarbeiter: " + authentication.getName());
  List<Konto> ergebnis = kontoService.findAll(konto);

  model.addAttribute("ergebnis", ergebnis);

  log.debug("Showing Results on KontoSuche Page for Mitarbeiter: " + authentication.getName() + " " + ergebnis.size() + " results returned.");
  return "mitarbeiter/crm/kontosuche";
  }


}
