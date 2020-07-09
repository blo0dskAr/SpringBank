package at.blo0dy.SpringBank.controller.mitarbeiter.crm.giro;

import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import at.blo0dy.SpringBank.model.enums.AntragStatusEnum;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.giro.GiroKontoAntragService;
import at.blo0dy.SpringBank.service.konto.giro.GiroService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("mitarbeiter/kunde/giro")
public class CrmGiroAntragController {

  GiroKontoAntragService giroKontoAntragService;
  GiroService giroService;
  KundeService kundeService;

  @Autowired
  public CrmGiroAntragController(GiroKontoAntragService giroKontoAntragService, GiroService giroService, KundeService kundeService) {
    this.giroKontoAntragService = giroKontoAntragService;
    this.giroService = giroService;
    this.kundeService = kundeService;
  }

  @GetMapping("/antrag")
  public String showSparAntragPage(Model model) {

    model.addAttribute("gesamtAnzahl",giroKontoAntragService.count());
    model.addAttribute("anzahlGenehmigt",giroKontoAntragService.countByStatus("GENEHMIGT"));
    model.addAttribute("anzahlAbgelehnt",giroKontoAntragService.countByStatus("ABGELEHNT"));
    model.addAttribute("anzahlEingereicht",giroKontoAntragService.countByStatus("EINGEREICHT"));
    model.addAttribute("offeneAntragListe", giroKontoAntragService.findByStatus("EINGEREICHT"));


    return "mitarbeiter/crm/giro/giroAntrag";
  }

  @GetMapping("/antrag/showGiroAntragForKontoForm")
  public String showSparAntragForKontoForm(@RequestParam("giroKontoAntragId") Long giroKontoAntragId, Model model) {

    GiroKontoAntrag giroKontoAntrag = giroKontoAntragService.findById(giroKontoAntragId);
    Kunde kunde = kundeService.findByKundennummer(giroKontoAntrag.getKundennummer().toString());

    model.addAttribute("giroKontoAntrag", giroKontoAntrag);
    model.addAttribute("kunde", kunde);

    return "mitarbeiter/crm/giro/giroKontoAntrag2Konto";
  }


  @PostMapping("/antrag/saveGiroAntrag2KontoForm")
  public String saveGiroAntrag2KontoForm(@Valid @ModelAttribute("giroKontoAntrag") GiroKontoAntrag giroKontoAntrag,
                                         @ModelAttribute("kunde") Kunde kunde, Model model) {

    final Kunde mykunde = kundeService.findByKundennummer(giroKontoAntrag.getKundennummer().toString());
    final KontoStatusEnum kontoStatusAufgrundKundenStatus = kundeService.getBestmoeglicherKontoStatusByKundennummer(kunde.getKundennummer());

    if (giroKontoAntrag.getAntragStatus().equals(AntragStatusEnum.GENEHMIGT))  {
      BigDecimal ueberziehungsrahmen;
      if (giroKontoAntrag.isUeberziehungsrahmenGewuenscht()) {
        ueberziehungsrahmen = BigDecimal.valueOf(500);
      } else {
        ueberziehungsrahmen = BigDecimal.ZERO;
      }
      log.debug("CrmGirorAntragController: Girokonto wurde genehmigt, Girokonto wird erstellt");
      GiroKonto giroKonto = new GiroKonto(LocalDateTime.now(), kundeService.generateNewKontonummerByKundennummer(kunde.getKundennummer()), mykunde, BigDecimal.ZERO, kontoStatusAufgrundKundenStatus, giroKontoAntrag, ueberziehungsrahmen );
      log.debug("CrmGiroAntragController: --> GirokontoDaten: " + giroKonto.toString()) ;
      giroService.save(giroKonto);
    }

    log.debug("CrmGiroAntragController: " + "GiroKontoAntrag wird gespeichert");
    giroKontoAntragService.save(giroKontoAntrag);

    return "redirect:/mitarbeiter/kunde/giro/antrag";
  }

}














