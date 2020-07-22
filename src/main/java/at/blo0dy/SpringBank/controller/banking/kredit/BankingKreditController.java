package at.blo0dy.SpringBank.controller.banking.kredit;


import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.dauerauftrag.DauerAuftrag;
import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.KontoService;
import at.blo0dy.SpringBank.service.konto.dauerauftrag.DauerAuftragService;
import at.blo0dy.SpringBank.service.konto.kontoBuchung.KontoBuchungService;
import at.blo0dy.SpringBank.service.konto.kredit.KreditKontoAntragService;
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

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/kunde/banking/kredit")
public class BankingKreditController {

  KreditService kreditService;
  KreditKontoAntragService kreditKontoAntragService;
  KundeService kundeService;
  KontoBuchungService kontoBuchungService;
  KontoService kontoService;
  ZahlungsAuftragService zahlungsAuftragService;
  DauerAuftragService dauerAuftragService;


  @Autowired
  public BankingKreditController(KreditService kreditService, KreditKontoAntragService kreditKontoAntragService, KundeService kundeService, KontoBuchungService kontoBuchungService,
                                 KontoService kontoService, ZahlungsAuftragService zahlungsAuftragService, DauerAuftragService dauerAuftragService) {
    this.kreditService = kreditService;
    this.kreditKontoAntragService = kreditKontoAntragService;
    this.kundeService = kundeService;
    this.kontoBuchungService = kontoBuchungService;
    this.kontoService = kontoService;
    this.zahlungsAuftragService = zahlungsAuftragService;
    this.dauerAuftragService = dauerAuftragService;
  }

  @GetMapping("/kreditkontouebersicht")
  public String viewKreditKontoUebersicht(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

    String authKundennummer = authentication.getName();
    log.debug("Showing viewKreditKontoUebersicht for Kunde: " + authKundennummer);

    List<KreditKonto> kreditKontenListe = kreditService.findKreditKontenByKundennummer(authKundennummer);
    Kunde kunde = kundeService.findByKundennummer(authKundennummer);
    kreditKontenListe.forEach(kreditKonto -> kreditKonto.setKontoBuchungList(kontoBuchungService.findByKontoId(kreditKonto.getId())));
    log.debug("loaded KreditKonten: " + kreditKontenListe);
    log.debug("loaded Kunde: " + kunde.getKundennummer() + "(auth=" + authKundennummer + ")");

    model.addAttribute("kunde", kunde);
    model.addAttribute("kontenListe",kreditKontenListe);
    model.addAttribute("activeLink", "kundeBankingKreditUebersicht");

    return "kunde/banking/kontouebersicht";
  }

