package at.blo0dy.SpringBank.controller.mitarbeiter.crm.giro;

import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.giro.GiroService;
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
@RequestMapping("mitarbeiter/kunde/giro")
public class CrmGiroKontoController {

  GiroService giroService;
  KundeService kundeService;
  ZahlungsAuftragService zahlungsAuftragService;

  @Autowired
  public CrmGiroKontoController(KundeService kundeService, GiroService giroService, ZahlungsAuftragService zahlungsAuftragService) {
    this.kundeService = kundeService;
    this.giroService = giroService;
    this.zahlungsAuftragService = zahlungsAuftragService;
  }


  @GetMapping("/konto")
  public String showGiroKontoPage() {

    return "mitarbeiter/crm/giro/giroKonto";
  }


  @GetMapping("/konto/showGiroKontoDetailPage")
  public String showGiroKontoDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                        @RequestParam("giroKontoId") Long giroKontoId) {

    log.debug("Showing GiroKontoDetailPage for Mitarbeiter: " + authentication.getName() + " und KontoId: " + giroKontoId);

    Konto girokonto = giroService.findById(giroKontoId);
    Kunde kunde = girokonto.getKunde();

    model.addAttribute("girokonto", girokonto);
    model.addAttribute("kunde", kunde);
    model.addAttribute("countOffeneZA",zahlungsAuftragService.countOffeneZahlungsAuftraegeByKontoId(giroKontoId));

    return "mitarbeiter/crm/giro/konto-detail";

  }


}
