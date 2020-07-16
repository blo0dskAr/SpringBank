package at.blo0dy.SpringBank.controller.mitarbeiter.crm.giro;

import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
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

  @Autowired
  public CrmGiroKontoController(KundeService kundeService, GiroService giroService, ZahlungsAuftragService zahlungsAuftragService) {
    this.kundeService = kundeService;
    this.giroService = giroService;
    this.zahlungsAuftragService = zahlungsAuftragService;
  }


  @GetMapping("/konto")
  public String showGiroKontoPage() {

    return "mitarbeiter/crm/giro/giroKonto";
  }


  @GetMapping("/konto/showGiroKontoDetailPage")
  public String showGiroKontoDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                        @RequestParam("giroKontoId") Long giroKontoId) {

    log.debug("Showing GiroKontoDetailPage for Mitarbeiter: " + authentication.getName() + " und KontoId: " + giroKontoId);

    Konto girokonto = giroService.findById(giroKontoId);
    Kunde kunde = girokonto.getKunde();

    model.addAttribute("girokonto", girokonto);
    model.addAttribute("kunde", kunde);
    model.addAttribute("countOffeneZA",zahlungsAuftragService.countOffeneZahlungsAuftraegeByKontoId(giroKontoId));

    return "mitarbeiter/crm/giro/konto-detail";

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
    model.addAttribute("girokonto", girokonto);
    model.addAttribute("requestedKontonummer", girokonto.getKontonummer().toString());
    model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);

    return "mitarbeiter/crm/giro/zahlungsAuftrag-form";
  }


  @PostMapping("/konto/saveEinzahlungsForm")
  public String processEinzahlungsForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                       @Valid @ModelAttribute(name = "zahlungsAuftrag") ZahlungsAuftrag zahlungsAuftrag, BindingResult result,
                                       RedirectAttributes redirectAttrs) {

    String tmpKontonummer = zahlungsAuftrag.getKontonummer();
    String tmpMitarbeiter = authentication.getName();
    Konto girokonto = giroService.findByKontonummer(Long.valueOf(tmpKontonummer));
    String tmpKundennummer = girokonto.getKunde().getKundennummer();

    log.debug("GiroKontoEinzahlungsForm soll gespeichert werden für Mitarbeiter: " + tmpMitarbeiter + " und KontoNr: " + tmpKontonummer);

    if(result.hasErrors()) {
      log.warn("Fehler beim speichern eines EinzahlungsAuftrag für Mitarbeiter: " + tmpMitarbeiter + " und KontoNr: " + tmpKontonummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      List<String> kontonummerAuswahlList = giroService.findKontoNummerOffenerGiroKontenByKundennummer(girokonto.getKunde().getKundennummer());
      model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);
      model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);
      model.addAttribute("girokonto", girokonto);
      model.addAttribute("requestedKontonummer", girokonto.getKontonummer().toString());

      return "mitarbeiter/crm/giro/zahlungsAuftrag-form";
    }

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

    redirectAttrs.addFlashAttribute("zahlungsAuftragGespeichert", true);
    return "redirect:/mitarbeiter/kunde/giro/konto/showGiroKontoDetailPage?giroKontoId=" + girokonto.getId();
  }




}
