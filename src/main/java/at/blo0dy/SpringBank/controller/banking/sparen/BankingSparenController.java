package at.blo0dy.SpringBank.controller.banking.sparen;



import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragStatusEnum;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.KontoService;
import at.blo0dy.SpringBank.service.konto.kontoBuchung.KontoBuchungService;
import at.blo0dy.SpringBank.service.konto.sparen.SparKontoAntragService;
import at.blo0dy.SpringBank.service.konto.sparen.SparService;
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

import javax.naming.Binding;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/kunde/banking/sparen")
public class BankingSparenController {

  SparService sparService;
  SparKontoAntragService sparKontoAntragService;
  KundeService kundeService;
  KontoBuchungService kontoBuchungService;
  KontoService kontoService;
  ZahlungsAuftragService zahlungsAuftragService;

  @Autowired
  public BankingSparenController(SparService sparService, KundeService kundeService, KontoBuchungService kontoBuchungService, KontoService kontoService, ZahlungsAuftragService zahlungsAuftragService,
                                 SparKontoAntragService sparKontoAntragService ) {
    this.sparService = sparService;
    this.kundeService = kundeService;
    this.kontoBuchungService = kontoBuchungService;
    this.kontoService = kontoService;
    this.zahlungsAuftragService = zahlungsAuftragService;
    this.sparKontoAntragService = sparKontoAntragService;
  }

  @GetMapping("/sparkontouebersicht")
  public String viewSparKontoUebersicht(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

    String authKundennummer = authentication.getName();
    log.debug("Showing viewSparKontoUebersicht for Kunde: " + authKundennummer);

    List<SparKonto> sparKontenListe = sparService.findSparKontoByKundennummer(authKundennummer);
    Kunde kunde = kundeService.findByKundennummer(authKundennummer);
    sparKontenListe.forEach(sparKonto -> sparKonto.setKontoBuchungList(kontoBuchungService.findByKontoId(sparKonto.getId())));
    log.debug("loaded SparKonten: " + sparKontenListe);
    log.debug("loaded Kunde: " + kunde.getKundennummer() + "(auth=" + authKundennummer + ")");

    model.addAttribute("kunde", kunde);
    model.addAttribute("sparKontenListe",sparKontenListe);
    model.addAttribute("activeLink", "kundeBankingSparenUebersicht");


    return "kunde/banking/sparen/sparkontouebersicht";
  }

