package at.blo0dy.SpringBank.controller.mitarbeiter.crm;

import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.service.konto.KontoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/mitarbeiter/kunde/")
public class KontoSucheController {

  KontoService kontoService;

  @Autowired
  public KontoSucheController(KontoService kontoService) {
    this.kontoService = kontoService;
  }





  @GetMapping("/kontosuche")
  public String showKontoSucheForm(Model model) {

    Konto konto = new Konto();

    model.addAttribute("konto",konto);

    return "mitarbeiter/crm/kontosuche";
  }


/*  @PostMapping("/kontosuche")
  public String showKontoSucheFormWithErgebnis(@ModelAttribute Konto konto,  Model model) {

    System.out.println(konto);

//    Konto konto = kontoService.findByKontonummer(Long.valueOf(theSearchField));

//    model.addAttribute("suchErgebnis", konto);

    return "mitarbeiter/crm/kontosuche";
  }*/



@PostMapping("/kontosuche")
  public String showKonto(@ModelAttribute Konto konto,  Model model) {

  List<Konto> ergebnis = kontoService.findAll(konto);

  System.out.println("KONTO -....................--------->: " + konto);
  System.out.println("ERGEBNIS _-------------------------->: " + ergebnis);

  model.addAttribute("ergebnis", ergebnis);

  return "mitarbeiter/crm/kontosuche";
  }






}
