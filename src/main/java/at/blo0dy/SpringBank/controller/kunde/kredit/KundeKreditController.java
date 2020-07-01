package at.blo0dy.SpringBank.controller.kunde.kredit;


import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerVorlage;
import at.blo0dy.SpringBank.service.kunde.kredit.KreditService;
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

@Controller
@RequestMapping("/kunde/kredit")
public class KundeKreditController {

  KreditService kreditService;

  @Autowired
  public KundeKreditController(KreditService kreditService) {
    this.kreditService = kreditService;
  }

  @GetMapping({"/index", "/", ""})
  public String viewKreditIndex() {

    return "kunde/kredit/index";
  }

  @GetMapping("/eroeffnung")
  public String viewKreditEroeffnung() {

    return "kunde/banking/kredit/eroeffnungsForm";
  }

  @GetMapping("/rechner")
  public String showKreditRechnerForm(Model model) {

    BigDecimal anfangsWerte = BigDecimal.ZERO;

    // / 100 division workaround fürs frontend
    KreditRechnerVorlage kv = new KreditRechnerVorlage(BigInteger.valueOf(84), kreditService.getZinssatz().divide(BigDecimal.valueOf(100)), BigDecimal.valueOf(10000));
    KreditRechnerErgebnis ke = new KreditRechnerErgebnis(anfangsWerte, anfangsWerte, anfangsWerte, anfangsWerte);

    model.addAttribute("kreditrechnervorlage", kv);
    model.addAttribute("ergebnis", ke);

    return "kunde/kredit/rechner";
  }

  @PostMapping("/rechner")
  public String berechneKredit(@Validated @ModelAttribute("kreditrechnervorlage") KreditRechnerVorlage kv, BindingResult result, Model model ) {
    if (result.hasErrors()) {
      kv.setZinssatz(kv.getZinssatz().divide(BigDecimal.valueOf(100)));
      return "kunde/kredit/rechner";
    }  else {
      KreditRechnerErgebnis ke = kreditService.getKreditRechnerErgebnis(kv);
      // Workaround bis dieses % wird als *100 dargestellt (aber nicht gerechnet) gelöst wird
      kv.setZinssatz(kv.getZinssatz().divide(BigDecimal.valueOf(100)));
      model.addAttribute("kreditrechnervorlage", kv);
      model.addAttribute("ergebnis",ke);
      return "kunde/kredit/rechner";
    }
  }

}
