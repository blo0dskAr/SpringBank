package at.blo0dy.SpringBank.controller.banking.giro;


import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.KontoService;
import at.blo0dy.SpringBank.service.konto.dauerauftrag.DauerAuftragService;
import at.blo0dy.SpringBank.service.konto.giro.GiroKontoAntragService;
import at.blo0dy.SpringBank.service.konto.giro.GiroService;
import at.blo0dy.SpringBank.service.konto.kontoBuchung.KontoBuchungService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/kunde/banking/giro")
public class BankingGiroController {

  GiroService giroService;
  GiroKontoAntragService giroKontoAntragService;
  KundeService kundeService;
  KontoBuchungService kontoBuchungService;
  KontoService kontoService;
  ZahlungsAuftragService zahlungsAuftragService;
  DauerAuftragService dauerAuftragService;


  @Autowired
  public BankingGiroController(GiroService giroService, GiroKontoAntragService giroKontoAntragService, KundeService kundeService, KontoBuchungService kontoBuchungService,
                               KontoService kontoService, ZahlungsAuftragService zahlungsAuftragService, DauerAuftragService dauerAuftragService) {
    this.giroService = giroService;
    this.giroKontoAntragService = giroKontoAntragService;
    this.kundeService = kundeService;
    this.kontoBuchungService = kontoBuchungService;
    this.kontoService = kontoService;
    this.zahlungsAuftragService = zahlungsAuftragService;
    this.dauerAuftragService = dauerAuftragService;
  }

  @GetMapping("/girokontouebersicht")
  public String viewGiroKontoUebersicht(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

    String authKundennummer = authentication.getName();
    log.debug("Showing viewGiroKontoUebersicht for Kunde: " + authKundennummer);

    List<GiroKonto> giroKontenListe = giroService.findGiroKontenByKundennummer(authKundennummer);
    Kunde kunde = kundeService.findByKundennummer(authKundennummer);
    giroKontenListe.forEach(giroKonto -> giroKonto.setKontoBuchungList(kontoBuchungService.findByKontoId(giroKonto.getId())));
    log.debug("loaded GiroKonten: " + giroKontenListe);
    log.debug("loaded Kunde: " + kunde.getKundennummer() + "(auth=" + authKundennummer + ")");

    model.addAttribute("kunde", kunde);
    model.addAttribute("kontenListe",giroKontenListe);
    model.addAttribute("activeLink", "kundeBankingGiroUebersicht");

    return "kunde/banking/kontouebersicht";
  }

