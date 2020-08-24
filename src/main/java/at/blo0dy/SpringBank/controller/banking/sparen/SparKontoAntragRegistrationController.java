package at.blo0dy.SpringBank.controller.banking.sparen;


import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoRegistrationForm;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.sparen.SparKontoAntragService;
import at.blo0dy.SpringBank.service.konto.sparen.SparService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Controller
@RequestMapping("/kunde/banking/sparen")
public class SparKontoAntragRegistrationController {

  // TODO: da sollt ich das repository rausrefactoren, und den service dazwischen geben
  private SparKontoAntragService sparKontoAntragService;
  private SparService sparService;
  private KundeService kundeService ;

  @Autowired
  public SparKontoAntragRegistrationController(SparKontoAntragService sparKontoAntragService, SparService sparService, KundeService kundeService) {
    this.sparKontoAntragService = sparKontoAntragService;
    this.sparService = sparService;
    this.kundeService = kundeService;
  }

  @GetMapping("/register")
  public String registerForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication , Model model) {

    SparKontoRegistrationForm sparKontoRegistrationForm = new SparKontoRegistrationForm();

    // TODO: authentication.getname als name mitschicken sollt ich mal als controller advice für authentifizierte KundenPages definieren
    String kundennummer = authentication.getName();
    log.debug("Showing SparKontoRegistrationForm für Kunde: " + kundennummer);
    model.addAttribute("kundennummer", kundennummer);

    model.addAttribute("activeLink", "kundeBankingSparenForm");
    model.addAttribute("sparkontoantrag", sparKontoRegistrationForm);

    return "kunde/banking/sparen/registration";
  }

  @PostMapping("/register")
  public String processRegistration(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                    @Valid @ModelAttribute("sparkontoantrag") SparKontoRegistrationForm form, BindingResult result,
                                    Model model, RedirectAttributes redirectAttrs) {

    log.debug("SparKontoRegistrationForm erhalten: " + form);
    Kunde tmpKunde = kundeService.findByKundennummer(form.getKundennummer().toString());

    if (result.hasErrors()) {
      log.debug("Fehler beim speichern Der SparkontoRegistrationForm erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      model.addAttribute("kundennummer", authentication.getName());
      model.addAttribute("activeLink", "kundeBankingSparenForm");
      return "kunde/banking/sparen/registration";
    }

    // AntragDatum generieren und für die Eingabe optimieren.
    // TODO: guggen wie man das global aktiviert, die bisherigen versuche waren eher mäßig..
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    form.setAntragDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));

    log.debug("SparKontoRegistrationForm wird als SparkontoAntrag gespeichert");

    // Anzahl der Konten und Anträge prüfen
    int anzahlAktiveSparKonten = sparService.countAktiveKontenByKundeId(tmpKunde.getId());
    int anzahlEingereichtSparAntraege = sparKontoAntragService.countEingereichteSparAntraegeByKundennummer(tmpKunde.getKundennummer());

    if (anzahlAktiveSparKonten + anzahlEingereichtSparAntraege >= 5) {
      redirectAttrs.addFlashAttribute("zuVieleAktive", true);
      log.debug("SparKontoRegistrationForm Konnte nicht gespeichert werden, bereits zu viele Aktive Aäntrge");
    } else {
      sparKontoAntragService.save(form.toSparKontoAntrag());
      log.debug("SparKontoRegistrationForm wurde erfolgreich als SparkontoAntrag gespeichert");
      redirectAttrs.addFlashAttribute("antragGespeichert", true);
    }

    return "redirect:/kunde/banking/index";
  }





}
