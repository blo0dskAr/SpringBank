package at.blo0dy.SpringBank.controller.mitarbeiter.crm;

import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import at.blo0dy.SpringBank.model.enums.AntragsStatusEnum;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.sparen.SparKontoAntragService;
import at.blo0dy.SpringBank.service.konto.sparen.SparService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;

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
  public String saveSparAntrag2KontoForm(@Valid @ModelAttribute("sparKontoAntrag") SparKontoAntrag sparKontoAntrag,
                                         @Valid @ModelAttribute("kunde") Kunde kunde, Model model) {

    Kunde mykunde = kundeService.findByKundennummer(sparKontoAntrag.getKundennummer().toString());

    if (sparKontoAntrag.getAntragsStatus().equals(AntragsStatusEnum.GENEHMIGT))  {
      SparKonto sparKonto = new SparKonto(LocalDate.now(), 12345678005L, mykunde,  BigDecimal.ZERO, KontoStatusEnum.ANTRAG_OFFEN, "12345678001",  sparKontoAntrag);
      System.out.println(sparKonto);
      sparService.save(sparKonto);
    }

    sparKontoAntragService.save(sparKontoAntrag);
    //kundeService.save(kunde);

    return "redirect:/mitarbeiter/kunde/sparen/antrag";
  }


  @GetMapping("/konto")
  public String showSparKontoPage() {

    return "mitarbeiter/crm/sparKonto";
  }


}
