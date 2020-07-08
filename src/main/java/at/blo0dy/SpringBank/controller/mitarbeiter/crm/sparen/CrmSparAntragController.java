package at.blo0dy.SpringBank.controller.mitarbeiter.crm.sparen;

import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import at.blo0dy.SpringBank.model.enums.AntragStatusEnum;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.sparen.SparKontoAntragService;
import at.blo0dy.SpringBank.service.konto.sparen.SparService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("mitarbeiter/kunde/sparen")
public class CrmSparAntragController {

  SparKontoAntragService sparKontoAntragService;
  SparService sparService;
  KundeService kundeService;

  @Autowired
  public CrmSparAntragController(SparKontoAntragService sparKontoAntragService, KundeService kundeService,  SparService sparService) {
    this.sparKontoAntragService = sparKontoAntragService;
    this.kundeService = kundeService;
    this.sparService = sparService;
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
  public String showSparAntragForKontoForm(@RequestParam("sparKontoAntragId") Long sparKontoAntragId, Model model) {

    SparKontoAntrag sparKontoAntrag = sparKontoAntragService.findById(sparKontoAntragId);
    Kunde kunde = kundeService.findByKundennummer(sparKontoAntrag.getKundennummer().toString());

    model.addAttribute("sparKontoAntrag", sparKontoAntrag);
    model.addAttribute("kunde", kunde);

    return "mitarbeiter/crm/sparen/sparKontoAntrag2Konto";
  }


  @PostMapping("/antrag/saveSparAntrag2KontoForm")
  public String saveSparAntrag2KontoForm(@Valid @ModelAttribute("sparKontoAntrag") SparKontoAntrag sparKontoAntrag, BindingResult result,
                                         @ModelAttribute("kunde") Kunde kunde, Model model) {

    if (result.hasErrors()) {
      model.addAttribute("kunde", kundeService.findByKundennummer(sparKontoAntrag.getKundennummer().toString()));
      return "mitarbeiter/crm/sparen/sparKontoAntrag2Konto";
    }

    final Kunde mykunde = kundeService.findByKundennummer(sparKontoAntrag.getKundennummer().toString());

    final KontoStatusEnum kontoStatusAufgrundKundenStatus = kundeService.getBestmoeglicherKontoStatusByKundennummer(kunde.getKundennummer());

    if (sparKontoAntrag.getAntragStatus().equals(AntragStatusEnum.GENEHMIGT))  {
      log.debug("Sparkonto wurde genehmigt, Sparkonto wird erstellt");
      SparKonto sparKonto = new SparKonto(LocalDateTime.now(), kundeService.generateNewKontonummerByKundennummer(kunde.getKundennummer()), mykunde, BigDecimal.ZERO, kontoStatusAufgrundKundenStatus, "12345678001",  sparKontoAntrag);
      log.debug(" --> SparkontoDaten: " + sparKonto.toString()) ;
      sparService.save(sparKonto);
    }

    log.debug("SparKontoAntrag wird gespeichert");
    sparKontoAntragService.save(sparKontoAntrag);

    return "redirect:/mitarbeiter/kunde/sparen/antrag";
  }

}














