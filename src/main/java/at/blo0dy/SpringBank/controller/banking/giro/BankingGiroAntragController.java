package at.blo0dy.SpringBank.controller.banking.giro;

import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import at.blo0dy.SpringBank.service.konto.giro.GiroKontoAntragService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/kunde/banking/giro")
public class BankingGiroAntragController {

  GiroKontoAntragService giroKontoAntragService;

  @Autowired
  public BankingGiroAntragController(GiroKontoAntragService giroKontoAntragService) {
    this.giroKontoAntragService = giroKontoAntragService;
  }

  @GetMapping("/showGiroAntragDetailPage")
  public String showGiroAntragDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                         @RequestParam("antragId") Long antragId, RedirectAttributes redirectAttrs) {

    String authKundennummer = authentication.getName();
    log.debug("Showing showGiroAntragDetailPage for Kunde: " + authKundennummer + " and Antrag: " + antragId );

    GiroKontoAntrag giroKontoAntrag = giroKontoAntragService.findGiroAntragByAntragIdAndKundennummer(antragId, authKundennummer);

    log.debug("Check ob ID: " + antragId + " des Antrages bei Kunde: " + authKundennummer + " liegt.");
    if (giroKontoAntrag == null) {
      log.error("Check ob ID: " + antragId + " des Antrages bei Kunde: " + authKundennummer + " liegt. - FEHLGESCHLAGEN");
      redirectAttrs.addFlashAttribute("beschissError", true);

      return "redirect:/kunde/banking/giro/girokontouebersicht";
    }
    log.debug("Check ob ID: " + antragId + " des Antrages bei Kunde: " + authKundennummer + " liegt. - ERFOLGREICH");
    model.addAttribute("girokontoantrag", giroKontoAntrag);

    return "kunde/banking/giro/antrag-detail";
  }


  @PostMapping("/saveGiroAntragDetailPage")
  public String saveGiroAntragDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                         @Valid @ModelAttribute("girokontoantrag") GiroKontoAntrag giroKontoAntrag, BindingResult result,
                                         RedirectAttributes redirectAttrs) {
    String authKundennummer = authentication.getName();
    log.debug("GirokontoAntrag: " +  giroKontoAntrag.getId() + " zu Kunde: " + authKundennummer + " wird gespeichert" );

    if (result.hasErrors()) {
      log.warn("Fehler beim speichern eines GirokontoAntrags für Kunde: " + authKundennummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      model.addAttribute("girokontoantrag", giroKontoAntrag);

      return "kunde/banking/giro/antrag-detail";
    }

    giroKontoAntragService.save(giroKontoAntrag);
    log.debug("GirokontoAntrag: " +  giroKontoAntrag.getId() + " zu Kunde: " + authKundennummer + " wurde erfolgreich gespeichert" );

    redirectAttrs.addFlashAttribute("antragGespeichert", true);
    return "redirect:/kunde/banking/index";
  }



}
