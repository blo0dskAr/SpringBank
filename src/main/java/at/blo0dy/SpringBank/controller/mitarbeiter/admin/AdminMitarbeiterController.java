package at.blo0dy.SpringBank.controller.mitarbeiter.admin;


import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.service.MitarbeiterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/mitarbeiter/admin/mitarbeiterAdministration")
public class AdminMitarbeiterController {

  private MitarbeiterService mitarbeiterService;

  @Autowired
  public AdminMitarbeiterController(MitarbeiterService mitarbeiterService) {
    this.mitarbeiterService = mitarbeiterService;
  }

  @RequestMapping({"", "/", "/index"})
  public String getIndexPage(Model model) {

    List<Mitarbeiter> mitarbeiterListe = mitarbeiterService.findAll();

    model.addAttribute("mitarbeiter", mitarbeiterListe);
    model.addAttribute("activeLink", "AdminMAList");

    return "admin/list-mitarbeiter" ;

  }

  @GetMapping("/list")
  public String listMitarbeiter(Model theModel) {

    List<Mitarbeiter> mitarbeiterListe = mitarbeiterService.findAll();

    theModel.addAttribute("mitarbeiter", mitarbeiterListe);
    theModel.addAttribute("activeLink", "AdminMAList");

    return "admin/list-mitarbeiter";
  }

  @GetMapping("/delete")
  public String deleteMitarbeiter(@RequestParam("mitarbeiterId") Long theId, Model model) {
//    try {
      mitarbeiterService.deleteById(theId);
//    } catch (EmptyResultDataAccessException e) {
//      throw new UsernameNotFoundException("Mitarbeiter with id: " + theId + " not found.");
//    }
    return "redirect:/mitarbeiter/admin/mitarbeiterAdministration/list";
  }


  @PostMapping("/save")
  public String saveMitarbeiter(@Valid @ModelAttribute("mitarbeiter") Mitarbeiter mitarbeiter, Errors errors) {
    if (errors.hasErrors()) {
      return "admin/mitarbeiter-form";
    }  else {
      mitarbeiterService.save(mitarbeiter);
      return "redirect:/mitarbeiter/admin/mitarbeiterAdministration/list";
    }
  }

  @GetMapping("/showFormForAdd")
  public String showFormForAdd(Model theModel) {
    Mitarbeiter mitarbeiter = new Mitarbeiter();

    mitarbeiter.setId(0L);                      // kann angebl. zu probs kommen wenn ID != 0, so wirds fix aus der GenerationType geholt

    theModel.addAttribute("mitarbeiter", mitarbeiter);

    return "admin/mitarbeiter-form";
  }

  @GetMapping("/showFormForUpdate")
  public String showFormForUpdate(@RequestParam("mitarbeiterId") Long theId, Model theModel) {
    Mitarbeiter mitarbeiter = mitarbeiterService.findById(theId);

    theModel.addAttribute("mitarbeiter", mitarbeiter);

    return "admin/mitarbeiter-form";
  }

  @GetMapping("/search")
  public String searchMitarbeiter(@RequestParam("theSearchName") String theSearchName,
                                Model theModel) {

    // search customers from the service
    List<Mitarbeiter> mitarbeiterListe = mitarbeiterService.findMitarbeiterByVorAndNachName(theSearchName);

    // add the customers to the model
    theModel.addAttribute("mitarbeiter", mitarbeiterListe);

    return "redirect:/mitarbeiter/admin/mitarbeiterAdministration/list";
  }

}
