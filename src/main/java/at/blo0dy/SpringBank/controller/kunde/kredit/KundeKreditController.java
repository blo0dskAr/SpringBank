package at.blo0dy.SpringBank.controller.kunde.kredit;


import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerVorlage;
import at.blo0dy.SpringBank.service.konto.kredit.KreditService;
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

import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
@Controller
@RequestMapping("/kunde/kredit")
public class KundeKreditController {

  KreditService kreditService;
  ZinssatzService zinssatzService;

  @Autowired
  public KundeKreditController(KreditService kreditService, ZinssatzService zinssatzService) {
    this.kreditService = kreditService;
    this.zinssatzService = zinssatzService;
  }

  @GetMapping({"/index", "/", ""})
  public String viewKreditIndex(Model model) {
    log.debug("KreditIndexPage wird aufgerufen.");
    model.addAttribute("activeLink", "kundeKreditHome");

    return "kunde/kredit/index";
  }

  @GetMapping("/rechner")
  public String showKreditRechnerForm(Model model) {
    log.debug("KreditRechnerForm wird aufgerufen.");

    KreditRechnerVorlage kv = new KreditRechnerVorlage(BigInteger.valueOf(84), zinssatzService.getAktuellerKreditZinssatz().divide(BigDecimal.valueOf(100)), BigDecimal.valueOf(10000));
    KreditRechnerErgebnis ke = new KreditRechnerErgebnis(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

    model.addAttribute("kreditrechnervorlage", kv);
    model.addAttribute("ergebnis", ke);
    model.addAttribute("activeLink", "kundeKreditRechner");

    return "kunde/kredit/rechner";
  }

  @PostMapping("/rechner")
  public String berechneKredit(@Validated @ModelAttribute("kreditrechnervorlage") KreditRechnerVorlage kv, BindingResult result, Model model ) {
    log.debug("Unverbindliche Kreditberechnung wird durchgeführt.");
    if (result.hasErrors()) {
      log.debug("Fehler bei der Eingabe erhalten. Seite wird neu geladen.");
      model.addAttribute("ergebnis", new KreditRechnerErgebnis(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
      model.addAttribute("activeLink", "kundeKreditRechner");
      return "kunde/kredit/rechner";
    }  else {
      KreditRechnerErgebnis ke = kreditService.getKreditRechnerErgebnis(kv);
      model.addAttribute("kreditrechnervorlage", kv);
      model.addAttribute("ergebnis",ke);
      model.addAttribute("activeLink", "kundeKreditRechner");
      return "kunde/kredit/rechner";
    }
  }

}



















