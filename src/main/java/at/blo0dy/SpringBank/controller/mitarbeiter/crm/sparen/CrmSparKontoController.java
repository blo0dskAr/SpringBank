package at.blo0dy.SpringBank.controller.mitarbeiter.crm.sparen;

import at.blo0dy.SpringBank.model.enums.KontoProduktEnum;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.KontoService;
import at.blo0dy.SpringBank.service.konto.sparen.SparService;
import at.blo0dy.SpringBank.service.konto.dauerauftrag.DauerAuftragService;
import at.blo0dy.SpringBank.service.konto.zahlungsAuftrag.ZahlungsAuftragService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("mitarbeiter/kunde/sparen")
public class CrmSparKontoController {

  SparService sparService;
  KundeService kundeService;
  ZahlungsAuftragService zahlungsAuftragService;
  DauerAuftragService dauerAuftragService;
  KontoService kontoService;

  @Autowired
  public CrmSparKontoController(KundeService kundeService, SparService sparService, ZahlungsAuftragService zahlungsAuftragService, DauerAuftragService dauerAuftragService, KontoService kontoService) {
    this.kundeService = kundeService;
    this.sparService = sparService;
    this.zahlungsAuftragService = zahlungsAuftragService;
    this.dauerAuftragService = dauerAuftragService;
    this.kontoService = kontoService;
  }


  @GetMapping("/kontoBearbeitung")
  public String showSparKontoBearbeitungsPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {
    log.debug("Showing SparKontoBearbeitungsPage for Mitarbeiter: " + authentication.getName());


    Konto konto = new SparKonto();
    konto.setProdukt(KontoProduktEnum.SPAREN);
    konto.setKontoStatus(KontoStatusEnum.IN_EROEFFNUNG);

    model.addAttribute("konto",konto);

    List<Konto> ergebnis = kontoService.findAll(konto);

    model.addAttribute("ergebnis", ergebnis);
    return "mitarbeiter/crm/kontosuche";
  }

  @PostMapping("/kontoBearbeitung")
  public String showSparKontoBearbeitungsPageErg(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                                 @ModelAttribute Konto konto) {
    log.debug("Showing SparKontoBearbeitungsPage for Mitarbeiter: " + authentication.getName());

    List<Konto> ergebnis = kontoService.findAll(konto);

    model.addAttribute("ergebnis", ergebnis);
    return "mitarbeiter/crm/kontosuche";
  }


