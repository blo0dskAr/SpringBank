package at.blo0dy.SpringBank.controller.mitarbeiter.crm.sparen;

import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import at.blo0dy.SpringBank.model.enums.AntragStatusEnum;
import at.blo0dy.SpringBank.model.enums.KontoProduktEnum;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.kontoBuchung.KontoBuchung;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.KontoService;
import at.blo0dy.SpringBank.service.konto.sparen.SparKontoAntragService;
import at.blo0dy.SpringBank.service.konto.sparen.SparService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@Controller
@RequestMapping("mitarbeiter/kunde/sparen")
public class CrmSparAntragController {

  SparKontoAntragService sparKontoAntragService;
  SparService sparService;
  KundeService kundeService;
  KontoService kontoService;

  @Autowired
  public CrmSparAntragController(SparKontoAntragService sparKontoAntragService, SparService sparService, KundeService kundeService, KontoService kontoService) {
    this.sparKontoAntragService = sparKontoAntragService;
    this.sparService = sparService;
    this.kundeService = kundeService;
    this.kontoService = kontoService;
  }

  @GetMapping("/antrag")
  public String showSparAntragPage(Model model) {

    model.addAttribute("gesamtAnzahl",sparKontoAntragService.count());
    model.addAttribute("anzahlGenehmigt",sparKontoAntragService.countByStatus("GENEHMIGT"));
    model.addAttribute("anzahlAbgelehnt",sparKontoAntragService.countByStatus("ABGELEHNT"));
    model.addAttribute("anzahlEingereicht",sparKontoAntragService.countByStatus("EINGEREICHT"));
    model.addAttribute("offeneAntragListe", sparKontoAntragService.findByStatus("EINGEREICHT"));


    return "mitarbeiter/crm/sparen/sparAntrag";
  }

  @GetMapping("/antrag/showSparAntragForKontoForm")
  public String showSparAntrag2KontoForm(@RequestParam("sparKontoAntragId") Long sparKontoAntragId, Model model) {

    SparKontoAntrag sparKontoAntrag = sparKontoAntragService.findById(sparKontoAntragId);
    Kunde kunde = kundeService.findByKundennummer(sparKontoAntrag.getKundennummer().toString());
    log.debug("showSparAntragForKontoForm für id=" + sparKontoAntragId + ", KundeNr= " + kunde.getKundennummer() + " wird angezeigt");
//    sparKontoAntrag.setId(sparKontoAntragId);

    model.addAttribute("sparKontoAntrag", sparKontoAntrag);
    model.addAttribute("kunde", kunde);

    return "mitarbeiter/crm/sparen/sparKontoAntrag2Konto";
  }


  @PostMapping("/antrag/saveSparAntrag2KontoForm")
  public String saveSparAntrag2KontoForm(@Valid @ModelAttribute("sparKontoAntrag") SparKontoAntrag sparKontoAntrag, BindingResult result,
                                         @ModelAttribute("kunde") Kunde kunde, Model model, RedirectAttributes redirectAttrs) {

    log.debug("SparKontoAntrag id=" + sparKontoAntrag.getId() + ", KundeNr:" + sparKontoAntrag.getKundennummer() + " soll gespeichert werden" );
    if (result.hasErrors()) {
      model.addAttribute("kunde", kundeService.findByKundennummer(sparKontoAntrag.getKundennummer().toString()));
      return "mitarbeiter/crm/sparen/sparKontoAntrag2Konto";
    }

    final Kunde mykunde = kundeService.findByKundennummer(sparKontoAntrag.getKundennummer().toString());
    final KontoStatusEnum bestMoeglicherStatus = kundeService.getBestmoeglicherKontoStatusByKundennummer(kunde.getKundennummer());


    if (sparKontoAntrag.getAntragStatus().equals(AntragStatusEnum.ABGELEHNT) || sparKontoAntrag.getAntragStatus().equals(AntragStatusEnum.ABGELEHNT_WEIL_NEU_BERECHNET)) {
      redirectAttrs.addFlashAttribute("antragAbgelehnt", true);
    }

    if (sparKontoAntrag.getAntragStatus().equals(AntragStatusEnum.GENEHMIGT))  {
      log.debug("SparkontoAntrag wurde genehmigt, Sparkonto wird erstellt");
      SparKonto sparKonto = new SparKonto(LocalDateTime.now(), kundeService.generateNewKontonummerByKundennummer(kunde.getKundennummer()), mykunde, BigDecimal.ZERO,
                                          KontoStatusEnum.IN_EROEFFNUNG, sparKontoAntrag, new ArrayList<KontoBuchung>(), "", KontoProduktEnum.SPAREN);
      log.debug("Sparkonto wurde erstellt, Sparkonto wird gespeichert.)");
      sparService.save(sparKonto);
      log.debug("Sparkonto wurde erfolgreich gespeichert. (id=" + sparKonto.getId() + ")");

      if (bestMoeglicherStatus.equals(KontoStatusEnum.IN_EROEFFNUNG)) {
        log.debug("Sparkonto mit der ID=" + sparKonto.getId() + " Kann nicht eröffnet werden. BestMöglicher Status voerst erreicht");
        redirectAttrs.addFlashAttribute("noChanges", true);
      } else {
        log.debug("Gespeichertes Sparkonto mit der ID=" + sparKonto.getId() + " wird auf Mögliche KontoEröffnung geprüft:");
        String processErgebnis = kontoService.processKontoStatusById(sparKonto.getId(), bestMoeglicherStatus, bestMoeglicherStatus);

        switch(processErgebnis) {
          // TODO: habs NoChanges nun oben eingebaut. ich schätz das wird hier rauskommen
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
    sparKontoAntragService.save(sparKontoAntrag);
    log.debug("SparKontoAntrag wurde erfolgreich gespeichert");


    return "redirect:/mitarbeiter/kunde/sparen/antrag";
  }

}














