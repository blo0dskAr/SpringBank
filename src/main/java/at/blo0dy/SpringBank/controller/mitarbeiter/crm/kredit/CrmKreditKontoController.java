package at.blo0dy.SpringBank.controller.mitarbeiter.crm.kredit;

import at.blo0dy.SpringBank.model.enums.KontoProduktEnum;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.KontoService;
import at.blo0dy.SpringBank.service.konto.dauerauftrag.DauerAuftragService;
import at.blo0dy.SpringBank.service.konto.kredit.KreditService;
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
@RequestMapping("mitarbeiter/kunde/kredit")
public class CrmKreditKontoController {

  KreditService kreditService;
  KundeService kundeService;
  ZahlungsAuftragService zahlungsAuftragService;
  DauerAuftragService dauerAuftragService;
  KontoService kontoService;

  @Autowired
  public CrmKreditKontoController(KundeService kundeService, KreditService kreditService, ZahlungsAuftragService zahlungsAuftragService, KontoService kontoService, DauerAuftragService dauerAuftragService) {
    this.kundeService = kundeService;
    this.kreditService = kreditService;
    this.zahlungsAuftragService = zahlungsAuftragService;
    this.kontoService = kontoService;
    this.dauerAuftragService = dauerAuftragService;
  }


  @GetMapping("/kontoBearbeitung")
  public String showKreditKontoBearbeitungsPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

    Konto konto = new KreditKonto();
    konto.setProdukt(KontoProduktEnum.KREDIT);
    konto.setKontoStatus(KontoStatusEnum.IN_EROEFFNUNG);

    model.addAttribute("konto",konto);

    List<Konto> ergebnis = kontoService.findAll(konto);

    model.addAttribute("ergebnis", ergebnis);
    log.debug("Showing KreditKontoBearbeitungsPage for Mitarbeiter: " + authentication.getName());

    return "mitarbeiter/crm/kontosuche";
  }

  @PostMapping("/kontoBearbeitung")
  public String showSparKontoBearbeitungsPageErg(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                                 @ModelAttribute Konto konto) {
    List<Konto> ergebnis = kontoService.findAll(konto);

    model.addAttribute("ergebnis", ergebnis);
    log.debug("Showing SparKontoBearbeitungsPage for Mitarbeiter: " + authentication.getName());

    return "mitarbeiter/crm/kontosuche";
  }


  @GetMapping("/konto/showKreditKontoDetailPage")
  public String showKreditKontoDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                        @RequestParam("kreditKontoId") Long kreditKontoId) {

    log.debug("Showing KreditKontoDetailPage for Mitarbeiter: " + authentication.getName() + " und KontoId: " + kreditKontoId);

    Konto kreditkonto = kreditService.findById(kreditKontoId);
    Kunde kunde = kreditkonto.getKunde();

    model.addAttribute("konto", kreditkonto);
    model.addAttribute("kunde", kunde);
    model.addAttribute("countOffeneZA",zahlungsAuftragService.countOffeneZahlungsAuftraegeByKontoId(kreditKontoId));
    model.addAttribute("countAktiveDA",dauerAuftragService.countAktiveDauerAuftraegeByKontonummer(kreditkonto.getKontonummer()));

    return "mitarbeiter/crm/konto-detail";
  }


  @PostMapping("/konto/saveKreditKontoDetailPage")
  public String saveKreditKontoDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                          @ModelAttribute Konto konto, RedirectAttributes redirectAttrs ) {

    log.debug("Speichern der KreditKontoDetailPage for Mitarbeiter: " + authentication.getName() + " und KontoId: " + konto.getId() + " angefordert");
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

    return "redirect:/mitarbeiter/kunde/kredit/konto/showKreditKontoDetailPage?kreditKontoId=" + tmpKonto.getId();
  }



  @GetMapping("/konto/showEinzahlungsForm")
  public String showEinzahlungsForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                    @RequestParam("kreditKontoId") Long kreditKontoId, HttpServletRequest request) {

    log.debug("Showing KreditKontoEinzahlungsForm for Mitarbeiter: " + authentication.getName() + " und KontoId: " + kreditKontoId);

    Konto kreditkonto = kreditService.findById(kreditKontoId);
    ZahlungsAuftrag zahlungsAuftrag = new ZahlungsAuftrag();
    zahlungsAuftrag.setId(0L);
    zahlungsAuftrag.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
    zahlungsAuftrag.setKontonummer(kreditkonto.getKontonummer().toString());
    List<String> kontonummerAuswahlList = kreditService.findKontoNummerOffenerKreditKontenByKundennummer(kreditkonto.getKunde().getKundennummer());

    model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);
    model.addAttribute("konto", kreditkonto);
    model.addAttribute("requestedKontonummer", kreditkonto.getKontonummer().toString());
    model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);

    return "mitarbeiter/crm/zahlungsAuftrag-form";
  }


  @PostMapping("/konto/saveEinzahlungsForm")
  public String processEinzahlungsForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                       @Valid @ModelAttribute(name = "zahlungsAuftrag") ZahlungsAuftrag zahlungsAuftrag, BindingResult result,
                                       RedirectAttributes redirectAttrs) {

    String tmpKontonummer = zahlungsAuftrag.getKontonummer();
    String tmpMitarbeiter = authentication.getName();
    Konto kreditkonto = kreditService.findByKontonummer(Long.valueOf(tmpKontonummer));
    String tmpKundennummer = kreditkonto.getKunde().getKundennummer();

    log.debug("KreditKontoEinzahlungsForm soll gespeichert werden für Mitarbeiter: " + tmpMitarbeiter + " und KontoNr: " + tmpKontonummer);

    if(result.hasErrors()) {
      log.warn("Fehler beim speichern eines EinzahlungsAuftrag für Mitarbeiter: " + tmpMitarbeiter + " und KontoNr: " + tmpKontonummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      List<String> kontonummerAuswahlList = kreditService.findKontoNummerOffenerKreditKontenByKundennummer(kreditkonto.getKunde().getKundennummer());
      model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);
      model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);
      model.addAttribute("konto", kreditkonto);
      model.addAttribute("requestedKontonummer", kreditkonto.getKontonummer().toString());

      return "mitarbeiter/crm/zahlungsAuftrag-form";
    }

    zahlungsAuftrag.setDatAnlage(LocalDateTime.now());
    zahlungsAuftrag.setAuftragsStatus(ZahlungAuftragStatusEnum.ANGELEGT);
    zahlungsAuftrag.setKonto(kreditkonto);
    zahlungsAuftrag.setEmpfaengerKonto(kreditkonto.getKontonummer().toString());
    zahlungsAuftrag.setSenderKonto(kundeService.getConnectedGiroByKundennummer(tmpKundennummer));

    log.debug("KreditKontoEinzahlungsForm wird gespeichert für Mitarbeiter: " + tmpMitarbeiter + " und KontoNr: " + tmpKontonummer);
    zahlungsAuftragService.save(zahlungsAuftrag);
    log.debug("KreditKontoEinzahlungsForm wurde erfolgreich gespeichert für Mitarbeiter: " + tmpMitarbeiter + " und KontoNr: " + tmpKontonummer);

    redirectAttrs.addFlashAttribute("zahlungsAuftragGespeichert", true);
    return "redirect:/mitarbeiter/kunde/kredit/konto/showKreditKontoDetailPage?kreditKontoId=" + kreditkonto.getId();
  }




}
