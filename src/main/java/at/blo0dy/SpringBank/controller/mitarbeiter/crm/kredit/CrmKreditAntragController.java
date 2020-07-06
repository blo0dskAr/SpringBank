package at.blo0dy.SpringBank.controller.mitarbeiter.crm.kredit;

import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import at.blo0dy.SpringBank.model.enums.AntragsStatusEnum;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.kredit.KreditKontoAntragService;
import at.blo0dy.SpringBank.service.konto.kredit.KreditService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("mitarbeiter/kunde/kredit")
public class CrmKreditAntragController {

  KreditKontoAntragService kreditKontoAntragService;
  KreditService kreditService;
  KundeService kundeService;

  @Autowired
  public CrmKreditAntragController(KreditKontoAntragService kreditKontoAntragService, KundeService kundeService, KreditService kreditService) {
    this.kreditKontoAntragService = kreditKontoAntragService;
    this.kundeService = kundeService;
    this.kreditService = kreditService;
  }

  @GetMapping("/antrag")
  public String showKreditAntragPage(Model model) {

    model.addAttribute("gesamtAnzahl",kreditKontoAntragService.count());
    model.addAttribute("anzahlGenehmigt",kreditKontoAntragService.countByStatus("GENEHMIGT"));
    model.addAttribute("anzahlAbgelehnt",kreditKontoAntragService.countByStatus("ABGELEHNT"));
    model.addAttribute("anzahlEingereicht",kreditKontoAntragService.countByStatus("EINGEREICHT"));
    model.addAttribute("offeneAntragListe", kreditKontoAntragService.findByStatus("EINGEREICHT"));


    return "mitarbeiter/crm/kredit/kreditAntrag";
  }

  @GetMapping("/antrag/showKreditAntragForKontoForm")
  public String showKreditAntragForKontoForm(@RequestParam("kreditKontoAntragId") Long kreditKontoAntragId, Model model) {

    KreditKontoAntrag kreditKontoAntrag = kreditKontoAntragService.findById(kreditKontoAntragId);
    Kunde kunde = kundeService.findByKundennummer(kreditKontoAntrag.getKundennummer().toString());

    model.addAttribute("kreditKontoAntrag", kreditKontoAntrag);
    model.addAttribute("kunde", kunde);

    return "mitarbeiter/crm/kredit/kreditKontoAntrag2Konto";
  }


  @PostMapping("/antrag/saveKreditAntrag2KontoForm")
  public String saveKreditAntrag2KontoForm(@Valid @ModelAttribute("kreditKontoAntrag") KreditKontoAntrag kreditKontoAntrag,
                                           @ModelAttribute("kunde") Kunde kunde, Model model) {

    Kunde mykunde = kundeService.findByKundennummer(kreditKontoAntrag.getKundennummer().toString());

    KontoStatusEnum kontoStatusAufgrundKundenStatus = kundeService.getBestmoeglicherKontoStatusByKundennummer(kunde.getKundennummer());

    // TODO: Da muss man noch die neue Kontonummer generieren lassen, beim sparen auch.
    if (kreditKontoAntrag.getAntragsStatus().equals(AntragsStatusEnum.GENEHMIGT))  {
      KreditKonto kreditKonto = new KreditKonto(LocalDate.now(), kundeService.generateNewKontonummerByKundennummer(kunde.getKundennummer()), mykunde, BigDecimal.ZERO,
                                                kontoStatusAufgrundKundenStatus, kreditKontoAntrag.getKreditBetrag(),  kreditKontoAntrag.getRate(), kreditKontoAntrag.getLaufzeit(), kreditKontoAntrag);
      System.out.println(kreditKonto);
      kreditService.save(kreditKonto);
    }

    kreditKontoAntragService.save(kreditKontoAntrag);

    return "redirect:/mitarbeiter/kunde/kredit/antrag";
  }

}














