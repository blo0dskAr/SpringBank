package at.blo0dy.SpringBank.controller.mitarbeiter.crm.kredit;

import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import at.blo0dy.SpringBank.model.enums.AntragStatusEnum;
import at.blo0dy.SpringBank.model.enums.KontoProduktEnum;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.kontoBuchung.KontoBuchung;
import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerVorlage;
import at.blo0dy.SpringBank.service.konto.KontoService;
import at.blo0dy.SpringBank.service.konto.kredit.KreditKontoAntragService;
import at.blo0dy.SpringBank.service.konto.kredit.KreditService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@Controller
@RequestMapping("mitarbeiter/kunde/kredit")
public class CrmKreditAntragController {

  KreditKontoAntragService kreditKontoAntragService;
  KreditService kreditService;
  KundeService kundeService;
  KontoService kontoService;

  @Autowired
  public CrmKreditAntragController(KreditKontoAntragService kreditKontoAntragService, KreditService kreditService, KundeService kundeService, KontoService kontoService) {
    this.kreditKontoAntragService = kreditKontoAntragService;
    this.kreditService = kreditService;
    this.kundeService = kundeService;
    this.kontoService = kontoService;
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
    log.debug("showKreditAntragForKontoForm für id=" + kreditKontoAntragId + ", KundeNr= " + kunde.getKundennummer() + " wird angezeigt");

    model.addAttribute("kreditKontoAntrag", kreditKontoAntrag);
    model.addAttribute("kunde", kunde);

    return "mitarbeiter/crm/kredit/kreditKontoAntrag2Konto";
  }


