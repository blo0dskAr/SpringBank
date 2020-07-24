package at.blo0dy.SpringBank.controller.mitarbeiter.crm.dauerauftrag;

import at.blo0dy.SpringBank.model.enums.KontoProduktEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.dauerauftrag.DauerAuftrag;
import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import at.blo0dy.SpringBank.service.konto.KontoService;
import at.blo0dy.SpringBank.service.konto.dauerauftrag.DauerAuftragService;
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
@RequestMapping("/mitarbeiter/kunde/konto")
public class DauerAuftragController {

  DauerAuftragService dauerAuftragService;
  KontoService kontoService;


  @Autowired
  public DauerAuftragController(DauerAuftragService dauerAuftragService, KontoService kontoService) {
    this.dauerAuftragService = dauerAuftragService;
    this.kontoService = kontoService;
  }

  @GetMapping("/showDauerAuftragForm")
  public String showDauerAuftragForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                     @RequestParam("kontoId") Long kontoId, Model model,
                                     @RequestParam(required = false) Long dauerAuftragId)   {

    log.debug("Showing DauerAuftragForm for Mitarbeiter: " + authentication.getName() + " und KontoId: " + kontoId);

    Konto konto = kontoService.findById(kontoId);
    DauerAuftrag dauerAuftrag;

    if (dauerAuftragId != null) {
      dauerAuftrag = dauerAuftragService.findById(dauerAuftragId);
    } else {
      dauerAuftrag = new DauerAuftrag();
      dauerAuftrag.setKonto(konto);
      dauerAuftrag.setId(0L);
      dauerAuftrag.setKontonummer(konto.getKontonummer().toString());
    }
    if (konto instanceof KreditKonto) {
      dauerAuftrag.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
    }

    model.addAttribute("konto", konto);
    model.addAttribute("dauerAuftrag", dauerAuftrag);

    return "mitarbeiter/crm/dauerauftrag-form";
  }




  @PostMapping("/saveDauerAuftragForm")
  public String saveDauerAuftragForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                     @Valid @ModelAttribute DauerAuftrag dauerAuftrag, BindingResult result,
                                     RedirectAttributes redirectAttrs, Model model)   {

    Konto tmpKonto = dauerAuftrag.getKonto();
    String tmpKontonummer = tmpKonto.getKontonummer().toString();

    log.debug("Speichern des DauerAuftrags für Mitarbeiter: " + authentication.getName() + " und KontoNr: " + tmpKontonummer + " wurde angefordert");

    if (result.hasErrors()) {
      log.debug("Fehler beim speichern des DauerAuftrags für Mitarbeiter: " + authentication.getName() + " und KontoNr: " + tmpKontonummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      model.addAttribute("dauerAuftrag", dauerAuftrag);
      model.addAttribute("konto", tmpKonto);

      return "mitarbeiter/crm/dauerauftrag-form";
    }

    log.debug("DauerAuftrag für Mitarbeiter: " + authentication.getName() + " und KontoNr: " + tmpKontonummer + " wird gespeichert" );
    dauerAuftragService.saveNewDauerAuftrag(dauerAuftrag);
    log.debug("DauerAuftrag für Mitarbeiter: " + authentication.getName() + " und KontoNr: " + tmpKontonummer + " wurde erfolgreich gespeichert" );

    model.addAttribute("dauerAuftrag", dauerAuftrag);
    redirectAttrs.addFlashAttribute("dauerAuftragGespeichert", true);

    if (tmpKonto.getProdukt().equals(KontoProduktEnum.SPAREN)) {
      return "redirect:/mitarbeiter/kunde/sparen/konto/showSparKontoDetailPage?sparKontoId=" + tmpKonto.getId();
    } else if (tmpKonto.getProdukt().equals(KontoProduktEnum.KREDIT)) {
      return "redirect:/mitarbeiter/kunde/kredit/konto/showKreditKontoDetailPage?kreditKontoId=" + tmpKonto.getId();
    } else {
      return "redirect:/mitarbeiter/kunde/giro/konto/showGiroKontoDetailPage?giroKontoId=" + tmpKonto.getId();
    }

  }



  @GetMapping("/storniereDauerAuftrag")
  public String storniereDauerAuftrag(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                      @RequestParam Long kontoId, @RequestParam Long dauerAuftragId, RedirectAttributes redirectAttrs) {

    Konto tmpKonto = kontoService.findById(kontoId);

    dauerAuftragService.storniereDauerAuftragById(dauerAuftragId);

    redirectAttrs.addFlashAttribute("dauerAuftragStorniert", true);

    if (tmpKonto.getProdukt().equals(KontoProduktEnum.SPAREN)) {
      return "redirect:/mitarbeiter/kunde/sparen/konto/showSparKontoDetailPage?sparKontoId=" + tmpKonto.getId();
    } else if (tmpKonto.getProdukt().equals(KontoProduktEnum.KREDIT)) {
      return "redirect:/mitarbeiter/kunde/kredit/konto/showKreditKontoDetailPage?kreditKontoId=" + tmpKonto.getId();
    } else {
      return "redirect:/mitarbeiter/kunde/giro/konto/showGiroKontoDetailPage?giroKontoId=" + tmpKonto.getId();
    }

  }











}
