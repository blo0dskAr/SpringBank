package at.blo0dy.SpringBank.controller.mitarbeiter.crm.sparen;

import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.sparen.SparService;
import at.blo0dy.SpringBank.service.konto.zahlungsAuftrag.ZahlungsAuftragService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("mitarbeiter/kunde/sparen")
public class CrmSparKontoController {

  SparService sparService;
  KundeService kundeService;
  ZahlungsAuftragService zahlungsAuftragService;

  @Autowired
  public CrmSparKontoController(KundeService kundeService, SparService sparService, ZahlungsAuftragService zahlungsAuftragService) {
    this.kundeService = kundeService;
    this.sparService = sparService;
    this.zahlungsAuftragService = zahlungsAuftragService;
  }


  @GetMapping("/konto")
  public String showSparKontoPage() {

    return "mitarbeiter/crm/sparen/sparKonto";
  }


  @GetMapping("/showSparKontoDetailPage")
  public String showSparKontoDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                        @RequestParam("sparKontoId") Long sparKontoId) {

    log.debug("Showing SparKontoDetailPage for Mitarbeiter: " + authentication.getName() + " und KontoId: " + sparKontoId);

    Konto sparkonto = sparService.findById(sparKontoId);
    Kunde kunde = sparkonto.getKunde();

    model.addAttribute("sparkonto", sparkonto);
    model.addAttribute("kunde", kunde);
    model.addAttribute("countOffeneZA",zahlungsAuftragService.countOffeneZahlungsAuftraegeByKontoId(sparKontoId));

    return "mitarbeiter/crm/sparen/konto-detail";

  }




}