  @PostMapping(value = "/antrag/saveKreditAntrag2KontoForm", params = {"saveKreditKonto"})
  public String saveKreditAntrag2KontoForm(@Valid @ModelAttribute("kreditKontoAntrag") KreditKontoAntrag kreditKontoAntrag, BindingResult bindingResult,
                                           @ModelAttribute("kunde") Kunde kunde, Model model, RedirectAttributes redirectAttrs) {

    Long tmpKredAntrId = kreditKontoAntrag.getId();
    String tmpKundenNummer = kreditKontoAntrag.getKundennummer().toString();

      log.debug("KreditAntrag id=" + tmpKredAntrId + ", KundeNr:" + tmpKundenNummer + " soll gespeichert werden" );
      try {
          KreditKontoAntrag vergleichsAntrag = kreditKontoAntragService.findById(tmpKredAntrId);
          log.debug("Es wird geprüft ob sich die KreditAntragdaten verändert haben für KreditAntragId=" + tmpKredAntrId);
          if (kreditKontoAntragService.compareBasicKreditData(kreditKontoAntrag, vergleichsAntrag)) {
          model.addAttribute("kreditMussNeuBerechnetWerden", false);
          log.debug("--> Keine NeuBerechnung Notwendig für KreditAntragId=" + tmpKredAntrId);
        } else {
          kunde = kundeService.findByKundennummer(tmpKundenNummer);
          model.addAttribute("kunde", kunde);
          model.addAttribute("kreditMussNeuBerechnetWerden", true);
          log.debug("--> NeuBerechnung Notwendig für KreditAntragId=" + tmpKredAntrId);
          return "mitarbeiter/crm/kredit/kreditKontoAntrag2Konto";
        }
      } catch (Exception e) {
        log.debug("Exception Gefangen, da wohl irgendwas null " + e.getMessage());
      }

    if (bindingResult.hasErrors()) {
      kunde = kundeService.findByKundennummer(tmpKundenNummer);
      model.addAttribute("kunde", kunde);
      log.debug("FormValidation Error erhalten für Kunde=" + tmpKundenNummer );

      return "mitarbeiter/crm/kredit/kreditKontoAntrag2Konto";
    }

    Kunde mykunde = kundeService.findByKundennummer(tmpKundenNummer);
    KontoStatusEnum bestMoeglicherStatus = kundeService.getBestmoeglicherKontoStatusByKundennummer(kunde.getKundennummer());

    // Wenn auf genehmigt gestellt wird, wird ein neues Kreditkonto gespeichert
    if (kreditKontoAntrag.getAntragStatus().equals(AntragStatusEnum.GENEHMIGT)) {
      log.debug("KreditKontoAntrag wurde genehmigt, KreditKonto wird erstellt");
      KreditKonto kreditKonto = new KreditKonto(LocalDateTime.now(), kundeService.generateNewKontonummerByKundennummer(kunde.getKundennummer()), mykunde, BigDecimal.ZERO,
                                                KontoStatusEnum.IN_EROEFFNUNG, kreditKontoAntrag, kreditKontoAntrag.getKreditBetrag(), kreditKontoAntrag.getRate(), kreditKontoAntrag.getLaufzeit(),
                                                new ArrayList<KontoBuchung>(), KontoProduktEnum.KREDIT);
      log.debug("KreditKonto wurde erstellt, KreditKonto wird gespeichert. Kunde: " + tmpKundenNummer);
      kreditService.save(kreditKonto);
      log.debug("KreditKonto wurde erfolgreich gespeichert Kunde: " + tmpKundenNummer);

      log.debug("Änderungen an KreditAntragId=" + tmpKredAntrId + " werden gespeichert");
      kreditKontoAntragService.save(kreditKontoAntrag);
      log.debug("Änderungen an KreditAntragId=" + tmpKredAntrId + " wurden erfolgreich gespeichert");

      if (bestMoeglicherStatus.equals(KontoStatusEnum.IN_EROEFFNUNG)) {
        log.debug("KreditKonto mit der ID=" + kreditKonto.getId() + " Kann nicht eröffnet werden. BestMöglicher Status voerst erreicht");
      } else {
        log.debug("Gespeichertes KreditKonto mit der ID=" + kreditKonto.getId() + " wird auf Mögliche KontoEröffnung geprüft:");
        String processErgebnis = kontoService.processKontoStatusById(kreditKonto.getId(), bestMoeglicherStatus, bestMoeglicherStatus);

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

    return "redirect:/mitarbeiter/kunde/kredit/antrag";
  }


  @PostMapping(value = "/antrag/saveKreditAntrag2KontoForm", params = {"recalculate"})
  public String recalculateKreditAntrag2KontoForm(@Valid @ModelAttribute("kreditKontoAntrag") KreditKontoAntrag kreditKontoAntrag, BindingResult bindingResult,
                                                  @ModelAttribute("kunde") Kunde kunde, Model model) {

    Long tmpKredAntrId = kreditKontoAntrag.getId();
    String tmpKundenNummer = kreditKontoAntrag.getKundennummer().toString();

    if (bindingResult.hasErrors()) {
      kunde = kundeService.findByKundennummer(tmpKundenNummer);
      model.addAttribute("kunde", kunde);
      log.debug("FormValidation Error bei Neuberechnung erhalten für Kunde=" + tmpKundenNummer );
      return "mitarbeiter/crm/kredit/kreditKontoAntrag2Konto";
    }

    log.debug("Neuberechnung für KreditAntragId=" + tmpKredAntrId + " wird durchgeführt");
    KreditRechnerErgebnis ke = kreditService.getKreditRechnerErgebnis(new KreditRechnerVorlage(kreditKontoAntrag.getLaufzeit(), kreditKontoAntrag.getZinssatz(), kreditKontoAntrag.getKreditBetrag()));

    kunde = kundeService.findByKundennummer(kreditKontoAntrag.getKundennummer().toString());
    model.addAttribute("kunde", kunde);

    KreditKontoAntrag neuBerechneterAntrag = new KreditKontoAntrag(LocalDateTime.now(), AntragStatusEnum.EINGEREICHT,ke.getKreditBetrag(),kreditKontoAntrag.getZinssatz(),
                                                                    ke.getMonatlicheRate(),kreditKontoAntrag.getLaufzeit(),ke.getGesamtBelastung(),kreditKontoAntrag.getKundennummer(), KontoProduktEnum.KREDIT);

    kreditKontoAntrag.setGesamtBelastung(ke.getGesamtBelastung());;
    kreditKontoAntrag.setKreditBetrag(ke.getKreditBetrag());
    kreditKontoAntrag.setRate(ke.getMonatlicheRate());
    kreditKontoAntragService.save(kreditKontoAntrag);
    log.debug("Neuberechnung für KreditAntragId=" + tmpKredAntrId + " wurde durchgeführt, Änderungen gespeichert");

    model.addAttribute("neuBerechneterAntrag", neuBerechneterAntrag);
    model.addAttribute("ke",ke);
    model.addAttribute("kreditKontoAntrag", kreditKontoAntrag);

    return "mitarbeiter/crm/kredit/kreditKontoAntrag2Konto" ;
  }

}