  @GetMapping("/konto/showSparKontoDetailPage")
  public String showSparKontoDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                        @RequestParam("sparKontoId") Long sparKontoId) {

    log.debug("Showing SparKontoDetailPage for Mitarbeiter: " + authentication.getName() + " und KontoId: " + sparKontoId);

    Konto sparkonto = sparService.findById(sparKontoId);
    Kunde kunde = sparkonto.getKunde();

    model.addAttribute("konto", sparkonto);
    model.addAttribute("kunde", kunde);
    model.addAttribute("countOffeneZA",zahlungsAuftragService.countOffeneZahlungsAuftraegeByKontoId(sparKontoId));
    model.addAttribute("countAktiveDA",dauerAuftragService.countAktiveDauerAuftraegeByKontonummer(sparkonto.getKontonummer()));

    return "mitarbeiter/crm/konto-detail";

  }

  @PostMapping("/konto/saveSparKontoDetailPage")
  public String saveSparKontoDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                        @ModelAttribute Konto konto, RedirectAttributes redirectAttrs ) {

    log.debug("Speichern der SparKontoDetailPage for Mitarbeiter: " + authentication.getName() + " und KontoId: " + konto.getId() + " angefordert");
    Konto tmpKonto = kontoService.findById(konto.getId());
    KontoStatusEnum neuerStatus = konto.getKontoStatus();

    Kunde kunde = tmpKonto.getKunde();

    KontoStatusEnum bestMoeglicherStatus = kundeService.getBestmoeglicherKontoStatusByKundennummer(kunde.getKundennummer());
    String processErgebnis = kontoService.processKontoStatusById(tmpKonto.getId(),neuerStatus, bestMoeglicherStatus);

    switch(processErgebnis) {
      case "NO_CHANGES":
        redirectAttrs.addFlashAttribute("noChanges", true);
        break;
      case "TRANSITION_NOT_POSSIBLE":
        redirectAttrs.addFlashAttribute("transitionNotPossible", true);
        break;
      case "SALDO_NOT_ZERO":
        redirectAttrs.addFlashAttribute("saldoNotZero", true);
        break;
      case "KONTO_NOW_CLOSED":
        redirectAttrs.addFlashAttribute("kontoClosed", true);
        break;
      case "KONTO_NOW_OPEN":
        redirectAttrs.addFlashAttribute("kontoOpened", true);
        break;
    }

    return "redirect:/mitarbeiter/kunde/sparen/konto/showSparKontoDetailPage?sparKontoId=" + tmpKonto.getId();

  }




  @GetMapping({"/konto/showEinzahlungsForm", "/konto/showAuszahlungsForm"})
  public String showEinzahlungsForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                    @RequestParam("sparKontoId") Long sparKontoId, HttpServletRequest request) {

    log.debug("Showing SparKontoEinzahlungsForm for Mitarbeiter: " + authentication.getName() + " und KontoId: " + sparKontoId);

    Konto sparkonto = sparService.findById(sparKontoId);
    ZahlungsAuftrag zahlungsAuftrag = new ZahlungsAuftrag();
    zahlungsAuftrag.setId(0L);
    if (request.getRequestURI().equals("/mitarbeiter/kunde/sparen/konto/showAuszahlungsForm")) {
      zahlungsAuftrag.setAuftragsArt(ZahlungAuftragArtEnum.AUSZAHLUNG);
    } else if (request.getRequestURI().equals("/mitarbeiter/kunde/sparen/konto/showEinzahlungsForm")) {
      zahlungsAuftrag.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
    } else {
      // setz nix, damit mans manuell auswählen kann. (in allgemeiner maske)
    }
    zahlungsAuftrag.setKontonummer(sparkonto.getKontonummer().toString());
    List<String> kontonummerAuswahlList = sparService.findKontoNummerOffenerSparKontenByKundennummer(sparkonto.getKunde().getKundennummer());

    model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);
    model.addAttribute("konto", sparkonto);
    model.addAttribute("requestedKontonummer", sparkonto.getKontonummer().toString());
    model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);

    return "mitarbeiter/crm/zahlungsAuftrag-form";
  }


  @PostMapping("/konto/saveEinzahlungsForm")
  public String processEinzahlungsForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                       @Valid @ModelAttribute(name = "zahlungsAuftrag") ZahlungsAuftrag zahlungsAuftrag, BindingResult result,
                                       RedirectAttributes redirectAttrs) {

    String tmpKontonummer = zahlungsAuftrag.getKontonummer();
    String tmpMitarbeiter = authentication.getName();
    Konto sparkonto = sparService.findByKontonummer(tmpKontonummer);
    String tmpKundennummer = sparkonto.getKunde().getKundennummer();

    log.debug("SparKontoEinzahlungsForm soll gespeichert werden für Mitarbeiter: " + tmpMitarbeiter + " und KontoNr: " + tmpKontonummer);

    // Form Validation Errors
    if(result.hasErrors()) {
      log.warn("Fehler beim speichern eines EinzahlungsAuftrag für Mitarbeiter: " + tmpMitarbeiter + " und KontoNr: " + tmpKontonummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      List<String> kontonummerAuswahlList = sparService.findKontoNummerOffenerSparKontenByKundennummer(sparkonto.getKunde().getKundennummer());
      model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);
      model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);
      model.addAttribute("konto", sparkonto);
      model.addAttribute("requestedKontonummer", sparkonto.getKontonummer().toString());

      return "mitarbeiter/crm/zahlungsAuftrag-form";
    }

    // SaldoPrüfung
    if (zahlungsAuftrag.getAuftragsArt().equals(ZahlungAuftragArtEnum.AUSZAHLUNG)) {
      if (!zahlungsAuftragService.checkAuszahlungWithVerfuegbarerBetrag(sparkonto, zahlungsAuftrag.getBetrag() )) {
        result.rejectValue("betrag","error.zahlungsAuftrag", "Verfügbarer Saldo nicht ausreichend");
      }
    }

    // TODO: Auch hier muss ich 2 mal auf errors checken, das sollte sich irgendfwie vermeiden lassen
    if(result.hasErrors()) {
      log.warn("Fehler beim speichern eines EinzahlungsAuftrag für Mitarbeiter: " + tmpMitarbeiter + " und KontoNr: " + tmpKontonummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      List<String> kontonummerAuswahlList = sparService.findKontoNummerOffenerSparKontenByKundennummer(sparkonto.getKunde().getKundennummer());
      model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);
      model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);
      model.addAttribute("konto", sparkonto);
      model.addAttribute("requestedKontonummer", sparkonto.getKontonummer().toString());

      return "mitarbeiter/crm/zahlungsAuftrag-form";
    }


    zahlungsAuftrag.setDatAnlage(LocalDateTime.now());
    zahlungsAuftrag.setAuftragsStatus(ZahlungAuftragStatusEnum.ANGELEGT);
    zahlungsAuftrag.setKonto(sparkonto);
    if (zahlungsAuftrag.getAuftragsArt().equals(ZahlungAuftragArtEnum.EINZAHLUNG)) {
      zahlungsAuftrag.setEmpfaengerKonto(sparkonto.getKontonummer().toString());
      zahlungsAuftrag.setSenderKonto(kundeService.getConnectedGiroByKundennummer(tmpKundennummer));
    } else {
      zahlungsAuftrag.setSenderKonto(kundeService.getConnectedGiroByKundennummer(tmpKundennummer));
      zahlungsAuftrag.setEmpfaengerKonto(sparkonto.getKontonummer().toString());
    }


    log.debug("SparKontoEinzahlungsForm wird gespeichert für Mitarbeiter: " + tmpMitarbeiter + " und KontoNr: " + tmpKontonummer);
    zahlungsAuftragService.save(zahlungsAuftrag);
    log.debug("SparKontoEinzahlungsForm wurde erfolgreich gespeichert für Mitarbeiter: " + tmpMitarbeiter + " und KontoNr: " + tmpKontonummer);

    redirectAttrs.addFlashAttribute("zahlungsAuftragGespeichert", true);
    return "redirect:/mitarbeiter/kunde/sparen/konto/showSparKontoDetailPage?sparKontoId=" + sparkonto.getId();
  }




}