  @GetMapping({"/showEinzahlungsFormWithKonto", "showAuszahlungsFormWithKonto"})
  public String showEinzahlungsForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model, @RequestParam("kontoId") Long kontoId,
                                    HttpServletRequest request) {

    String authKundennummer = authentication.getName();
    log.debug("Showing showAddEinzahlungForm for Kunde: " + authKundennummer);

    String requestedKontonummer = kontoService.findKontonummerById(kontoId);

    ZahlungsAuftrag zahlungsAuftrag = new ZahlungsAuftrag();
    zahlungsAuftrag.setId(0L);
    if (request.getRequestURI().equals("/kunde/banking/sparen/showAuszahlungsFormWithKonto")) {
      zahlungsAuftrag.setAuftragsArt(ZahlungAuftragArtEnum.AUSZAHLUNG);
    } else if (request.getRequestURI().equals("/kunde/banking/sparen/showEinzahlungsFormWithKonto")) {
      zahlungsAuftrag.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
    } else {
      // setz nix, damit mans manuell auswählen kann. (in allgemeiner maske)
    }
    zahlungsAuftrag.setAuftragsDatum(LocalDate.now());
    // TODO: Glaub da ist theoretisch beschiss möglich - hier schon prüfen?
    zahlungsAuftrag.setKontonummer(requestedKontonummer);

    List<String> kontonummerAuswahlList = sparService.findKontoNummerOffenerSparKontenByKundennummer(authKundennummer);

    model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);
    model.addAttribute("zahlungsAuftrag",zahlungsAuftrag);


    return "kunde/banking/sparen/zahlungsAuftrag-form";
  }

  @PostMapping("/saveEinzahlungsFormWithKonto")
  public String saveEinzahlungsForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                    @Valid @ModelAttribute(name = "zahlungsAuftrag") ZahlungsAuftrag zahlungsAuftrag, BindingResult result,
                                    Model model, RedirectAttributes redirectAttrs) {

    String authKundennummer = authentication.getName();
    log.debug("Showing showAddEinzahlungForm for Kunde: " + authKundennummer);

    SparKonto sparKonto;

    log.debug("Check ob Kontonummer " + zahlungsAuftrag.getKontonummer() + " des EinzahlungsAuftrages bei Kunde: " + authKundennummer + " liegt.");
    // TODO den TryCatchBlock brauch ich wahrscheinlich gar nicht, weil sparkonto=null nicht als exception kommt, sondern einfach als leeres ergebnis, das mit nem if zu checken is.
    try {
      sparKonto = sparService.findSparKontoByKontonummerAndKundennummer(zahlungsAuftrag.getKontonummer(), authKundennummer);
    } catch (NullPointerException e) {
      // TODO: Überlegen ob man da den Kunden nicht gleich raushaut aus dem Banking. . muss auch noch getestet werden irgendwie :)
      log.error("Check ob Kontonummer " + zahlungsAuftrag.getKontonummer() + " des EinzahlungsAuftrages bei Kunde: " + authKundennummer + " liegt - FEHLGESCHLAGEN.");
      model.addAttribute("errorObj", "errorObj");
      model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);
      return "kunde/banking/sparen/zahlungsAuftrag-form";
    }

    // SaldoPrüfung
    if (zahlungsAuftrag.getAuftragsArt().equals(ZahlungAuftragArtEnum.AUSZAHLUNG)) {
      if (!zahlungsAuftragService.checkAuszahlungWithVerfuegbarerSaldo(sparKonto.getAktSaldo(), zahlungsAuftrag.getBetrag() )) {
        result.rejectValue("betrag","error.zahlungsAuftrag", "Verfügbarer Saldo nicht ausreichend");
      }
    }

    if (result.hasErrors()) {
      log.warn("Fehler beim speichern eines EinzahlungsAuftrag für Kunde: " + authKundennummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      List<String> kontonummerAuswahlList = sparService.findKontoNummerOffenerSparKontenByKundennummer(authKundennummer);

      model.addAttribute("zahlungsAuftrag", zahlungsAuftrag);
      model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);

      return "kunde/banking/sparen/zahlungsAuftrag-form";
    }

    log.debug("Prüfungen für Kontonummer " + zahlungsAuftrag.getKontonummer() + " bei Kunde: " + authKundennummer + " erfolgreich abgeschlossen.");
    zahlungsAuftrag.setKonto(sparKonto);
    zahlungsAuftrag.setDatAnlage(LocalDateTime.now());
    zahlungsAuftrag.setAuftragsStatus(ZahlungAuftragStatusEnum.ANGELEGT);


    if (zahlungsAuftrag.getAuftragsArt().equals(ZahlungAuftragArtEnum.EINZAHLUNG)) {
      zahlungsAuftrag.setEmpfaengerKonto(sparKonto.getKontonummer().toString());
      zahlungsAuftrag.setSenderKonto(kundeService.getConnectedGiroByKundennummer(authKundennummer));
    } else {
      zahlungsAuftrag.setSenderKonto(kundeService.getConnectedGiroByKundennummer(authKundennummer));
      zahlungsAuftrag.setEmpfaengerKonto(sparKonto.getKontonummer().toString());
    }

    log.debug("ZahlungsAuftrag zu Kunde: " + authKundennummer + " und Konto: " + zahlungsAuftrag.getKontonummer() + " wird gespeichert" );
    zahlungsAuftragService.save(zahlungsAuftrag);
    log.debug("ZahlungsAuftrag zu Kunde: " + authKundennummer + " und Konto: " + zahlungsAuftrag.getKontonummer() + " wurde erfolgreich gespeichert" );

    redirectAttrs.addFlashAttribute("zahlungsAuftragGespeichert", true);
    return "redirect:/kunde/banking/sparen/sparkontouebersicht";
  }


//  // TODO: Das muss besser gehn als die GetMappings zu duplizieren, nur wegen des AUSZAHLUNG/EINZAHLUNG Enums.. params hat aber ned gepasst...
  // TODO: ERLEDIGT, sollte nimma benötigt werden, sollte aber eventuell noch enger, produktunabhänger gestaltet werden
