package at.blo0dy.SpringBank.controller.mitarbeiter.crm.kontobuchung;


import at.blo0dy.SpringBank.model.enums.KontoProduktEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.kontoBuchung.KontoBuchung;
import at.blo0dy.SpringBank.service.konto.KontoService;
import at.blo0dy.SpringBank.service.konto.kontoBuchung.KontoBuchungService;
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
import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Controller
@RequestMapping("/mitarbeiter/kunde/konto")
public class KontoBuchungController {

  KontoBuchungService kontoBuchungService ;
  KontoService kontoService;

  @Autowired
  public KontoBuchungController(KontoBuchungService kontoBuchungService, KontoService kontoService) {
    this.kontoBuchungService = kontoBuchungService;
    this.kontoService = kontoService;
  }


  @GetMapping("/showKontoBuchungForm")
  public String showKontoBuchungForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                     @RequestParam("kontoId") Long kontoId, Model model )   {

    Konto konto = kontoService.findById(kontoId);

    log.debug("Showing KontoBuchungForm for Mitarbeiter: " + authentication.getName() + " und KontoId: " + kontoId);

    KontoBuchung kontoBuchung = new KontoBuchung();
    kontoBuchung.setKonto(konto);
    kontoBuchung.setId(0L);
    kontoBuchung.setSaldoNachBuchung(BigDecimal.ZERO);
    kontoBuchung.setBuchungsDatum(LocalDate.now());

    model.addAttribute("konto", konto);
    model.addAttribute("kontoBuchung", kontoBuchung);

    return "mitarbeiter/crm/kontobuchung-form";

  }


  @PostMapping("/saveKontoBuchungForm")
  public String saveKontoBuchungForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                     @Valid @ModelAttribute KontoBuchung kontoBuchung, BindingResult result,
                                     RedirectAttributes redirectAttrs, Model model)   {

    Konto tmpKonto = kontoBuchung.getKonto();
    String tmpKontonummer = tmpKonto.getKontonummer().toString();

    log.debug("Speichern Der KontoBuchung für Mitarbeiter: " + authentication.getName() + " und KontoNr: " + tmpKontonummer + " wurde angefordert");

    if (result.hasErrors()) {
      log.debug("Fehler beim speichern Der KontoBuchung für Mitarbeiter: " + authentication.getName() + " und KontoNr: " + tmpKontonummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      model.addAttribute("kontoBuchung", kontoBuchung);
      model.addAttribute("konto", tmpKonto);

      return "mitarbeiter/crm/kontobuchung-form";

    }

    log.debug("KontoBuchung für Mitarbeiter: " + authentication.getName() + " und KontoNr: " + tmpKontonummer + " wird gespeichert" );
    BigDecimal neuerSaldo = kontoBuchungService.saveNewKontoBuchung(kontoBuchung);
    log.debug("KontoBuchung für Mitarbeiter: " + authentication.getName() + " und KontoNr: " + tmpKontonummer + " wurde erfolgreich gespeichert" );

    log.debug("Saldo für KontoNr: " + tmpKonto.getKontonummer() + " wird aktualisiert");
    kontoService.UpdateKontoSaldoById(tmpKonto.getId(), neuerSaldo);
    log.debug("Saldo für KontoNr: " + tmpKonto.getKontonummer() + " wurde erfolgreich aktualisiert");

    model.addAttribute("kontoBuchung", kontoBuchung);
    redirectAttrs.addFlashAttribute("kontoBuchungDurchgefuehrt", true);

    // TODO: hmmm da wirds langsam zeit für bissi mehr vererbung ausnutzen
    if (tmpKonto.getProdukt().equals(KontoProduktEnum.SPAREN)) {
      return "redirect:/mitarbeiter/kunde/sparen/konto/showSparKontoDetailPage?sparKontoId=" + tmpKonto.getId();
    } else if (tmpKonto.getProdukt().equals(KontoProduktEnum.KREDIT)) {
      return "redirect:/mitarbeiter/kunde/kredit/konto/showKreditKontoDetailPage?kreditKontoId=" + tmpKonto.getId();
    } else {
      return "redirect:/mitarbeiter/kunde/giro/konto/showGiroKontoDetailPage?giroKontoId=" + tmpKonto.getId();
    }

  }




}
