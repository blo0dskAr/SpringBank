package at.blo0dy.SpringBank.controller;

import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.model.person.rolle.Rolle;
import at.blo0dy.SpringBank.service.MitarbeiterService;
import at.blo0dy.SpringBank.service.rolle.RolleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/mitarbeiter/admin/rollen")
public class AdminRolleController {

  RolleService rolleService;
  MitarbeiterService mitarbeiterService;

  @Autowired
  public AdminRolleController(RolleService rolleService, MitarbeiterService mitarbeiterService) {
    this.rolleService = rolleService;
    this.mitarbeiterService = mitarbeiterService;
  }

  @GetMapping({"/list", "/", "", "/index"})
  public String listRollen(Model model) {

    List<Rolle> rollenListe = rolleService.findAll() ;

    model.addAttribute("rollen", rollenListe) ;
    model.addAttribute("activeLink", "AdminRolleList");

    System.out.println(model.getAttribute("rollen"));
    System.out.println(model.getAttribute("constrainterror"));

    return "rolle/list-rollen";
  }

  // Todo ExceptionHandler checken und basteln.
  @GetMapping("/delete")
  public String deleteRoleById(@Validated @RequestParam("rolleId") Long theId, Model model) {
    try {
      rolleService.deleteById(theId);
    } catch (DataIntegrityViolationException ex) {
      ex.printStackTrace();
      log.error("ConstraintVerletzung beim löschen einer Rolle vorgefallen");
      model.addAttribute("constrainterror", ex.getRootCause());

      List<Rolle> rollenListe = rolleService.findAll() ;
      model.addAttribute("rollen", rollenListe) ;
      model.addAttribute("activeLink", "AdminRolleList");

      return "rolle/list-rollen";

    }
    return "redirect:/mitarbeiter/admin/rollen/index";
  }


  @PostMapping("/save")
  public String saveRolle(@Valid @ModelAttribute("rolle") Rolle rolle, Errors errors) {
    if (errors.hasErrors()) {
      return "rolle/rollen-form";
    }  else {
      rolleService.save(rolle);
      return "redirect:/mitarbeiter/admin/rollen/index";
    }
  }

  @GetMapping("/showFormForAdd")
  public String showFormForAdd(Model model) {

    Rolle rolle = new Rolle();

    rolle.setId(0L);

    model.addAttribute("rolle", rolle);

    return "rolle/rollen-form";
  }

  @GetMapping("/showFormForUpdate")
  public String showFormForUpdate(@RequestParam("rolleId") Long theId, Model theModel) {
    Rolle rolle = rolleService.findById(theId);

    theModel.addAttribute("rolle", rolle);

    return "rolle/rollen-form";
  }


  @GetMapping({"/showRolleDetail{rolleId}"})
  public String listRolleDetail(@Validated @RequestParam("rolleId") Long theRoleId, Model model) {

    List<Mitarbeiter> mitarbeiterListe = rolleService.findMitarbeiterIdsByRoleId(theRoleId) ;

    model.addAttribute("rolleId", theRoleId);
    model.addAttribute("mitarbeiter", mitarbeiterListe) ;

    return "rolle/rolledetail";
  }


  @GetMapping("/removeRoleFromUser{rolleId}{mitarbeiterId}")
  public String removeRoleFromUser(@Validated @RequestParam("rolleId") Long theId,
                                   @Validated @RequestParam("mitarbeiterId") Long theMitarbeiterId, Model model) {
    rolleService.removeRoleFromUser(theId, theMitarbeiterId);

    List<Mitarbeiter> mitarbeiterListe = rolleService.findMitarbeiterIdsByRoleId(theId) ;
    model.addAttribute("rolleId", theId);
    model.addAttribute("mitarbeiter", mitarbeiterListe) ;

    return "rolle/rolledetail";
  }



  @GetMapping("/addRoleToUserPage")
  public String addRoleToUserPage(@Validated @RequestParam("rolleId") Long theRoleId, Model model) {
    List<Mitarbeiter> mitarbeiterListe = rolleService.findMitarbeiterIdsByRoleIdExeptExisting(theRoleId);
    model.addAttribute("rolleId", theRoleId);
    model.addAttribute("mitarbeiter", mitarbeiterListe);

    return "rolle/add-user" ;
  }

  @GetMapping("/addRoleToUser")
  public String addRoleToUser(@Validated @RequestParam("rolleId") Long theId,
                              @Validated @RequestParam("mitarbeiterId") Long theMitarbeiterId, Model model) {

    rolleService.addRoleToUser(theId, theMitarbeiterId);

    List<Mitarbeiter> mitarbeiterListe = rolleService.findMitarbeiterIdsByRoleId(theId) ;
    model.addAttribute("rolleId", theId);
    model.addAttribute("mitarbeiter", mitarbeiterListe) ;

    return "rolle/rolledetail";
  }
}
