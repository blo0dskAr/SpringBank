package at.blo0dy.SpringBank.controller.kunde.sparen;


import at.blo0dy.SpringBank.model.produkt.sparen.AdvancedSparZinsRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.sparen.AdvancedSparZinsRechnerVorlage;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerVorlage;
import at.blo0dy.SpringBank.service.kunde.sparen.SparService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/kunde/sparen")
public class KundeSparenController {

  SparService sparService;

  @Autowired
  public KundeSparenController(SparService sparService) {
    this.sparService = sparService;
  }

  @GetMapping({"/index", "/", ""})
  public String viewSparenIndex() {

    return "kunde/sparen/index";
  }

  @GetMapping("/rechner")
  public String viewSparenRechner(Model model) {

    // / 100 division workaround fürs frontend
    SparZinsRechnerVorlage sv = new SparZinsRechnerVorlage(LocalDate.now(), sparService.getZinssatz()/100,5000D, 0);
    SparZinsRechnerErgebnis se = new SparZinsRechnerErgebnis(0.00,0.00,0.00,0.00, 0.00);

//    model.addAttribute("zinssatz", sparService.getZinssatz());
//    model.addAttribute("betrag", 5000);
//    model.addAttribute("datum", LocalDate.now());
    model.addAttribute("ergebnis",se);
//    sv.setZinssatz(sv.getZinssatz()/100);
    model.addAttribute("sparzinsrechnervorlage", sv);

    return "kunde/sparen/rechner";
  }

  @PostMapping("/rechner")
  public String berechneSparZinsen(@ModelAttribute("sparzinsrechnervorlage") SparZinsRechnerVorlage sv, Model model, Errors errors) {
    if (errors.hasErrors()) {
      return "admin/mitarbeiter-form";
    }  else {
      SparZinsRechnerErgebnis se = sparService.getSparZinsRechnerEregebnis(sv);

//      model.addAttribute("zinssatz", sparService.getZinssatz());
//      model.addAttribute("betrag", 5000);
//      model.addAttribute("datum", LocalDate.now());
//      sv.setZinssatz(sv.getZinssatz()/100);
      // Workaround bis dieses % wird als *100 dargestellt (aber nicht gerechnet) gelöst wird
        sv.setZinssatz(sv.getZinssatz()/100);
      model.addAttribute("sparzinsrechnervorlage", sv);
      model.addAttribute("ergebnis",se);
      return "kunde/sparen/rechner";
    }
  }


/*  @PostMapping("/save")
  public String saveMitarbeiter(@Valid @ModelAttribute("mitarbeiter") Mitarbeiter mitarbeiter, Errors errors) {
    if (errors.hasErrors()) {
      return "admin/mitarbeiter-form";
    }  else {
      mitarbeiterService.save(mitarbeiter);
      return "redirect:/mitarbeiter/admin/mitarbeiterAdministration/list";
    }
  }*/



  @GetMapping("/rechner2")
  public String viewSparenRechner2(Model model) {

    BigDecimal anfangsWerte = BigDecimal.ZERO;

    // / 100 division workaround fürs frontend
    AdvancedSparZinsRechnerVorlage sv = new AdvancedSparZinsRechnerVorlage(0, sparService.getZinssatz() / 100, 5000D,0, 0);
    AdvancedSparZinsRechnerErgebnis se = new AdvancedSparZinsRechnerErgebnis(anfangsWerte, anfangsWerte, anfangsWerte, anfangsWerte, anfangsWerte);

//    model.addAttribute("zinssatz", sparService.getZinssatz());
//    model.addAttribute("betrag", 5000);
//    model.addAttribute("datum", LocalDate.now());
    model.addAttribute("ergebnis", se);
//    sv.setZinssatz(sv.getZinssatz()/100);
    model.addAttribute("sparzinsrechnervorlage", sv);

    return "kunde/sparen/rechner2";
  }

  @PostMapping("/rechner2")
  public String berechneSparZinsen2(@ModelAttribute("sparzinsrechnervorlage") AdvancedSparZinsRechnerVorlage sv, Model model, Errors errors) {
    if (errors.hasErrors()) {
      return "admin/mitarbeiter-form";
    }  else {
      List<AdvancedSparZinsRechnerErgebnis> se = sparService.getAdvancedSparZinsRechnerErgebnis(sv);

//      model.addAttribute("zinssatz", sparService.getZinssatz());
//      model.addAttribute("betrag", 5000);
//      model.addAttribute("datum", LocalDate.now());
//      sv.setZinssatz(sv.getZinssatz()/100);
      // Workaround bis dieses % wird als *100 dargestellt (aber nicht gerechnet) gelöst wird
      sv.setZinssatz(sv.getZinssatz()/100);
      model.addAttribute("sparzinsrechnervorlage", sv);
      model.addAttribute("ergebnis",se);
      return "kunde/sparen/rechner2";
    }
  }

}
