package at.blo0dy.SpringBank.controller.mitarbeiter.crm.giro;

import at.blo0dy.SpringBank.model.antrag.KontoAntrag;
import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import at.blo0dy.SpringBank.model.enums.AntragStatusEnum;
import at.blo0dy.SpringBank.model.enums.KontoProduktEnum;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import at.blo0dy.SpringBank.model.konto.kontoBuchung.KontoBuchung;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.KontoAntragService;
import at.blo0dy.SpringBank.service.konto.KontoService;
import at.blo0dy.SpringBank.service.konto.giro.GiroKontoAntragService;
import at.blo0dy.SpringBank.service.konto.giro.GiroService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("mitarbeiter/kunde/giro")
public class CrmGiroAntragController {

  GiroKontoAntragService giroKontoAntragService;
  GiroService giroService;
  KundeService kundeService;
  KontoService kontoService;
  KontoAntragService kontoAntragService;

  @Autowired
  public CrmGiroAntragController(GiroKontoAntragService giroKontoAntragService, GiroService giroService, KundeService kundeService, KontoService kontoService, KontoAntragService kontoAntragService) {
    this.giroKontoAntragService = giroKontoAntragService;
    this.giroService = giroService;
    this.kundeService = kundeService;
    this.kontoService = kontoService;
    this.kontoAntragService = kontoAntragService;
  }

  @GetMapping("/antragBearbeitung")
  public String showGiroAntragBearbeitungsPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {
    log.debug("Showing GiroAntragBearbeitungsPage for Mitarbeiter: " + authentication.getName());

    KontoAntrag kontoAntrag = new KontoAntrag();
    kontoAntrag.setProdukt(KontoProduktEnum.GIRO);
    kontoAntrag.setAntragStatus(AntragStatusEnum.EINGEREICHT);

    model.addAttribute("kontoantrag", kontoAntrag);

    List<KontoAntrag> ergebnis = kontoAntragService.findAll(kontoAntrag);

    model.addAttribute("ergebnis", ergebnis);

    return "mitarbeiter/crm/antragsuche";
  }

  @PostMapping("/antragBearbeitung")
  public String showGiroAntragBearbeitungsPageErg(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                                  @ModelAttribute KontoAntrag kontoAntrag) {
    log.debug("Showing GiroKontoAntragsPage for Mitarbeiter: " + authentication.getName());

    List<KontoAntrag> ergebnis = kontoAntragService.findAll(kontoAntrag);

    model.addAttribute("ergebnis", ergebnis);
    model.addAttribute("kontoantrag", kontoAntrag);

    return "mitarbeiter/crm/antragsuche";
  }

  @GetMapping("/antrag/showGiroAntragForKontoForm")
  public String showGiroAntragForKontoForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                           @RequestParam("giroKontoAntragId") Long giroKontoAntragId, Model model) {
    String loginName = authentication.getName();
    log.debug("Showing GiroAntrag2KontoForm für GiroAntragId: " + giroKontoAntragId + " für Mitarbeiter: " + loginName);

    GiroKontoAntrag giroKontoAntrag = giroKontoAntragService.findById(giroKontoAntragId);
    Kunde kunde = kundeService.findByKundennummer(giroKontoAntrag.getKundennummer().toString());

    model.addAttribute("giroKontoAntrag", giroKontoAntrag);
    model.addAttribute("kunde", kunde);

    return "mitarbeiter/crm/giro/giroKontoAntrag2Konto";
  }


  @PostMapping("/antrag/saveGiroAntrag2KontoForm")
  public String saveGiroAntrag2KontoForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                         @Valid @ModelAttribute("giroKontoAntrag") GiroKontoAntrag giroKontoAntrag,
                                         @ModelAttribute("kunde") Kunde kunde, Model model, RedirectAttributes redirectAttrs) {

    String loginName = authentication.getName();
    log.debug("GiroKontoAntrag id=" + giroKontoAntrag.getId() + ", KundeNr:" + giroKontoAntrag.getKundennummer() + " soll von MitarbeiterId: " + loginName + " gespeichert werden." );

    final Kunde mykunde = kundeService.findByKundennummer(giroKontoAntrag.getKundennummer().toString());
    final KontoStatusEnum bestMoeglicherStatus = kundeService.getBestmoeglicherKontoStatusByKundennummer(kunde.getKundennummer());

    if (giroKontoAntrag.getAntragStatus().equals(AntragStatusEnum.ABGELEHNT) || giroKontoAntrag.getAntragStatus().equals(AntragStatusEnum.ABGELEHNT_WEIL_NEU_BERECHNET)) {
      redirectAttrs.addFlashAttribute("antragAbgelehnt", true);
    }

    if (giroKontoAntrag.getAntragStatus().equals(AntragStatusEnum.GENEHMIGT))  {
      log.debug("GiroKontoAntrag wurde genehmigt, GiroKonto wird erstellt");
      BigDecimal ueberziehungsrahmen;
//      if (giroKontoAntrag.isUeberziehungsrahmenGewuenscht()) {
//        ueberziehungsrahmen = BigDecimal.valueOf(500);
//      } else {
//        ueberziehungsrahmen = BigDecimal.ZERO;
//      }
      GiroKonto giroKonto = new GiroKonto(LocalDateTime.now(), kundeService.generateNewKontonummerByKundennummer(kunde.getKundennummer()), mykunde, BigDecimal.ZERO,
                                          KontoStatusEnum.IN_EROEFFNUNG, giroKontoAntrag, BigDecimal.ZERO, new ArrayList<KontoBuchung>(), KontoProduktEnum.GIRO);
      log.debug("Girokonto wurde erstellt, Girokonto wird gespeichert.)");
      giroService.save(giroKonto);
      log.debug("Girokonto wurde erfolgreich gespeichert. (id=" + giroKonto.getId() + ")");

      if (bestMoeglicherStatus.equals(KontoStatusEnum.IN_EROEFFNUNG)) {
        log.debug("Girokonto mit der ID=" + giroKonto.getId() + " Kann nicht eröffnet werden. BestMöglicher Status voerst erreicht");
        redirectAttrs.addFlashAttribute("noChanges", true);
      } else {
        log.debug("Gespeichertes Girokonto mit der ID=" + giroKonto.getId() + " wird auf Mögliche KontoEröffnung geprüft:");
        String processErgebnis = kontoService.processKontoStatusById(giroKonto.getId(), bestMoeglicherStatus, bestMoeglicherStatus);

        switch(processErgebnis) {
          case "NO_CHANGES":
            redirectAttrs.addFlashAttribute("noChanges", true);
            break;
          case "TRANSITION_NOT_POSSIBLE":
            redirectAttrs.addFlashAttribute("transitionNotPossible", true);
            break;
          case "KONTO_NOW_OPEN":
            redirectAttrs.addFlashAttribute("kontoOpened", true);
            break;
        }
      }
    }

    log.debug("SparKontoAntrag wird gespeichert");
    giroKontoAntragService.save(giroKontoAntrag);
    log.debug("SparKontoAntrag wurde erfolgreich gespeichert");

    return "redirect:/mitarbeiter/kunde/giro/antragBearbeitung";
  }

}














