package at.blo0dy.SpringBank.controller.mitarbeiter.admin;

import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import at.blo0dy.SpringBank.service.konto.dauerauftrag.DauerAuftragService;
import at.blo0dy.SpringBank.service.konto.zahlungsAuftrag.ZahlungsAuftragService;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
  public String showBatchAdministrationPage(Model model) {

    ZahlungsAuftrag zahlungsAuftrag = new ZahlungsAuftrag();
    zahlungsAuftrag.setBetrag(BigDecimal.ONE);
    zahlungsAuftrag.setKontonummer("AT121234123412341234");

    model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);

    return "admin/admin-batch-page";
  }


  @PostMapping("/batchAdministration/processZahlungsAuftrag")
  public String processZahlungsAuftrag(@CurrentSecurityContext(expression = "authentication")Authentication authentication,
                                       @Valid @ModelAttribute ZahlungsAuftrag zahlungsAuftrag, BindingResult result, Model model) {

    System.out.println(zahlungsAuftrag);

    if (result.hasErrors()) {
      log.debug("Fehler beim Durchführen des ZahlungsAuftragsBatches Mitarbeiter: " + authentication.getName() + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      log.debug(String.valueOf(result.getAllErrors()));

      return "admin/admin-batch-page";
    }

    log.debug("Requesting processZahlungsAuftragsBatch");
    zahlungsAuftragService.processZahlungsAuftragsBatch(zahlungsAuftrag);
    log.debug("processZahlungsAuftragsBatch erfolgreich durchgeführt");

    return "admin/admin-batch-page";
  }






}
