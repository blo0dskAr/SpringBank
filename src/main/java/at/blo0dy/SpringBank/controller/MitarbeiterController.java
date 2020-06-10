package at.blo0dy.SpringBank.controller;


import at.blo0dy.SpringBank.model.person.adresse.Adresse;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.service.MitarbeiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/mitarbeiter")
public class MitarbeiterController {

  private MitarbeiterService mitarbeiterService;

  @Autowired
  public MitarbeiterController(MitarbeiterService mitarbeiterService) {
    this.mitarbeiterService = mitarbeiterService;
  }

  @GetMapping("/list")
  public String listMitarbeiter(Model theModel) {

    List<Mitarbeiter> mitarbeiterListe = mitarbeiterService.findAll();

    theModel.addAttribute("mitarbeiter", mitarbeiterListe);

    return "mitarbeiter/list-mitarbeiter";
  }

  @GetMapping("/delete")
  public String deleteMitarbeiter(@RequestParam("mitarbeiterId") Long theId) {
    mitarbeiterService.deleteById(theId);

    return "redirect:/mitarbeiter/list";
  }

  @PostMapping("/save")
  public String saveMitarbeiter(@Valid @ModelAttribute("mitarbeiter") Mitarbeiter mitarbeiter, Errors errors) {
    if (errors.hasErrors()) {
      System.out.println(errors.hasErrors());
      System.out.println(errors.getAllErrors());
      return "mitarbeiter/mitarbeiter-form";
    }  else {
      System.out.println(errors.hasErrors());
      System.out.println(errors.getAllErrors());
      mitarbeiterService.save(mitarbeiter);
      return "redirect:/mitarbeiter/list";
    }
  }

  @GetMapping("/showFormForAdd")
  public String showFormForAdd(Model theModel) {
    Mitarbeiter mitarbeiter = new Mitarbeiter();

    mitarbeiter.setId(0L);

    theModel.addAttribute("mitarbeiter", mitarbeiter);

    return "mitarbeiter/mitarbeiter-form";
  }

  @GetMapping("/showFormForUpdate")
  public String showFormForUpdate(@RequestParam("mitarbeiterId") Long theId, Model theModel) {
    Mitarbeiter mitarbeiter = mitarbeiterService.findById(theId);

    theModel.addAttribute("mitarbeiter", mitarbeiter);

    return "mitarbeiter/mitarbeiter-form";
  }

  @GetMapping("/search")
  public String searchMitarbeiter(@RequestParam("theSearchName") String theSearchName,
                                Model theModel) {

    // search customers from the service
    List<Mitarbeiter> mitarbeiterListe = mitarbeiterService.findMitarbeiterByVorAndNachName(theSearchName);

    // add the customers to the model
    theModel.addAttribute("mitarbeiter", mitarbeiterListe);

    return "redirect:/mitarbeiter/list";
  }

}
