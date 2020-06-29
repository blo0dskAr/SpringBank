package at.blo0dy.SpringBank.controller.kunde.sparen;


import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerVorlage;
import at.blo0dy.SpringBank.model.produkt.sparen.SparenUtility;
import at.blo0dy.SpringBank.service.kunde.sparen.SparService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.LocalDate;

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

    SparZinsRechnerVorlage sv = new SparZinsRechnerVorlage(LocalDate.now(), SparenUtility.getZinssatz(),5000D);
    SparZinsRechnerErgebnis se = new SparZinsRechnerErgebnis(0,0,0);

    model.addAttribute("zinssatz", sparService.getZinssatz());
    model.addAttribute("betrag", 5000);
    model.addAttribute("datum", LocalDate.now());
    model.addAttribute("ergebnis",se);
    model.addAttribute("sparzinsrechnervorlage", sv);

    return "kunde/sparen/rechner";
  }

  @PostMapping("/rechner")
  public String berechneSparZinsen(@ModelAttribute("sparzinsrechnervorlage") SparZinsRechnerVorlage sv, Model model, Errors errors) {
    if (errors.hasErrors()) {
      return "admin/mitarbeiter-form";
    }  else {
      SparZinsRechnerErgebnis se = sparService.getSparZinsRechnerEregebnis(sv);

      model.addAttribute("zinssatz", sparService.getZinssatz());
      model.addAttribute("betrag", 5000);
      model.addAttribute("datum", LocalDate.now());
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

}
