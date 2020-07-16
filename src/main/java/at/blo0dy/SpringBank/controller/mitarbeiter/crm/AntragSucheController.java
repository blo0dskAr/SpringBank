package at.blo0dy.SpringBank.controller.mitarbeiter.crm;

import at.blo0dy.SpringBank.model.antrag.KontoAntrag;
import at.blo0dy.SpringBank.service.konto.KontoAntragService;
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

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/mitarbeiter/kunde")
public class AntragSucheController {

  KontoAntragService kontoAntragService;

  @Autowired
  public AntragSucheController(KontoAntragService kontoAntragService) {
    this.kontoAntragService = kontoAntragService;
  }

  @GetMapping("/antragsuche")
  public String showAntragSucheForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

    KontoAntrag kontoAntrag = new KontoAntrag();

    model.addAttribute("kontoantrag", kontoAntrag);

    return "mitarbeiter/crm/antragsuche";
  }

  @PostMapping("/antragsuche")
  public String processAntragSucheForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                       @ModelAttribute(name = "kontoantrag") KontoAntrag kontoAntrag, Model model) {

    log.debug("Loading Results on AntragSuche Page for Mitarbeiter: " + authentication.getName());

    List<KontoAntrag> ergebnis = kontoAntragService.findAll(kontoAntrag);

    model.addAttribute("ergebnis", ergebnis);
    log.debug("Showing Results on AntragSuche Page for Mitarbeiter: " + authentication.getName() + " " + ergebnis.size() + " results returned.");


    return "mitarbeiter/crm/antragsuche";

  }




}
