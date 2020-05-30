package at.blo0dy.SpringBank.controller;


import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.service.MitarbeiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
