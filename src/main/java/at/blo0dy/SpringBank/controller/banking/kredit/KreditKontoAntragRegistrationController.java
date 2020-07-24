package at.blo0dy.SpringBank.controller.banking.kredit;


import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoRegistrationForm;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerVorlage;
import at.blo0dy.SpringBank.service.konto.kredit.KreditKontoAntragService;
import at.blo0dy.SpringBank.service.konto.kredit.KreditService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import at.blo0dy.SpringBank.service.produkt.zinssatz.ZinssatzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Controller
@RequestMapping("/kunde/banking/kredit")
public class KreditKontoAntragRegistrationController {

  private KreditKontoAntragService kreditKontoAntragService;
  private KundeService kundeService ;
  private KreditService kreditService;
  private ZinssatzService zinssatzService;

  @Autowired
  public KreditKontoAntragRegistrationController(KreditKontoAntragService kreditKontoAntragService, KundeService kundeService, KreditService kreditService, ZinssatzService zinssatzService) {
    this.kreditKontoAntragService = kreditKontoAntragService;
    this.kundeService = kundeService;
    this.kreditService = kreditService;
    this.zinssatzService = zinssatzService;
  }

  @GetMapping("/register")
  public String registerForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication , Model model) {

    KreditRechnerVorlage kv = new KreditRechnerVorlage(BigInteger.valueOf(84), zinssatzService.getAktuellerKreditZinssatz().divide(BigDecimal.valueOf(100)), BigDecimal.valueOf(10000));
    KreditRechnerErgebnis ke = new KreditRechnerErgebnis(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

    model.addAttribute("kreditrechnervorlage", kv);
    model.addAttribute("ergebnis", ke);
    model.addAttribute("activeLink", "kundeBankingKreditForm");

    String kundennummer = authentication.getName();
    model.addAttribute("kundennummer", kundennummer);
    log.debug("Kunde " + kundennummer + " ruft die KreditKontoRegistrierungsSeite auf. Standardvorlage wurde erstellt.");

    return "kunde/banking/kredit/registration";
}

  @PostMapping(value = "/register", params={"calculate"})
  public String processKreditRegistration(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                          @Validated @ModelAttribute("kreditrechnervorlage") KreditRechnerVorlage kv,
                                          BindingResult bindingResult, Model model) {
    log.debug("KreditKontoRegistrationForm zur Neuberechnung erhalten:");

      if (bindingResult.hasErrors()) {
        log.debug("Fehler beim speichern Der KreditRechnerVorlage erhalten. Wird mit Fehler neu geladen. (count=" + bindingResult.getErrorCount() + ")");
//        kv.setZinssatz(kv.getZinssatz().divide(BigDecimal.valueOf(100)));
        model.addAttribute("ergebnis", new KreditRechnerErgebnis(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        model.addAttribute("activeLink", "kundeBankingKreditForm");
        return "kunde/banking/kredit/registration";
      }  else {
        KreditRechnerErgebnis ergebnis = kreditService.getKreditRechnerErgebnis(kv);
        // Workaround bis dieses % wird als *100 dargestellt (aber nicht gerechnet) gelöst wird
//        kv.setZinssatz(kv.getZinssatz().divide(BigDecimal.valueOf(100)));
        model.addAttribute("kreditrechnervorlage", kv);
        model.addAttribute("ergebnis",ergebnis);
        model.addAttribute("calculatedCorrectly", true);
        model.addAttribute("activeLink", "kundeBankingKreditForm");

        log.debug("KreditBerechnung wurde erfolgreich durchgeführt:");
        log.debug("vorlage: " + kv);
        log.debug("ergebnis: " + ergebnis);

        return "kunde/banking/kredit/registration";
      }
    }

  @PostMapping(value = "/register", params={"saveKreditAntrag"})
  public String saveKreditRegistration(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                       @Validated @ModelAttribute("kreditrechnervorlage") KreditRechnerVorlage kv, Errors errors1,
                                       @Validated @ModelAttribute("ergebnis") KreditRechnerErgebnis ergebnis, Errors errors2,
                                       Model model, RedirectAttributes redirectAttrs) {

    log.debug("KreditKontoRegistrationForm zum speichern erhalten:");
    String kundennummer = authentication.getName();
    model.addAttribute("kundennummer", kundennummer);

    Kunde tmpKunde = kundeService.findByKundennummer(kundennummer);

    if (errors1.hasErrors() || errors2.hasErrors()) {
      kv.setZinssatz(kv.getZinssatz().divide(BigDecimal.valueOf(100)));
      model.addAttribute("ergebnis", new KreditRechnerErgebnis(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
      return "kunde/banking/kredit/registration";
    }
    ergebnis = kreditService.getKreditRechnerErgebnis(kv);

    log.debug("KreditAntrag soll gespeichert werden für Kunde: " + kundennummer);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    KreditKontoRegistrationForm kreditKontoRegistrationForm = new KreditKontoRegistrationForm(LocalDateTime.parse(LocalDateTime.now().format(formatter)), kv.getKreditBetrag(), ergebnis.getMonatlicheRate(), kv.getLaufzeit(), kv.getZinssatz(), ergebnis.getGesamtBelastung(), Long.valueOf(kundennummer) );
    log.debug("KreditKontoRegistrationForm wurde erstellt: " + kreditKontoRegistrationForm.toString());

    // Anzahl der Konten und Anträge prüfen
    int anzahlAktiveKreditKonten = kreditService.countAktiveKontenByKundeId(tmpKunde.getId());
    int anzahlEingereichtKreditAntraege = kreditKontoAntragService.countEingereichteKreditAntraegeByKundennummer(tmpKunde.getKundennummer());

    if (anzahlAktiveKreditKonten + anzahlEingereichtKreditAntraege >= 2) {
      redirectAttrs.addFlashAttribute("zuVieleAktive", true);
      log.debug("KreditKontoRegistrationForm Konnte nicht gespeichert werden, bereits zu viele Aktive Aäntrge");
    } else {
      kreditKontoAntragService.save(kreditKontoRegistrationForm.toKreditKontoAntrag());
      log.debug("KreditKontoRegistrationForm wurde erfolgreich als KreditkontoAntrag gespeichert");
      redirectAttrs.addFlashAttribute("antragGespeichert", true);
    }


    return "redirect:/kunde/banking/index" ;
  }
}
