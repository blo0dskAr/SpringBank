package at.blo0dy.SpringBank.controller.banking.sparen;

import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import at.blo0dy.SpringBank.service.konto.sparen.SparKontoAntragService;
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
@RequestMapping("/kunde/banking/sparen")
public class BankingSparAntragController {

  SparKontoAntragService sparKontoAntragService;

  @Autowired
  public BankingSparAntragController(SparKontoAntragService sparKontoAntragService) {
    this.sparKontoAntragService = sparKontoAntragService;
  }

  @GetMapping("/showSparAntragDetailPage")
  public String showSparAntragDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                         @RequestParam("antragId") Long antragId, RedirectAttributes redirectAttrs) {

    String authKundennummer = authentication.getName();
    log.debug("Showing showSparAntragDetailPage for Kunde: " + authKundennummer + " and Antrag: " + antragId );

    SparKontoAntrag sparKontoAntrag = sparKontoAntragService.findSparAntragByAntragIdAndKundennummer(antragId, authKundennummer);

    log.debug("Check ob ID: " + antragId + " des Antrages bei Kunde: " + authKundennummer + " liegt.");
    if (sparKontoAntrag == null) {
      log.error("Check ob ID: " + antragId + " des Antrages bei Kunde: " + authKundennummer + " liegt. - FEHLGESCHLAGEN");
      redirectAttrs.addFlashAttribute("beschissError", true);

      return "redirect:/kunde/banking/sparen/sparkontouebersicht";
    }
    log.debug("Check ob ID: " + antragId + " des Antrages bei Kunde: " + authKundennummer + " liegt. - ERFOLGREICH");
    model.addAttribute("sparkontoantrag", sparKontoAntrag);

    return "kunde/banking/sparen/antrag-detail";
  }


  @PostMapping("/saveSparAntragDetailPage")
  public String saveSparAntragDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                         @Valid @ModelAttribute("sparkontoantrag") SparKontoAntrag sparKontoAntrag, BindingResult result,
                                         RedirectAttributes redirectAttrs) {

    String authKundennummer = authentication.getName();

    if (result.hasErrors()) {
      log.warn("Fehler beim speichern eines SparkontoAntrags für Kunde: " + authKundennummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      model.addAttribute("sparkontoantrag", sparKontoAntrag);

      return "kunde/banking/sparen/antrag-detail";
    }

    log.debug("SparkontoAntrag: " +  sparKontoAntrag.getId() + " zu Kunde: " + authKundennummer + " wird gespeichert" );
    sparKontoAntragService.save(sparKontoAntrag);
    log.debug("SparkontoAntrag: " +  sparKontoAntrag.getId() + " zu Kunde: " + authKundennummer + " wurde erfolgreich gespeichert" );

    redirectAttrs.addFlashAttribute("antragGespeichert", true);
    return "redirect:/kunde/banking/index";
  }

}
