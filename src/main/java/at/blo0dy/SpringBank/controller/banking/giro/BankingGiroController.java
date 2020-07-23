package at.blo0dy.SpringBank.controller.banking.giro;


import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragStatusEnum;
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

    String requestedKontonummer = kontoService.findKontonummerById(kontoId);

    ZahlungsAuftrag zahlungsAuftrag = new ZahlungsAuftrag();
    zahlungsAuftrag.setId(0L);
    if (request.getRequestURI().equals("/kunde/banking/giro/showAuszahlungsFormWithKonto")) {
      zahlungsAuftrag.setAuftragsArt(ZahlungAuftragArtEnum.AUSZAHLUNG);
    } else if (request.getRequestURI().equals("/kunde/banking/giro/showEinzahlungsFormWithKonto")) {
      zahlungsAuftrag.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
    } else {
      // setz nix, damit mans manuell auswählen kann. (in allgemeiner maske)
    }
    zahlungsAuftrag.setAuftragsDatum(LocalDate.now());
    // TODO: Glaub da ist theoretisch beschiss möglich - hier schon prüfen?
    zahlungsAuftrag.setKontonummer(requestedKontonummer);

    List<String> kontonummerAuswahlList = giroService.findKontoNummerOffenerGiroKontenByKundennummer(authKundennummer);

    model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);
    model.addAttribute("zahlungsAuftrag",zahlungsAuftrag);


    return "kunde/banking/zahlungsAuftrag-form";
  }


  @PostMapping("/saveEinzahlungsFormWithKonto")
  public String saveEinzahlungsForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                    @Valid @ModelAttribute(name = "zahlungsAuftrag") ZahlungsAuftrag zahlungsAuftrag, BindingResult result,
                                    Model model, RedirectAttributes redirectAttrs) {

    String authKundennummer = authentication.getName();
    log.debug("Showing showAddEinzahlungForm for Kunde: " + authKundennummer);

    if (result.hasErrors()) {
      log.warn("Fehler beim speichern eines EinzahlungsAuftrag für Kunde: " + authKundennummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      List<String> kontonummerAuswahlList = giroService.findKontoNummerOffenerGiroKontenByKundennummer(authKundennummer);

      model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);
      model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);

      return "kunde/banking/zahlungsAuftrag-form";
    }

    GiroKonto giroKonto;

    log.debug("Check ob Kontonummer " + zahlungsAuftrag.getKontonummer() + " des EinzahlungsAuftrages bei Kunde: " + authKundennummer + " liegt.");
    try {
      giroKonto = giroService.findGiroKontoByKontonummerAndKundennummer(zahlungsAuftrag.getKontonummer(), authKundennummer);
    } catch (NullPointerException e) {
      // TODO: Überlegen ob man da den Kunden nicht gleich raushaut aus dem Banking. . muss auch noch getestet werden irgendwie :)
      log.error("Check ob Kontonummer " + zahlungsAuftrag.getKontonummer() + " des EinzahlungsAuftrages bei Kunde: " + authKundennummer + " liegt - FEHLGESCHLAGEN.");
      model.addAttribute("errorObj", "errorObj");
      model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);
      return "kunde/banking/zahlungsAuftrag-form";
    }

    // SaldoPrüfung
    if (zahlungsAuftrag.getAuftragsArt().equals(ZahlungAuftragArtEnum.AUSZAHLUNG)) {
      if (!zahlungsAuftragService.checkAuszahlungWithVerfuegbarerBetrag(giroKonto, zahlungsAuftrag.getBetrag() )) {
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
      zahlungsAuftrag.setEmpfaengerKonto(giroKonto.getKontonummer().toString());
      zahlungsAuftrag.setSenderKonto(kundeService.getConnectedGiroByKundennummer(authKundennummer));
    } else {
      zahlungsAuftrag.setSenderKonto(kundeService.getConnectedGiroByKundennummer(authKundennummer));
      zahlungsAuftrag.setEmpfaengerKonto(giroKonto.getKontonummer().toString());
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
    // TODO: eigetnlich sollt ich irgendwie das passwort jedes mal raushauen, wenn ich nen kunden an den view  weiter geb (bzw. schon im service)
    Kunde kunde = kundeService.findByKundennummer(authKundennummer);
    model.addAttribute("kunde", kunde);
    log.debug("Showing showGiroKontoDetailPage for Kunde: " + authKundennummer + " and Konto: " + requestedGiroKontonummer );

    GiroKonto giroKonto;

    log.debug("Check ob Kontonummer " + requestedGiroKontonummer + " des EinzahlungsAuftrages bei Kunde: " + authKundennummer + " liegt.");
    giroKonto = giroService.findGiroKontoByKontonummerAndKundennummer(requestedGiroKontonummer, authKundennummer);

    if (giroKonto == null) {
      // TODO: Überlegen ob man da den Kunden nicht gleich raushaut aus dem Banking. . muss auch noch getestet werden irgendwie :)
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




  @GetMapping("/showGiroAntragDetailPage")
  public String showGiroAntragDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                         @RequestParam("antragId") Long antragId, RedirectAttributes redirectAttrs) {

    String authKundennummer = authentication.getName();
    log.debug("Showing showGiroAntragDetailPage for Kunde: " + authKundennummer + " and Antrag: " + antragId );

    GiroKontoAntrag giroKontoAntrag = giroKontoAntragService.findGiroAntragByAntragIdAndKundennummer(antragId, authKundennummer);

    log.debug("Check ob ID: " + antragId + " des Antrages bei Kunde: " + authKundennummer + " liegt.");
    if (giroKontoAntrag == null) {
      log.error("Check ob ID: " + antragId + " des Antrages bei Kunde: " + authKundennummer + " liegt. - FEHLGESCHLAGEN");
      redirectAttrs.addFlashAttribute("beschissError", true);

      return "redirect:/kunde/banking/giro/girokontouebersicht";
    }
    log.debug("Check ob ID: " + antragId + " des Antrages bei Kunde: " + authKundennummer + " liegt. - ERFOLGREICH");
    model.addAttribute("girokontoantrag", giroKontoAntrag);

    return "kunde/banking/giro/antrag-detail";
  }


  @PostMapping("/saveGiroAntragDetailPage")
  public String saveGiroAntragDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                         @Valid @ModelAttribute("girokontoantrag") GiroKontoAntrag giroKontoAntrag, BindingResult result,
                                         RedirectAttributes redirectAttrs) {

    String authKundennummer = authentication.getName();

    if (result.hasErrors()) {
      log.warn("Fehler beim speichern eines GirokontoAntrags für Kunde: " + authKundennummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      model.addAttribute("girokontoantrag", giroKontoAntrag);

      return "kunde/banking/giro/antrag-detail";
    }

    log.debug("GirokontoAntrag: " +  giroKontoAntrag.getId() + " zu Kunde: " + authKundennummer + " wird gespeichert" );
    giroKontoAntragService.save(giroKontoAntrag);
    log.debug("GirokontoAntrag: " +  giroKontoAntrag.getId() + " zu Kunde: " + authKundennummer + " wurde erfolgreich gespeichert" );

    redirectAttrs.addFlashAttribute("antragGespeichert", true);
    return "redirect:/kunde/banking/index";
  }






/*  @GetMapping("/showDauerAuftragForm")
  public String showDauerAuftragForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                     @RequestParam("kontoId") Long kontoId, Model model, RedirectAttributes redirectAttrs,
                                     @RequestParam(required = false) Long dauerAuftragId)   {

    String authKundennummer = authentication.getName();
    String tmpkontonummer = kontoService.findKontonummerById(kontoId);
    Konto testKonto = giroService.findGiroKontoByKontonummerAndKundennummer(tmpkontonummer, authKundennummer);

    log.debug("Check ob KontoId: " + kontoId + " bei Kunde: " + authKundennummer + " liegt.");
    if (testKonto == null) {
      log.error("Check ob KontoId: " + kontoId + " bei Kunde: " + authKundennummer + " liegt. - FEHLGESCHLAGEN");
      redirectAttrs.addFlashAttribute("beschissError", true);

      return "redirect:/kunde/banking/sparen/sparkontouebersicht";
    }

    log.debug("Showing DauerAuftragForm for Kunde: " + authentication.getName() + " und KontoId: " + kontoId);
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

    return "kunde/banking/dauerauftrag-form";
  }




  @PostMapping("/saveDauerAuftragForm")
  public String saveDauerAuftragForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                     @Valid @ModelAttribute DauerAuftrag dauerAuftrag, BindingResult result,
                                     RedirectAttributes redirectAttrs, Model model) {

    Konto tmpKonto = dauerAuftrag.getKonto();
    String tmpKontonummer = tmpKonto.getKontonummer().toString();

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

    // TODO: hmmm da wirds langsam zeit für noch bissi mehr vererbung ausnutzen

    return "redirect:/kunde/banking/giro/showKontoDetailPage?kontoId=" + tmpKonto.getId();
  }*/


}