  @GetMapping("/showEinzahlungsFormWithKonto")
  public String showEinzahlungsForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model, @RequestParam("kontoId") Long kontoId) {

    String authKundennummer = authentication.getName();
    log.debug("Showing showAddEinzahlungForm for Kunde: " + authKundennummer);

    String requestedKontonummer = kontoService.findKontonummerById(kontoId);

    ZahlungsAuftrag zahlungsAuftrag = new ZahlungsAuftrag();
    zahlungsAuftrag.setId(0L);
    zahlungsAuftrag.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
    zahlungsAuftrag.setAuftragsDatum(LocalDate.now());
    // TODO: Glaub da ist theoretisch beschiss möglich - hier schon prüfen?
    zahlungsAuftrag.setKontonummer(requestedKontonummer);

    List<String> kontonummerAuswahlList = kreditService.findKontoNummerOffenerKreditKontenByKundennummer(authKundennummer);

    model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);
    model.addAttribute("zahlungsAuftrag",zahlungsAuftrag);

    return "kunde/banking/zahlungsAuftrag-form";
  }


  @PostMapping("/saveEinzahlungsFormWithKonto")
  public String saveEinzahlungsForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                    @Valid @ModelAttribute(name = "zahlungsAuftrag") ZahlungsAuftrag zahlungsAuftrag, BindingResult result,
                                    Model model, RedirectAttributes redirectAttrs ) {

    String authKundennummer = authentication.getName();
    log.debug("Showing showAddEinzahlungForm for Kunde: " + authKundennummer);

    KreditKonto kreditKonto;

    if (result.hasErrors()) {
      log.warn("Fehler beim speichern eines EinzahlungsAuftrag für Kunde: " + authKundennummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      List<String> kontonummerAuswahlList = kreditService.findKontoNummerOffenerKreditKontenByKundennummer(authKundennummer);

      model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);
      model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);

      return "kunde/banking/zahlungsAuftrag-form";
    }

    log.debug("Check ob Kontonummer " + zahlungsAuftrag.getKontonummer() + " des EinzahlungsAuftrages bei Kunde: " + authKundennummer + " liegt.");
    try {
      kreditKonto = kreditService.findKreditKontoByKontonummerAndKundennummer(zahlungsAuftrag.getKontonummer(), authKundennummer);
    } catch (NullPointerException e) {
      // TODO: Überlegen ob man da den Kunden nicht gleich raushaut aus dem Banking. . muss auch noch getestet werden irgendwie :)
      log.error("Check ob Kontonummer " + zahlungsAuftrag.getKontonummer() + " des EinzahlungsAuftrages bei Kunde: " + authKundennummer + " liegt - FEHLGESCHLAGEN.");
      model.addAttribute("errorObj", "errorObj");
      model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);
      return "kunde/banking/zahlungsAuftrag-form";
    }

    log.debug("Prüfungen für Kontonummer " + zahlungsAuftrag.getKontonummer() + " bei Kunde: " + authKundennummer + " erfolgreich abgeschlossen.");
    zahlungsAuftrag.setKonto(kreditKonto);
    zahlungsAuftrag.setDatAnlage(LocalDateTime.now());
    zahlungsAuftrag.setAuftragsStatus(ZahlungAuftragStatusEnum.ANGELEGT);


    if (zahlungsAuftrag.getAuftragsArt().equals(ZahlungAuftragArtEnum.EINZAHLUNG)) {
      zahlungsAuftrag.setEmpfaengerKonto(kreditKonto.getKontonummer().toString());
      zahlungsAuftrag.setSenderKonto(kundeService.getConnectedGiroByKundennummer(authKundennummer));
    } else {
      zahlungsAuftrag.setSenderKonto(kundeService.getConnectedGiroByKundennummer(authKundennummer));
      zahlungsAuftrag.setEmpfaengerKonto(kreditKonto.getKontonummer().toString());
    }

    log.debug("ZahlungsAuftrag zu Kunde: " + authKundennummer + " und Konto: " + zahlungsAuftrag.getKontonummer() + " wird gespeichert" );
    zahlungsAuftragService.save(zahlungsAuftrag);
    log.debug("ZahlungsAuftrag zu Kunde: " + authKundennummer + " und Konto: " + zahlungsAuftrag.getKontonummer() + " wurde erfolgreich gespeichert" );

    redirectAttrs.addFlashAttribute("zahlungsAuftragGespeichert", true);
    return "redirect:/kunde/banking/kredit/kreditkontouebersicht";
  }


  @GetMapping("/showKontoDetailPage")
  public String showKreditKontoDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                          @RequestParam("kontoId") Long kontoId, RedirectAttributes redirectAttrs) {

    String requestedKreditKontonummer = kontoService.findKontonummerById(kontoId);
    String authKundennummer = authentication.getName();
    // TODO: eigetnlich sollt ich irgendwie das passwort jedes mal raushauen, wenn ich nen kunden an den view  weiter geb (bzw. schon im service)
    Kunde kunde = kundeService.findByKundennummer(authKundennummer);
    model.addAttribute("kunde", kunde);
    log.debug("Showing showKreditKontoDetailPage for Kunde: " + authKundennummer + " and Konto: " + requestedKreditKontonummer );

    KreditKonto kreditKonto;

    log.debug("Check ob Kontonummer " + requestedKreditKontonummer + " des EinzahlungsAuftrages bei Kunde: " + authKundennummer + " liegt.");
    kreditKonto = kreditService.findKreditKontoByKontonummerAndKundennummer(requestedKreditKontonummer, authKundennummer);

    if (kreditKonto == null) {
      // TODO: Überlegen ob man da den Kunden nicht gleich raushaut aus dem Banking. . muss auch noch getestet werden irgendwie :)
      log.error("Check ob Kontonummer " + requestedKreditKontonummer + " des EinzahlungsAuftrages bei Kunde: " + authKundennummer + " liegt - FEHLGESCHLAGEN.");
      redirectAttrs.addFlashAttribute("beschissError", true);

      return "redirect:/kunde/banking/kredit/kreditkontouebersicht";
    } else {
      log.debug("Check ob Kontonummer " + requestedKreditKontonummer + " des EinzahlungsAuftrages bei Kunde: " + authKundennummer + " liegt. - ERFOLGREICH");

      List<ZahlungsAuftrag> zahlungsAuftragList = zahlungsAuftragService.findZahlungsAuftraegeByKontonummer(requestedKreditKontonummer);

      model.addAttribute("konto", kreditKonto);
      model.addAttribute("zahlungsAuftragsList", zahlungsAuftragList);
      model.addAttribute("countOffeneZA",zahlungsAuftragService.countOffeneZahlungsAuftraegeByKontoId(kontoId));
      model.addAttribute("countAktiveDA",dauerAuftragService.countAktiveDauerAuftraegeByKontonummer(kreditKonto.getKontonummer()));

      return "kunde/banking/konto-detail";
    }
  }



  @GetMapping("/showKreditAntragDetailPage")
  public String showKreditAntragDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                           @RequestParam("antragId") Long antragId, RedirectAttributes redirectAttrs) {

    String authKundennummer = authentication.getName();
    log.debug("Showing showKreditAntragDetailPage for Kunde: " + authKundennummer + " and Antrag: " + antragId );

    KreditKontoAntrag kreditKontoAntrag = kreditKontoAntragService.findKreditAntragByAntragIdAndKundennummer(antragId, authKundennummer);

    log.debug("Check ob ID: " + antragId + " des Antrages bei Kunde: " + authKundennummer + " liegt.");
    if (kreditKontoAntrag == null) {
      log.error("Check ob ID: " + antragId + " des Antrages bei Kunde: " + authKundennummer + " liegt. - FEHLGESCHLAGEN");
      redirectAttrs.addFlashAttribute("beschissError", true);

      return "redirect:/kunde/banking/kredit/kreditkontouebersicht";
    }
    log.debug("Check ob ID: " + antragId + " des Antrages bei Kunde: " + authKundennummer + " liegt. - ERFOLGREICH");
    model.addAttribute("kreditkontoantrag", kreditKontoAntrag);

    return "kunde/banking/kredit/antrag-detail";
  }





  @GetMapping("/showDauerAuftragForm")
  public String showDauerAuftragForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                     @RequestParam("kontoId") Long kontoId, Model model, RedirectAttributes redirectAttrs )   {

    String authKundennummer = authentication.getName();
    String tmpkontonummer = kontoService.findKontonummerById(kontoId);
    Konto testKonto = kreditService.findKreditKontoByKontonummerAndKundennummer(tmpkontonummer, authKundennummer);

    log.debug("Check ob KontoId: " + kontoId + " bei Kunde: " + authKundennummer + " liegt.");
    if (testKonto == null) {
      log.error("Check ob KontoId: " + kontoId + " bei Kunde: " + authKundennummer + " liegt. - FEHLGESCHLAGEN");
      redirectAttrs.addFlashAttribute("beschissError", true);

      return "redirect:/kunde/banking/kredit/kreditkontouebersicht";
    }

    log.debug("Showing DauerAuftragForm for Kunde: " + authentication.getName() + " und KontoId: " + kontoId);
    Konto konto = kontoService.findById(kontoId);

    DauerAuftrag dauerAuftrag = new DauerAuftrag();
    dauerAuftrag.setKonto(konto);
    dauerAuftrag.setId(0L);
    dauerAuftrag.setKontonummer(konto.getKontonummer().toString());
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

    return "redirect:/kunde/banking/kredit/showKontoDetailPage?kontoId=" + tmpKonto.getId();
  }


}









