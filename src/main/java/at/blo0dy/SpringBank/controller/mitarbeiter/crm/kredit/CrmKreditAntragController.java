package at.blo0dy.SpringBank.controller.mitarbeiter.crm.kredit;

import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import at.blo0dy.SpringBank.model.enums.AntragsStatusEnum;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerVorlage;
import at.blo0dy.SpringBank.service.konto.kredit.KreditKontoAntragService;
import at.blo0dy.SpringBank.service.konto.kredit.KreditService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
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

    model.addAttribute("gesamtAnzahl", kreditKontoAntragService.count());
    model.addAttribute("anzahlGenehmigt", kreditKontoAntragService.countByStatus("GENEHMIGT"));
    model.addAttribute("anzahlAbgelehnt", kreditKontoAntragService.countByStatus("ABGELEHNT"));
    model.addAttribute("anzahlEingereicht", kreditKontoAntragService.countByStatus("EINGEREICHT"));
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


  @PostMapping(value = "/antrag/saveKreditAntrag2KontoForm", params = {"saveKreditKonto"})
  public String saveKreditAntrag2KontoForm(@Valid @ModelAttribute("kreditKontoAntrag") KreditKontoAntrag kreditKontoAntrag, BindingResult bindingResult,
                                           @ModelAttribute("kunde") Kunde kunde, Model model) {

    try {
      // TODO: da mal bissl aufräumen , ich brauch nur noch den selbstgeschriebenen dataComparer...
      log.debug("CrmKreditAntragController: saveKreditAntrag2KontoForm ----> KreditKontoAntrag: " + kreditKontoAntrag);
      KreditKontoAntrag vergleichsAntrag = kreditKontoAntragService.findById(kreditKontoAntrag.getId());
      log.debug("CrmKreditAntragController: saveKreditAntrag2KontoForm ----> Vergleichs-Antrag: " + vergleichsAntrag);
      log.debug("VERGLEICH DER ANTRAG_DATAS -------> " + kreditKontoAntragService.compareBasicKreditData(kreditKontoAntrag, vergleichsAntrag));

      if (kreditKontoAntragService.compareBasicKreditData(kreditKontoAntrag, vergleichsAntrag)) {
        model.addAttribute("kreditMussNeuBerechnetWerden", false);
      } else {
        kunde = kundeService.findByKundennummer(kreditKontoAntrag.getKundennummer().toString());
        model.addAttribute("kunde", kunde);
        model.addAttribute("kreditMussNeuBerechnetWerden", true);
        return "mitarbeiter/crm/kredit/kreditKontoAntrag2Konto";
      }
    } catch (Exception e) {
      log.debug("Exception Gefangen, da wohl irgendwas null " + e.getMessage());
    }

    if (bindingResult.hasErrors()) {
      // KreditRechnerErgebnis ke = kreditService.getKreditRechnerErgebnis(new KreditRechnerVorlage(kreditKontoAntrag.getLaufzeit(),kreditKontoAntrag.getZinssatz(),kreditKontoAntrag.getKreditBetrag()));
      kunde = kundeService.findByKundennummer(kreditKontoAntrag.getKundennummer().toString());
      model.addAttribute("kunde", kunde);
      log.debug("showKreditAntragForKontoForm:------> Kunde:  " + kunde);

      return "mitarbeiter/crm/kredit/kreditKontoAntrag2Konto";
    }

    Kunde mykunde = kundeService.findByKundennummer(kreditKontoAntrag.getKundennummer().toString());

    KontoStatusEnum kontoStatusAufgrundKundenStatus = kundeService.getBestmoeglicherKontoStatusByKundennummer(kunde.getKundennummer());

    if (kreditKontoAntrag.getAntragsStatus().equals(AntragsStatusEnum.GENEHMIGT)) {
      KreditKonto kreditKonto = new KreditKonto(LocalDate.now(), kundeService.generateNewKontonummerByKundennummer(kunde.getKundennummer()), mykunde, BigDecimal.ZERO,
              kontoStatusAufgrundKundenStatus, kreditKontoAntrag.getKreditBetrag(), kreditKontoAntrag.getRate(), kreditKontoAntrag.getLaufzeit(), kreditKontoAntrag);
      kreditService.save(kreditKonto);
    }

    kreditKontoAntragService.save(kreditKontoAntrag);
    return "redirect:/mitarbeiter/kunde/kredit/antrag";
  }


  @PostMapping(value = "/antrag/saveKreditAntrag2KontoForm", params = {"recalculate"})
  public String recalculateKreditAntrag2KontoForm(@Valid @ModelAttribute("kreditKontoAntrag") KreditKontoAntrag kreditKontoAntrag, BindingResult bindingResult,
                                                  @ModelAttribute("kunde") Kunde kunde, Model model) {

    if (bindingResult.hasErrors()) {
      // KreditRechnerErgebnis ke = kreditService.getKreditRechnerErgebnis(new KreditRechnerVorlage(kreditKontoAntrag.getLaufzeit(),kreditKontoAntrag.getZinssatz(),kreditKontoAntrag.getKreditBetrag()));
      kunde = kundeService.findByKundennummer(kreditKontoAntrag.getKundennummer().toString());
      model.addAttribute("kunde", kunde);
      log.debug("showKreditAntragForKontoForm:------> Kunde:  " + kunde);

      return "mitarbeiter/crm/kredit/kreditKontoAntrag2Konto";
    }

    log.debug("KREDITKONTOANTRAG BEIM RECALCULATEN: -----> " + kreditKontoAntrag.toString());
    KreditRechnerErgebnis ke = kreditService.getKreditRechnerErgebnis(new KreditRechnerVorlage(kreditKontoAntrag.getLaufzeit(), kreditKontoAntrag.getZinssatz(), kreditKontoAntrag.getKreditBetrag()));

    kunde = kundeService.findByKundennummer(kreditKontoAntrag.getKundennummer().toString());
    model.addAttribute("kunde", kunde);

    KreditKontoAntrag neuBerechneterAntrag = new KreditKontoAntrag(LocalDateTime.now(),AntragsStatusEnum.EINGEREICHT,ke.getKreditBetrag(),kreditKontoAntrag.getZinssatz(),
                                                                    ke.getMonatlicheRate(),kreditKontoAntrag.getLaufzeit(),ke.getGesamtBelastung(),kreditKontoAntrag.getKundennummer());
    log.debug("NEU BERECHNETER KREDITKONTO ANTRAG: ------> " + neuBerechneterAntrag.toString());

    model.addAttribute("kreditKontoAntrag", neuBerechneterAntrag);
    kreditKontoAntragService.setKreditAntragAbgelehntWeilNeuBerechnetById(kreditKontoAntrag.getId());

    return "mitarbeiter/crm/kredit/kreditKontoAntrag2Konto";
  }

}














