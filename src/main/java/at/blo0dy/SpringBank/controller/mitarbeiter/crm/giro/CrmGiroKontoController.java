package at.blo0dy.SpringBank.controller.mitarbeiter.crm.giro;

import at.blo0dy.SpringBank.model.enums.KontoProduktEnum;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.KontoService;
import at.blo0dy.SpringBank.service.konto.dauerauftrag.DauerAuftragService;
import at.blo0dy.SpringBank.service.konto.giro.GiroService;
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
@RequestMapping("mitarbeiter/kunde/giro")
public class CrmGiroKontoController {

  GiroService giroService;
  KundeService kundeService;
  ZahlungsAuftragService zahlungsAuftragService;
  KontoService kontoService;
  DauerAuftragService dauerAuftragService;

  @Autowired
  public CrmGiroKontoController(KundeService kundeService, GiroService giroService, ZahlungsAuftragService zahlungsAuftragService, KontoService kontoService, DauerAuftragService dauerAuftragService) {
    this.kundeService = kundeService;
    this.giroService = giroService;
    this.zahlungsAuftragService = zahlungsAuftragService;
    this.kontoService = kontoService;
    this.dauerAuftragService = dauerAuftragService;
  }


  @GetMapping("/kontoBearbeitung")
  public String showGiroKontoBearbeitungsPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

    log.debug("Showing GiroKontoBearbeitungsPage for Mitarbeiter: " + authentication.getName());

    Konto konto = new GiroKonto();
    konto.setProdukt(KontoProduktEnum.GIRO);
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


