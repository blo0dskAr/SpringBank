package at.blo0dy.SpringBank.controller.banking.kredit;

import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import at.blo0dy.SpringBank.service.konto.kredit.KreditKontoAntragService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

}
