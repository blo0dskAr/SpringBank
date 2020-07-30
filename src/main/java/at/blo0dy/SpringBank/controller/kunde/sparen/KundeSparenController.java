package at.blo0dy.SpringBank.controller.kunde.sparen;


import at.blo0dy.SpringBank.model.produkt.sparen.AdvancedSparZinsRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.sparen.AdvancedSparZinsRechnerVorlage;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerVorlage;
import at.blo0dy.SpringBank.service.konto.sparen.SparService;
import at.blo0dy.SpringBank.service.produkt.zinssatz.ZinssatzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/kunde/sparen")
public class KundeSparenController {

  SparService sparService;
  ZinssatzService zinssatzService;


  @Autowired
  public KundeSparenController(SparService sparService, ZinssatzService zinssatzService) {
    this.sparService = sparService;
    this.zinssatzService = zinssatzService;
  }

  @GetMapping({"/index", "/", ""})
  public String viewSparenIndex(Model model) {
    log.debug("SparIndexPage wird aufgerufen.");
    model.addAttribute("activeLink", "kundeSparenHome");

    return "kunde/sparen/index";
  }

  @GetMapping("/rechner")
  public String viewSparenRechner(Model model) {
    log.debug("SparRechnerPage wird aufgerufen");

    // / 100 division workaround fürs frontend
    SparZinsRechnerVorlage sv = new SparZinsRechnerVorlage(LocalDate.now(), zinssatzService.getAktuellerSparZinssatz().divide(BigDecimal.valueOf(100)) ,BigDecimal.valueOf(5000));
    SparZinsRechnerErgebnis se = new SparZinsRechnerErgebnis( BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

    model.addAttribute("activeLink", "kundeSparenRechner1");
    model.addAttribute("ergebnis",se);
    model.addAttribute("sparzinsrechnervorlage", sv);

    return "kunde/sparen/rechner";
  }

  @PostMapping("/rechner")
  public String berechneSparZinsen(@Valid @ModelAttribute("sparzinsrechnervorlage") SparZinsRechnerVorlage sv, BindingResult result, Model model) {
    log.debug("Unverbindliche Sparzinsberechnung wird durchgeführt");
    if (result.hasErrors()) {
      log.debug("Fehler bei der Eingabe erhalten. Seite wird neu geladen.");
//      sv.setZinssatz(sv.getZinssatz().divide(BigDecimal.valueOf(100)));
      SparZinsRechnerErgebnis se = new SparZinsRechnerErgebnis( BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
      model.addAttribute("ergebnis",se);
      model.addAttribute("activeLink", "kundeSparenRechner1");
      return "kunde/sparen/rechner";
    }  else {
      SparZinsRechnerErgebnis se = sparService.getSparZinsRechnerEregebnis(sv);

      // Workaround bis dieses % wird als *100 dargestellt (aber nicht gerechnet) gelöst wird
//      sv.setZinssatz(sv.getZinssatz().divide(BigDecimal.valueOf(100)));
      model.addAttribute("activeLink", "kundeSparenRechner1");
      model.addAttribute("sparzinsrechnervorlage", sv);
      model.addAttribute("ergebnis",se);
      return "kunde/sparen/rechner";
    }
  }

  @GetMapping("/rechner2")
  public String viewSparenRechner2(Model model) {
    log.debug("Erweiterte SparRechnerForm wird aufgerufen");
    BigDecimal anfangsWerte = BigDecimal.ZERO;

    // / 100 division workaround fürs frontend
    AdvancedSparZinsRechnerVorlage sv = new AdvancedSparZinsRechnerVorlage(BigInteger.valueOf(5), zinssatzService.getAktuellerSparZinssatz().divide(BigDecimal.valueOf(100)), BigDecimal.ZERO,BigDecimal.valueOf(100), BigDecimal.valueOf(5000));
    AdvancedSparZinsRechnerErgebnis se = new AdvancedSparZinsRechnerErgebnis(anfangsWerte, anfangsWerte, anfangsWerte, anfangsWerte, anfangsWerte);

    model.addAttribute("activeLink", "kundeSparenRechner2");
    model.addAttribute("ergebnis", se);
    model.addAttribute("sparzinsrechnervorlage", sv);

    return "kunde/sparen/rechner2";
  }

  @PostMapping("/rechner2")
  public String berechneSparZinsen2(@Validated @ModelAttribute("sparzinsrechnervorlage") AdvancedSparZinsRechnerVorlage sv, BindingResult result,  Model model ) {
    log.debug("Unverbindliche erweiterte SparzinsBerechnung wird durchgeführt.");
    if (result.hasErrors()) {
      log.debug("Fehler bei der Eingabe erhalten. Seite wird neu geladen.");
//      sv.setZinssatz(sv.getZinssatz().divide(BigDecimal.valueOf(100)));
      model.addAttribute("activeLink", "kundeSparenRechner2");
      return "kunde/sparen/rechner2";
    }  else {
      List<AdvancedSparZinsRechnerErgebnis> se = sparService.getAdvancedSparZinsRechnerErgebnis(sv);
      // Workaround bis dieses % wird als *100 dargestellt (aber nicht gerechnet) gelöst wird
//      sv.setZinssatz(sv.getZinssatz().divide(BigDecimal.valueOf(100)));
      model.addAttribute("activeLink", "kundeSparenRechner2");
      model.addAttribute("sparzinsrechnervorlage", sv);
      model.addAttribute("ergebnis",se);
      return "kunde/sparen/rechner2";
    }
  }

}