  @GetMapping("/konto/showGiroKontoDetailPage")
  public String showGiroKontoDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                        @RequestParam("giroKontoId") Long giroKontoId) {

    log.debug("Showing GiroKontoDetailPage for Mitarbeiter: " + authentication.getName() + " und KontoId: " + giroKontoId);

    Konto girokonto = giroService.findById(giroKontoId);
    Kunde kunde = girokonto.getKunde();

    model.addAttribute("konto", girokonto);
    model.addAttribute("kunde", kunde);
    model.addAttribute("countOffeneZA",zahlungsAuftragService.countOffeneZahlungsAuftraegeByKontoId(giroKontoId));
    model.addAttribute("countAktiveDA",dauerAuftragService.countAktiveDauerAuftraegeByKontonummer(girokonto.getKontonummer()));

    return "mitarbeiter/crm/konto-detail";
  }


  @PostMapping("/konto/saveGiroKontoDetailPage")
  public String saveGiroKontoDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                        @ModelAttribute Konto konto, RedirectAttributes redirectAttrs ) {

    log.debug("Speichern der GiroKontoDetailPage for Mitarbeiter: " + authentication.getName() + " und KontoId: " + konto.getId() + " angefordert");
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

    return "redirect:/mitarbeiter/kunde/giro/konto/showGiroKontoDetailPage?giroKontoId=" + tmpKonto.getId();

  }



  @GetMapping({"/konto/showEinzahlungsForm", "/konto/showAuszahlungsForm"})
  public String showEinzahlungsForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                    @RequestParam("giroKontoId") Long giroKontoId, HttpServletRequest request) {

    log.debug("Showing GiroKontoEinzahlungsForm for Mitarbeiter: " + authentication.getName() + " und KontoId: " + giroKontoId);

    Konto girokonto = giroService.findById(giroKontoId);
    ZahlungsAuftrag zahlungsAuftrag = new ZahlungsAuftrag();
    zahlungsAuftrag.setId(0L);
    if (request.getRequestURI().equals("/mitarbeiter/kunde/giro/konto/showAuszahlungsForm")) {
      zahlungsAuftrag.setAuftragsArt(ZahlungAuftragArtEnum.AUSZAHLUNG);
    } else if (request.getRequestURI().equals("/mitarbeiter/kunde/giro/konto/showEinzahlungsForm")) {
      zahlungsAuftrag.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
    } else {
      // setz nix, damit mans manuell auswählen kann. (in allgemeiner maske)
    }
    zahlungsAuftrag.setKontonummer(girokonto.getKontonummer().toString());
    List<String> kontonummerAuswahlList = giroService.findKontoNummerOffenerGiroKontenByKundennummer(girokonto.getKunde().getKundennummer());

    model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);
    model.addAttribute("konto", girokonto);
    model.addAttribute("requestedKontonummer", girokonto.getKontonummer().toString());
    model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);

    return "mitarbeiter/crm/zahlungsAuftrag-form";
  }


  @PostMapping("/konto/saveEinzahlungsForm")
  public String processEinzahlungsForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                       @Valid @ModelAttribute(name = "zahlungsAuftrag") ZahlungsAuftrag zahlungsAuftrag, BindingResult result,
                                       RedirectAttributes redirectAttrs) {

    String tmpMitarbeiter = authentication.getName();
    String tmpKontonummer = zahlungsAuftrag.getKontonummer();
    log.debug("GiroKontoEinzahlungsForm soll gespeichert werden für Mitarbeiter: " + tmpMitarbeiter + " und KontoNr: " + tmpKontonummer);

    GiroKonto girokonto = giroService.findByKontonummer(Long.valueOf(tmpKontonummer));
    String tmpKundennummer = girokonto.getKunde().getKundennummer();

    if(result.hasErrors()) {
      log.warn("Fehler beim speichern eines EinzahlungsAuftrag für Mitarbeiter: " + tmpMitarbeiter + " und KontoNr: " + tmpKontonummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      List<String> kontonummerAuswahlList = giroService.findKontoNummerOffenerGiroKontenByKundennummer(girokonto.getKunde().getKundennummer());
      model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);
      model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);
      model.addAttribute("konto", girokonto);
      model.addAttribute("requestedKontonummer", girokonto.getKontonummer().toString());

      return "mitarbeiter/crm/zahlungsAuftrag-form";
    }

    // SaldoPrüfung
    if (zahlungsAuftrag.getAuftragsArt().equals(ZahlungAuftragArtEnum.AUSZAHLUNG)) {
      if (!zahlungsAuftragService.checkAuszahlungWithVerfuegbarerBetrag(girokonto, zahlungsAuftrag.getBetrag() )) {
        result.rejectValue("betrag","error.zahlungsAuftrag", "Verfügbarer Saldo nicht ausreichend");
      }
    }

    // TODO: Auch hier muss ich 2 mal auf errors checken, das sollte sich irgendwie vermeiden lassen
    if(result.hasErrors()) {
      log.warn("Fehler beim speichern eines EinzahlungsAuftrag für Mitarbeiter: " + tmpMitarbeiter + " und KontoNr: " + tmpKontonummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      List<String> kontonummerAuswahlList = giroService.findKontoNummerOffenerGiroKontenByKundennummer(girokonto.getKunde().getKundennummer());
      model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);
      model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);
      model.addAttribute("konto", girokonto);
      model.addAttribute("requestedKontonummer", girokonto.getKontonummer().toString());

    zahlungsAuftrag.setDatAnlage(LocalDateTime.now());
    zahlungsAuftrag.setAuftragsStatus(ZahlungAuftragStatusEnum.ANGELEGT);
    zahlungsAuftrag.setKonto(girokonto);
    if (zahlungsAuftrag.getAuftragsArt().equals(ZahlungAuftragArtEnum.EINZAHLUNG)) {
      zahlungsAuftrag.setEmpfaengerKonto(girokonto.getKontonummer().toString());
      zahlungsAuftrag.setSenderKonto(kundeService.getConnectedGiroByKundennummer(tmpKundennummer));
    } else {
      zahlungsAuftrag.setSenderKonto(kundeService.getConnectedGiroByKundennummer(tmpKundennummer));
      zahlungsAuftrag.setEmpfaengerKonto(girokonto.getKontonummer().toString());
    }
    log.debug("GiroKontoEinzahlungsForm wird gespeichert für Mitarbeiter: " + tmpMitarbeiter + " und KontoNr: " + tmpKontonummer);
    zahlungsAuftragService.save(zahlungsAuftrag);
    log.debug("GiroKontoEinzahlungsForm wurde erfolgreich gespeichert für Mitarbeiter: " + tmpMitarbeiter + " und KontoNr: " + tmpKontonummer);

    return "mitarbeiter/crm/zahlungsAuftrag-form";
    }

    redirectAttrs.addFlashAttribute("zahlungsAuftragGespeichert", true);
    return "redirect:/mitarbeiter/kunde/giro/konto/showGiroKontoDetailPage?giroKontoId=" + girokonto.getId();
  }

}
