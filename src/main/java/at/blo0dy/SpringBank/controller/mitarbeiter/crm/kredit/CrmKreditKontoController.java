package at.blo0dy.SpringBank.controller.mitarbeiter.crm.kredit;

import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.kredit.KreditService;
import at.blo0dy.SpringBank.service.konto.zahlungsAuftrag.ZahlungsAuftragService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("mitarbeiter/kunde/kredit")
public class CrmKreditKontoController {

  KreditService kreditService;
  KundeService kundeService;
  ZahlungsAuftragService zahlungsAuftragService;

  @Autowired
  public CrmKreditKontoController(KundeService kundeService, KreditService kreditService, ZahlungsAuftragService zahlungsAuftragService) {
    this.kundeService = kundeService;
    this.kreditService = kreditService;
    this.zahlungsAuftragService = zahlungsAuftragService;
  }


  @GetMapping("/konto")
  public String showKreditKontoPage() {

    return "mitarbeiter/crm/kredit/kreditKonto";
  }


  @GetMapping("/konto/showKreditKontoDetailPage")
  public String showKreditKontoDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                        @RequestParam("kreditKontoId") Long kreditKontoId) {

    log.debug("Showing KreditKontoDetailPage for Mitarbeiter: " + authentication.getName() + " und KontoId: " + kreditKontoId);

    Konto kreditkonto = kreditService.findById(kreditKontoId);
    Kunde kunde = kreditkonto.getKunde();

    model.addAttribute("kreditkonto", kreditkonto);
    model.addAttribute("kunde", kunde);
    model.addAttribute("countOffeneZA",zahlungsAuftragService.countOffeneZahlungsAuftraegeByKontoId(kreditKontoId));

    return "mitarbeiter/crm/kredit/konto-detail";

  }


}
