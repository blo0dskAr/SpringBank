package at.blo0dy.SpringBank.controller.mitarbeiter.admin;


import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.service.MitarbeiterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
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
  public String getIndexPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {
    String loginName = authentication.getName();
    log.debug("AdminIndex Page wird von Mitarbeiter: " + loginName +  " aufgerufen");

    List<Mitarbeiter> mitarbeiterListe = mitarbeiterService.findAll();

    model.addAttribute("mitarbeiter", mitarbeiterListe);
    model.addAttribute("activeLink", "AdminMAList");

    return "admin/list-mitarbeiter" ;

  }

  @GetMapping("/list")
  public String listMitarbeiter(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model theModel) {
    String loginName = authentication.getName();
    log.debug("ListMitarbeiterPage Page wird von Mitarbeiter: " + loginName +  " aufgerufen");

    List<Mitarbeiter> mitarbeiterListe = mitarbeiterService.findAll();

    theModel.addAttribute("mitarbeiter", mitarbeiterListe);
    theModel.addAttribute("activeLink", "AdminMAList");

    return "admin/list-mitarbeiter";
  }

  @GetMapping("/delete")
  public String deleteMitarbeiter(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                  @RequestParam("mitarbeiterId") Long theId, Model model) {
    String loginName = authentication.getName();
    log.debug("Löschung von Mitarbeiter: " + theId + "wird von Mitarbeiter: " + loginName +  " angefordert.");

      mitarbeiterService.deleteById(theId);
    return "redirect:/mitarbeiter/admin/mitarbeiterAdministration/list";
  }


  @PostMapping("/save")
  public String saveMitarbeiter(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                @Valid @ModelAttribute("mitarbeiter") Mitarbeiter mitarbeiter, Errors errors) {
    String loginName = authentication.getName();
    log.debug("Speichern eines Mitarbeiters wird von Mitarbeiter: " + loginName +  " angefordert.");

    if (mitarbeiter.getMitarbeiterNummer().equals("")) {
      mitarbeiter.setMitarbeiterNummer(mitarbeiterService.getLatestMitarbeiterNummerPlusOne());
    }

    if (errors.hasErrors()) {
      return "admin/mitarbeiter-form";
    }  else {
      mitarbeiterService.save(mitarbeiter);
      return "redirect:/mitarbeiter/admin/mitarbeiterAdministration/list";
    }
  }

  @GetMapping("/showFormForAdd")
  public String showFormForAdd(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model theModel) {

    String loginName = authentication.getName();
    log.debug("MitarbeiterAddingForm wird von Mitarbeiter: " + loginName + " aufgerufen.");

    Mitarbeiter mitarbeiter = new Mitarbeiter();
    mitarbeiter.setId(0L);                      // kann angebl. zu probs kommen wenn ID != 0, so wirds fix aus der GenerationType geholt
    theModel.addAttribute("mitarbeiter", mitarbeiter);

    return "admin/mitarbeiter-form";
  }

  @GetMapping("/showFormForUpdate")
  public String showFormForUpdate(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                  @RequestParam("mitarbeiterId") Long theId, Model theModel) {
    String loginName = authentication.getName();
    log.debug("MitarbeiterUpdateForm wird von Mitarbeiter: " + loginName + " aufgerufen");

    Mitarbeiter mitarbeiter = mitarbeiterService.findById(theId);
    theModel.addAttribute("mitarbeiter", mitarbeiter);
    return "admin/mitarbeiter-form";
  }

  @GetMapping("/search")
  public String searchMitarbeiter(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                  @RequestParam("theSearchName") String theSearchName, Model theModel) {
    String loginName = authentication.getName();
    log.debug("MitarbeitierSuche wird von Mitarbeiter: " + loginName + " angefordert");

    List<Mitarbeiter> mitarbeiterListe = mitarbeiterService.findMitarbeiterByVorAndNachName(theSearchName);

    theModel.addAttribute("mitarbeiter", mitarbeiterListe);

    return "redirect:/mitarbeiter/admin/mitarbeiterAdministration/list";
  }

}