//  @GetMapping("/showAuszahlungsFormWithKonto")
//  public String showAuszahlungsForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model, @RequestParam("kontoId") Long kontoId) {
//
//    String authKundennummer = authentication.getName();
//    log.debug("Showing showAddAuszahlungForm for Kunde: " + authKundennummer);
//
//    String requestedKontonummer = kontoService.findKontonummerById(kontoId);
//
//    ZahlungsAuftrag zahlungsAuftrag = new ZahlungsAuftrag();
//    zahlungsAuftrag.setId(0L);
//    zahlungsAuftrag.setAuftragsArt(ZahlungAuftragArtEnum.AUSZAHLUNG);
//    zahlungsAuftrag.setAuftragsDatum(LocalDate.now());
//    zahlungsAuftrag.setKontonummer(requestedKontonummer);
//
//
//    List<String> kontonummerAuswahlList = sparService.findKontoNummerOffenerSparKontenByKundennummer(authKundennummer);
//
//    model.addAttribute("kontonummerAuswahl", kontonummerAuswahlList);
//    model.addAttribute("zahlungsAuftrag",zahlungsAuftrag);
//
//    return "kunde/banking/sparen/zahlungsAuftrag-form";
//  }




  @GetMapping("/showKontoDetailPage")
  public String showSparKontoDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                        @RequestParam("kontoId") Long kontoId, RedirectAttributes redirectAttrs) {

    String requestedSparKontonummer = kontoService.findKontonummerById(kontoId);
    String authKundennummer = authentication.getName();
    log.debug("Showing showSparKontoDetailPage for Kunde: " + authKundennummer + " and Konto: " + requestedSparKontonummer );

    SparKonto sparKonto;

    log.debug("Check ob Kontonummer " + requestedSparKontonummer + " des EinzahlungsAuftrages bei Kunde: " + authKundennummer + " liegt.");
    sparKonto = sparService.findSparKontoByKontonummerAndKundennummer(requestedSparKontonummer, authKundennummer);

    if (sparKonto == null) {
    // TODO: Überlegen ob man da den Kunden nicht gleich raushaut aus dem Banking. . muss auch noch getestet werden irgendwie :)
    log.error("Check ob Kontonummer " + requestedSparKontonummer + " des EinzahlungsAuftrages bei Kunde: " + authKundennummer + " liegt - FEHLGESCHLAGEN.");
    redirectAttrs.addFlashAttribute("beschissError", true);

    return "redirect:/kunde/banking/sparen/sparkontouebersicht";
    } else {
      log.debug("Check ob Kontonummer " + requestedSparKontonummer + " des EinzahlungsAuftrages bei Kunde: " + authKundennummer + " liegt. - ERFOLGREICH");

      List<ZahlungsAuftrag> zahlungsAuftragList = zahlungsAuftragService.findZahlungsAuftraegeByKontonummer(requestedSparKontonummer);

      model.addAttribute("sparkonto", sparKonto);
      model.addAttribute("zahlungsAuftragsList", zahlungsAuftragList);

      return "kunde/banking/sparen/konto-detail";
    }
  }



  @GetMapping("/showSparAntragDetailPage")
  public String showSparAntragDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                         @RequestParam("antragId") Long antragId, RedirectAttributes redirectAttrs) {

    String authKundennummer = authentication.getName();
    log.debug("Showing showSparAntragDetailPage for Kunde: " + authKundennummer + " and Antrag: " + antragId );

    SparKontoAntrag sparKontoAntrag = sparKontoAntragService.findSparAntragByAntragIdAndKundennummer(antragId, authKundennummer);

    log.debug("Check ob ID: " + antragId + " des Antrages bei Kunde: " + authKundennummer + " liegt.");
    if (sparKontoAntrag == null) {
      log.error("Check ob ID: " + antragId + " des Antrages bei Kunde: " + authKundennummer + " liegt. - FEHLGESCHLAGEN");
      redirectAttrs.addFlashAttribute("beschissError", true);

      return "redirect:/kunde/banking/sparen/sparkontouebersicht";
    }
    log.debug("Check ob ID: " + antragId + " des Antrages bei Kunde: " + authKundennummer + " liegt. - ERFOLGREICH");
    model.addAttribute("sparkontoantrag", sparKontoAntrag);

    return "kunde/banking/sparen/antrag-detail";
  }


  @PostMapping("/saveSparAntragDetailPage")
  public String saveSparAntragDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model,
                                         @Valid @ModelAttribute("sparkontoantrag") SparKontoAntrag sparKontoAntrag, BindingResult result,
                                         RedirectAttributes redirectAttrs) {

    String authKundennummer = authentication.getName();

    if (result.hasErrors()) {
      log.warn("Fehler beim speichern eines SparkontoAntrags für Kunde: " + authKundennummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      model.addAttribute("sparkontoantrag", sparKontoAntrag);

      return "kunde/banking/sparen/antrag-detail";
    }

    log.debug("SparkontoAntrag: " +  sparKontoAntrag.getId() + " zu Kunde: " + authKundennummer + " wird gespeichert" );
    sparKontoAntragService.save(sparKontoAntrag);
    log.debug("SparkontoAntrag: " +  sparKontoAntrag.getId() + " zu Kunde: " + authKundennummer + " wurde erfolgreich gespeichert" );

    redirectAttrs.addFlashAttribute("antragGespeichert", true);
    return "redirect:/kunde/banking/index";
  }

}
