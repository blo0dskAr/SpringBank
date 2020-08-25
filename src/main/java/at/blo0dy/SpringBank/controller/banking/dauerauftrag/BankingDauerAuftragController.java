package at.blo0dy.SpringBank.controller.banking.dauerauftrag;

import at.blo0dy.SpringBank.model.enums.KontoProduktEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.dauerauftrag.DauerAuftrag;
import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import at.blo0dy.SpringBank.service.konto.KontoService;
import at.blo0dy.SpringBank.service.konto.dauerauftrag.DauerAuftragService;
import at.blo0dy.SpringBank.service.konto.giro.GiroService;
import at.blo0dy.SpringBank.service.konto.kredit.KreditService;
import at.blo0dy.SpringBank.service.konto.sparen.SparService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping({"/kunde/banking/sparen", "/kunde/banking/kredit", "/kunde/banking/giro"})
public class BankingDauerAuftragController {

  KontoService kontoService;
  SparService sparService;
  KreditService kreditService;
  GiroService giroService;
  DauerAuftragService dauerAuftragService;


  @Autowired
  public BankingDauerAuftragController(KontoService kontoService, SparService sparService, KreditService kreditService, GiroService giroService, DauerAuftragService dauerAuftragService) {
    this.kontoService = kontoService;
    this.sparService = sparService;
    this.kreditService = kreditService;
    this.giroService = giroService;
    this.dauerAuftragService = dauerAuftragService;
  }