  @GetMapping({"/showEinzahlungsFormWithKonto", "showAuszahlungsFormWithKonto"})
  public String showEinzahlungsForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model, @RequestParam("kontoId") Long kontoId,
                                    HttpServletRequest request) {

    String authKundennummer = authentication.getName();
    log.debug("Showing showAddEinzahlungForm for Kunde: " + authKundennummer);

    Konto girokonto = giroService.findById(kontoId);
    String requestedKontonummer = girokonto.getKontonummer();
    ZahlungsAuftrag zahlungsAuftrag = new ZahlungsAuftrag();
    zahlungsAuftrag.setId(0L);

    if (request.getRequestURI().equals("/kunde/banking/giro/showAuszahlungsFormWithKonto")) {
      zahlungsAuftrag.setAuftragsArt(ZahlungAuftragArtEnum.AUSZAHLUNG);
    } else if (request.getRequestURI().equals("/kunde/banking/giro/showEinzahlungsFormWithKonto")) {
      zahlungsAuftrag.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
    } else {
      // setz nix, damit mans manuell auswählen kann. (ggf. in allgemeiner maske (nicht umgesetzt))
    }
    zahlungsAuftrag.setAuftragsDatum(LocalDate.now());
    // TODO: Glaub da ist theoretisch beschiss möglich - hier schon prüfen?
    zahlungsAuftrag.setKontonummer(requestedKontonummer);

    List<String> kontonummerAuswahlList = giroService.findKontoNummerOffenerGiroKontenByKundennummer(authKundennummer);

    model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);
    model.addAttribute("zahlungsAuftrag",zahlungsAuftrag);
    model.addAttribute("konto", girokonto);

    return "kunde/banking/zahlungsAuftrag-form";
  }


  @PostMapping("/saveEinzahlungsFormWithKonto")
  public String saveEinzahlungsForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                    @Valid @ModelAttribute(name = "zahlungsAuftrag") ZahlungsAuftrag zahlungsAuftrag, BindingResult result,
                                    Model model, RedirectAttributes redirectAttrs) {

    String authKundennummer = authentication.getName();
    GiroKonto giroKonto;
    log.debug("Showing showAddEinzahlungForm for Kunde: " + authKundennummer);

    if (result.hasErrors()) {
      log.warn("Fehler beim speichern eines EinzahlungsAuftrag für Kunde: " + authKundennummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      List<String> kontonummerAuswahlList = giroService.findKontoNummerOffenerGiroKontenByKundennummer(authKundennummer);
      giroKonto = giroService.findByKontonummer(zahlungsAuftrag.getKontonummer());

      model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);
      model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);
      model.addAttribute("konto", giroKonto);

      return "kunde/banking/zahlungsAuftrag-form";
    }

    log.debug("Check ob Kontonummer " + zahlungsAuftrag.getKontonummer() + " des EinzahlungsAuftrages bei Kunde: " + authKundennummer + " liegt.");

    giroKonto = giroService.findGiroKontoByKontonummerAndKundennummer(zahlungsAuftrag.getKontonummer(), authKundennummer);
    if (giroKonto == null) {
      // TODO: Überlegen ob man da den Kunden nicht gleich raushaut aus dem Banking.
      log.error("Check ob Kontonummer " + zahlungsAuftrag.getKontonummer() + " des EinzahlungsAuftrages bei Kunde: " + authKundennummer + " liegt - FEHLGESCHLAGEN.");
      model.addAttribute("errorObj", "errorObj");
      model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);
      return "kunde/banking/zahlungsAuftrag-form";
    }


    // SaldoPrüfung
    if (zahlungsAuftrag.getAuftragsArt().equals(ZahlungAuftragArtEnum.AUSZAHLUNG)) {
      if (!zahlungsAuftragService.checkAuszahlungWithVerfuegbarerBetrag(giroKonto, zahlungsAuftrag.getBetrag(), false)) {
        result.rejectValue("betrag","error.zahlungsAuftrag", "Verfügbarer Saldo nicht ausreichend");
      }
    }

    // TODO: hab hier nochmal die fehlerprüfung einbauen müssen, weil mir sonst entweder ne nullpointer exception fliegt, oder die saldoprüfung zwar durchgeführt, aber der fehler nicht im result gespeichert wird ...
    if (result.hasErrors()) {
      log.warn("Fehler beim speichern eines EinzahlungsAuftrag für Kunde: " + authKundennummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      List<String> kontonummerAuswahlList = giroService.findKontoNummerOffenerGiroKontenByKundennummer(authKundennummer);

      model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);
      model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);

      return "kunde/banking/zahlungsAuftrag-form";
    }

    log.debug("Prüfungen für Kontonummer " + zahlungsAuftrag.getKontonummer() + " bei Kunde: " + authKundennummer + " erfolgreich abgeschlossen.");
    zahlungsAuftrag.setKonto(giroKonto);
    zahlungsAuftrag.setDatAnlage(LocalDateTime.now());
    zahlungsAuftrag.setAuftragsStatus(ZahlungAuftragStatusEnum.ANGELEGT);


    if (zahlungsAuftrag.getAuftragsArt().equals(ZahlungAuftragArtEnum.EINZAHLUNG)) {
      zahlungsAuftrag.setEmpfaengerKonto(giroKonto.getKontonummer());
      zahlungsAuftrag.setSenderKonto(kundeService.getConnectedGiroByKundennummer(authKundennummer));
    } else {
      zahlungsAuftrag.setSenderKonto(kundeService.getConnectedGiroByKundennummer(authKundennummer));
      zahlungsAuftrag.setEmpfaengerKonto(giroKonto.getKontonummer());
    }

    log.debug("ZahlungsAuftrag zu Kunde: " + authKundennummer + " und Konto: " + zahlungsAuftrag.getKontonummer() + " wird gespeichert" );
    zahlungsAuftragService.save(zahlungsAuftrag);
    log.debug("ZahlungsAuftrag zu Kunde: " + authKundennummer + " und Konto: " + zahlungsAuftrag.getKontonummer() + " wurde erfolgreich gespeichert" );

    redirectAttrs.addFlashAttribute("zahlungsAuftragGespeichert", true);
    return "redirect:/kunde/banking/giro/girokontouebersicht";
  }

  @GetMapping("/showKontoDetailPage")
  public String showGiroKontoDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                        @RequestParam("kontoId") Long kontoId, RedirectAttributes redirectAttrs) {

    String requestedGiroKontonummer = kontoService.findKontonummerById(kontoId);
    String authKundennummer = authentication.getName();
    Kunde kunde = kundeService.findByKundennummer(authKundennummer);
    model.addAttribute("kunde", kunde);
    log.debug("Showing showGiroKontoDetailPage for Kunde: " + authKundennummer + " and Konto: " + requestedGiroKontonummer );

    GiroKonto giroKonto;

    log.debug("Check ob Kontonummer " + requestedGiroKontonummer + " des EinzahlungsAuftrages bei Kunde: " + authKundennummer + " liegt.");
    giroKonto = giroService.findGiroKontoByKontonummerAndKundennummer(requestedGiroKontonummer, authKundennummer);

    if (giroKonto == null) {
      // TODO: Überlegen ob man da den Kunden nicht gleich raushaut aus dem Banking.
      log.error("Check ob Kontonummer " + requestedGiroKontonummer + " des EinzahlungsAuftrages bei Kunde: " + authKundennummer + " liegt - FEHLGESCHLAGEN.");
      redirectAttrs.addFlashAttribute("beschissError", true);

      return "redirect:/kunde/banking/giro/girokontouebersicht";
    } else {
      log.debug("Check ob Kontonummer " + requestedGiroKontonummer + " des EinzahlungsAuftrages bei Kunde: " + authKundennummer + " liegt. - ERFOLGREICH");

      List<ZahlungsAuftrag> zahlungsAuftragList = zahlungsAuftragService.findZahlungsAuftraegeByKontonummer(requestedGiroKontonummer);

      model.addAttribute("konto", giroKonto);
      model.addAttribute("zahlungsAuftragsList", zahlungsAuftragList);
      model.addAttribute("countOffeneZA",zahlungsAuftragService.countOffeneZahlungsAuftraegeByKontoId(kontoId));
      model.addAttribute("countAktiveDA",dauerAuftragService.countAktiveDauerAuftraegeByKontonummer(giroKonto.getKontonummer()));

      return "kunde/banking/konto-detail";
    }
  }


}
