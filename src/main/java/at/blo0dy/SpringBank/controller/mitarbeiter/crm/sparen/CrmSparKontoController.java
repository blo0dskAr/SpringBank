package at.blo0dy.SpringBank.controller.mitarbeiter.crm.sparen;

import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
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
import org.springframework.validation.ObjectError;
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

  @Autowired
  public CrmSparKontoController(KundeService kundeService, SparService sparService, ZahlungsAuftragService zahlungsAuftragService, DauerAuftragService dauerAuftragService) {
    this.kundeService = kundeService;
    this.sparService = sparService;
    this.zahlungsAuftragService = zahlungsAuftragService;
    this.dauerAuftragService = dauerAuftragService;
  }


  @GetMapping("/konto")
  public String showSparKontoPage() {

    return "mitarbeiter/crm/sparen/sparKonto";
  }


  @GetMapping("/konto/showSparKontoDetailPage")
  public String showSparKontoDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                        @RequestParam("sparKontoId") Long sparKontoId) {

    log.debug("Showing SparKontoDetailPage for Mitarbeiter: " + authentication.getName() + " und KontoId: " + sparKontoId);

    Konto sparkonto = sparService.findById(sparKontoId);
    Kunde kunde = sparkonto.getKunde();

    model.addAttribute("sparkonto", sparkonto);
    model.addAttribute("kunde", kunde);
    model.addAttribute("countOffeneZA",zahlungsAuftragService.countOffeneZahlungsAuftraegeByKontoId(sparKontoId));
    model.addAttribute("countAktiveDA",dauerAuftragService.countAktiveDauerAuftraegeByKontonummer(sparkonto.getKontonummer()));

    return "mitarbeiter/crm/sparen/konto-detail";

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
    model.addAttribute("sparkonto", sparkonto);
    model.addAttribute("requestedKontonummer", sparkonto.getKontonummer().toString());
    model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);

    return "mitarbeiter/crm/sparen/zahlungsAuftrag-form";
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

    // SaldoPrüfung
    if (zahlungsAuftrag.getAuftragsArt().equals(ZahlungAuftragArtEnum.AUSZAHLUNG)) {
      result = zahlungsAuftragService.checkAuszahlungWithVerfuegbarerSaldo(result, sparkonto.getAktSaldo(), zahlungsAuftrag.getBetrag() );
    }

    // Form Validation Errors
    if(result.hasErrors()) {
      log.warn("Fehler beim speichern eines EinzahlungsAuftrag für Mitarbeiter: " + tmpMitarbeiter + " und KontoNr: " + tmpKontonummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      List<String> kontonummerAuswahlList = sparService.findKontoNummerOffenerSparKontenByKundennummer(sparkonto.getKunde().getKundennummer());
      model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);
      model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);
      model.addAttribute("sparkonto", sparkonto);
      model.addAttribute("requestedKontonummer", sparkonto.getKontonummer().toString());

      return "mitarbeiter/crm/sparen/zahlungsAuftrag-form";
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
