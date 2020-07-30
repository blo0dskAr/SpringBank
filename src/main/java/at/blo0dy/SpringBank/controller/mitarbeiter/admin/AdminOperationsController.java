package at.blo0dy.SpringBank.controller.mitarbeiter.admin;

import at.blo0dy.SpringBank.model.konto.dauerauftrag.DauerAuftrag;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import at.blo0dy.SpringBank.service.konto.dauerauftrag.DauerAuftragService;
import at.blo0dy.SpringBank.service.konto.zahlungsAuftrag.ZahlungsAuftragService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;


@Controller
@Slf4j
@RequestMapping("/mitarbeiter/admin/operations")
public class AdminOperationsController {

  ZahlungsAuftragService zahlungsAuftragService;
  DauerAuftragService dauerAuftragService;


  @Autowired
  public AdminOperationsController(ZahlungsAuftragService zahlungsAuftragService, DauerAuftragService dauerAuftragService) {
    this.zahlungsAuftragService = zahlungsAuftragService;
    this.dauerAuftragService = dauerAuftragService;
  }

  @GetMapping("/batchAdministration")
  public String showBatchAdministrationPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

    String loginName = authentication.getName();
    log.debug("BatchAdministration Page von Mitarbeiter: " + loginName + "aufgerufen.");

    ZahlungsAuftrag zahlungsAuftrag = new ZahlungsAuftrag();
    zahlungsAuftrag.setBetrag(BigDecimal.ONE);
    zahlungsAuftrag.setKontonummer("AT121234123412341234");

    DauerAuftrag dauerAuftrag = new DauerAuftrag();
    dauerAuftrag.setBetrag(BigDecimal.ONE);
    dauerAuftrag.setKontonummer("AT121234123412341234");
    dauerAuftrag.setTagImMonat(LocalDate.now().getDayOfMonth());
    dauerAuftrag.setText("dummy");

    model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);
    model.addAttribute("dauerAuftrag", dauerAuftrag);

    return "admin/admin-batch-page";
  }


  @PostMapping("/batchAdministration/processZahlungsAuftrag")
  public String processZahlungsAuftrag(@CurrentSecurityContext(expression = "authentication")Authentication authentication,
                                       @Valid @ModelAttribute ZahlungsAuftrag zahlungsAuftrag, BindingResult result, Model model,
                                        RedirectAttributes redirectAttrs) {
    String loginName = authentication.getName();
    log.debug("Requesting processZahlungsAuftragsBatch von Mitarbeiter: " + loginName);

    if (result.hasErrors()) {
      log.debug("Fehler beim Durchführen des ZahlungsAuftragsBatches Mitarbeiter: " + authentication.getName() + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      log.debug("ERRORS: " + result.getAllErrors());
      redirectAttrs.addFlashAttribute("fehlerAufgetreten", "Fehler aufgetreten");

      return "redirect:/mitarbeiter/admin/operations/batchAdministration";
    }

    String ergebnis = zahlungsAuftragService.processZahlungsAuftragsBatch(zahlungsAuftrag);
    log.debug("processZahlungsAuftragsBatch erfolgreich durchgeführt");
    redirectAttrs.addFlashAttribute("ergebnis", ergebnis);

    return "redirect:/mitarbeiter/admin/operations/batchAdministration";
  }




  @PostMapping("/batchAdministration/processDauerAuftrag")
  public String processDauerAuftrag(@CurrentSecurityContext(expression = "authentication")Authentication authentication,
                                    @Valid @ModelAttribute DauerAuftrag dauerAuftrag, BindingResult result, Model model,
                                    RedirectAttributes redirectAttrs) {
    String loginName = authentication.getName();
    log.debug("Requesting processDauerAuftragsBatch von Mitarbeiter: " + loginName);

    if (result.hasErrors()) {
      log.debug("Fehler beim Durchführen des DauerAuftragsBatches Mitarbeiter: " + authentication.getName() + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      log.debug("ERRORS: " + result.getAllErrors());
      redirectAttrs.addFlashAttribute("fehlerAufgetreten", "Fehler aufgetreten");

      return "redirect:/mitarbeiter/admin/operations/batchAdministration";
    }

    dauerAuftrag.setText(dauerAuftrag.getAuftragsArt().getDisplayName());

    String ergebnis = dauerAuftragService.processDauerAuftragsBatch(dauerAuftrag);
    log.debug("processDauerAuftragsBatch erfolgreich durchgeführt");
    redirectAttrs.addFlashAttribute("ergebnis", ergebnis);

    return "redirect:/mitarbeiter/admin/operations/batchAdministration";
  }






}
