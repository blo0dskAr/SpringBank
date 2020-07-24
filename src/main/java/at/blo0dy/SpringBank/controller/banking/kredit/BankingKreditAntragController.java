package at.blo0dy.SpringBank.controller.banking.kredit;

import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import at.blo0dy.SpringBank.service.konto.kredit.KreditKontoAntragService;
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
@RequestMapping("/kunde/banking/kredit")
public class BankingKreditAntragController {

  KreditKontoAntragService kreditKontoAntragService;

  @Autowired
  public BankingKreditAntragController(KreditKontoAntragService kreditKontoAntragService) {
    this.kreditKontoAntragService = kreditKontoAntragService;
  }

  @GetMapping("/showKreditAntragDetailPage")
  public String showKreditAntragDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                           @RequestParam("antragId") Long antragId, RedirectAttributes redirectAttrs) {

    String authKundennummer = authentication.getName();
    log.debug("Showing showKreditAntragDetailPage for Kunde: " + authKundennummer + " and Antrag: " + antragId );

    KreditKontoAntrag kreditKontoAntrag = kreditKontoAntragService.findKreditAntragByAntragIdAndKundennummer(antragId, authKundennummer);

    log.debug("Check ob ID: " + antragId + " des Antrages bei Kunde: " + authKundennummer + " liegt.");
    if (kreditKontoAntrag == null) {
      log.error("Check ob ID: " + antragId + " des Antrages bei Kunde: " + authKundennummer + " liegt. - FEHLGESCHLAGEN");
      redirectAttrs.addFlashAttribute("beschissError", true);

      return "redirect:/kunde/banking/kredit/kreditkontouebersicht";
    }
    log.debug("Check ob ID: " + antragId + " des Antrages bei Kunde: " + authKundennummer + " liegt. - ERFOLGREICH");
    model.addAttribute("kreditkontoantrag", kreditKontoAntrag);

    return "kunde/banking/kredit/antrag-detail";
  }


  @PostMapping("/saveKreditAntragDetailPage")
  public String saveKreditAntragDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                            @Valid @ModelAttribute("kreditkontoantrag") KreditKontoAntrag kreditKontoAntrag, BindingResult result,
                                            RedirectAttributes redirectAttrs) {

    String authKundennummer = authentication.getName();

    if (result.hasErrors()) {
      log.warn("Fehler beim speichern eines KreditkontoAntrags für Kunde: " + authKundennummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      model.addAttribute("kreditkontoantrag", kreditKontoAntrag);

      return "kunde/banking/kredit/antrag-detail";
    }

    log.debug("KreditkontoAntrag: " +  kreditKontoAntrag.getId() + " zu Kunde: " + authKundennummer + " wird gespeichert" );
    kreditKontoAntragService.save(kreditKontoAntrag);
    log.debug("KreditkontoAntrag: " +  kreditKontoAntrag.getId() + " zu Kunde: " + authKundennummer + " wurde erfolgreich gespeichert" );

    redirectAttrs.addFlashAttribute("antragGespeichert", true);
    return "redirect:/kunde/banking/index";
  }

}