  @GetMapping("/showDauerAuftragForm")
  public String showDauerAuftragForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                     @RequestParam("kontoId") Long kontoId, Model model, RedirectAttributes redirectAttrs,
                                     @RequestParam(required = false) Long dauerAuftragId)   {

    String authKundennummer = authentication.getName();
    Konto tmpKonto = kontoService.findById(kontoId);
    String tmpkontonummer = tmpKonto.getKontonummer().toString() ;
    KontoProduktEnum tmpProdukt = tmpKonto.getProdukt();

    log.debug("Showing DauerAuftragForm for Kunde: " + authKundennummer + " und KontoId: " + kontoId);

    Konto testKonto;

    if (tmpProdukt == KontoProduktEnum.SPAREN) {
      testKonto = sparService.findSparKontoByKontonummerAndKundennummer(tmpkontonummer, authKundennummer);
    } else if (tmpProdukt == KontoProduktEnum.KREDIT) {
      testKonto = kreditService.findKreditKontoByKontonummerAndKundennummer(tmpkontonummer, authKundennummer);
    } else {
      testKonto = giroService.findGiroKontoByKontonummerAndKundennummer(tmpkontonummer, authKundennummer);
    }

    log.debug("Check ob KontoId: " + kontoId + " bei Kunde: " + authKundennummer + " liegt.");
    if (testKonto == null) {
      log.error("Check ob KontoId: " + kontoId + " bei Kunde: " + authKundennummer + " liegt. - FEHLGESCHLAGEN");
      redirectAttrs.addFlashAttribute("beschissError", true);

      if (tmpProdukt == KontoProduktEnum.SPAREN) {
      return "redirect:/kunde/banking/sparen/sparkontouebersicht";
      } else if (tmpProdukt == KontoProduktEnum.KREDIT) {
        return "redirect:/kunde/banking/kredit/kreditkontouebersicht";
      } else {
        return "redirect:/kunde/banking/giro/girokontouebersicht";
      }
    }

    DauerAuftrag dauerAuftrag;

    if (dauerAuftragId != null) {
      log.debug("DauerauftragId: " + dauerAuftragId + " für KontoId: " + kontoId + " wird gesucht.");
      dauerAuftrag = dauerAuftragService.findById(dauerAuftragId);
      log.debug("DauerauftragId: " + dauerAuftragId + " für KontoId: " + kontoId + " wurde gefunden.");
    } else {
      log.debug("Kein Dauerauftrag für KontoId: " + kontoId + " mitgeliefert. Neuer Dauerauftrag wird erstellt.");
      dauerAuftrag = new DauerAuftrag();
      dauerAuftrag.setKonto(tmpKonto);
      dauerAuftrag.setId(0L);
      dauerAuftrag.setKontonummer(tmpkontonummer);
    }
    if (tmpKonto instanceof KreditKonto) {
      dauerAuftrag.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
    }

    model.addAttribute("konto", tmpKonto);
    model.addAttribute("dauerAuftrag", dauerAuftrag);

    return "kunde/banking/dauerauftrag-form";
  }




  @PostMapping("/saveDauerAuftragForm")
  public String saveDauerAuftragForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                     @Valid @ModelAttribute DauerAuftrag dauerAuftrag, BindingResult result,
                                     RedirectAttributes redirectAttrs, Model model) {

    Konto tmpKonto = dauerAuftrag.getKonto();
    String tmpKontonummer = tmpKonto.getKontonummer().toString();
    KontoProduktEnum tmpProdukt = tmpKonto.getProdukt();

    log.debug("Speichern des DauerAuftrags für Mitarbeiter: " + authentication.getName() + " und KontoNr: " + tmpKontonummer + " wurde angefordert");

    if (result.hasErrors()) {
      log.debug("Fehler beim speichern des DauerAuftrags für Mitarbeiter: " + authentication.getName() + " und KontoNr: " + tmpKontonummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      model.addAttribute("dauerAuftrag", dauerAuftrag);
      model.addAttribute("konto", tmpKonto);

      return "kunde/banking/dauerauftrag-form";
    }

    log.debug("DauerAuftrag für Mitarbeiter: " + authentication.getName() + " und KontoNr: " + tmpKontonummer + " wird gespeichert");
    dauerAuftragService.saveNewDauerAuftrag(dauerAuftrag);
    log.debug("DauerAuftrag für Mitarbeiter: " + authentication.getName() + " und KontoNr: " + tmpKontonummer + " wurde erfolgreich gespeichert");

    model.addAttribute("dauerAuftrag", dauerAuftrag);
    redirectAttrs.addFlashAttribute("DauerAuftragGespeichert", true);
    if (tmpProdukt == KontoProduktEnum.SPAREN) {
      return "redirect:/kunde/banking/sparen/showKontoDetailPage?kontoId=" + tmpKonto.getId();
    } else if (tmpProdukt == KontoProduktEnum.KREDIT) {
      return "redirect:/kunde/banking/kredit/showKontoDetailPage?kontoId=" + tmpKonto.getId();
    } else {
      return "redirect:/kunde/banking/giro/showKontoDetailPage?kontoId=" + tmpKonto.getId();
    }
  }



  @GetMapping("/storniereDauerAuftrag")
  public String storniereDauerAuftrag(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                      @RequestParam Long kontoId, @RequestParam Long dauerAuftragId, RedirectAttributes redirectAttrs) {
    log.debug("DauerauftragId: " + dauerAuftragId + " für KontoId: " + " wird storniert.");

    Konto tmpKonto = kontoService.findById(kontoId);

    dauerAuftragService.storniereDauerAuftragById(dauerAuftragId);
    log.debug("DauerauftragId: " + dauerAuftragId + " für KontoId: " + " wurde erfolgreich storniert.");

    redirectAttrs.addFlashAttribute("dauerAuftragStorniert", true);

    if (tmpKonto.getProdukt().equals(KontoProduktEnum.SPAREN)) {
      return "redirect:/kunde/banking/sparen/showKontoDetailPage?kontoId=" + tmpKonto.getId();
    } else if (tmpKonto.getProdukt().equals(KontoProduktEnum.KREDIT)) {
      return "redirect:/kunde/banking/kredit/showKontoDetailPage?kontoId=" + tmpKonto.getId();
    } else {
      return "redirect:/kunde/banking/giro/showKontoDetailPage?kontoId=" + tmpKonto.getId();
    }

  }




}
