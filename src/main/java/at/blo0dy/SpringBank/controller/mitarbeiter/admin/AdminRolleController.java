package at.blo0dy.SpringBank.controller.mitarbeiter.admin;

import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.model.person.rolle.Rolle;
import at.blo0dy.SpringBank.service.MitarbeiterService;
import at.blo0dy.SpringBank.service.rolle.RolleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
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
  public String listRollen(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

    String loginName = authentication.getName();
    log.debug("AdminRollenIndexPage wird von Mitarbeiter: " + loginName + " aufgerufen.");
    List<Rolle> rollenListe = rolleService.findAll() ;

    model.addAttribute("rollen", rollenListe) ;
    model.addAttribute("activeLink", "AdminRolleList");

    return "rolle/list-rollen";
  }

  // Todo ExceptionHandler checken und basteln.
  @GetMapping("/delete")
  public String deleteRoleById(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                               @Validated @RequestParam("rolleId") Long theId, Model model) {
    String loginName = authentication.getName();
    log.debug("Löschung von RolleId: " + theId + " wird von Mitarbeiter: " + loginName + " angefordert..");
    try {
      rolleService.deleteById(theId);
    } catch (DataIntegrityViolationException ex) {
     // ex.printStackTrace();
      log.error("ConstraintVerletzung beim löschen einer Rolle(id = " + theId + " vorgefallen");
      model.addAttribute("constrainterror", ex.getRootCause());

      List<Rolle> rollenListe = rolleService.findAll() ;
      model.addAttribute("rollen", rollenListe) ;
      model.addAttribute("activeLink", "AdminRolleList");

      return "rolle/list-rollen";

    }
    return "redirect:/mitarbeiter/admin/rollen/index";
  }


  @PostMapping("/save")
  public String saveRolle(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                          @Valid @ModelAttribute("rolle") Rolle rolle, Errors errors) {
    String loginName = authentication.getName();
    log.debug("Rolle speichern wird von Mitarbeiter: " + loginName + " angefordert.");
    if (errors.hasErrors()) {
      log.error("Fehler beim Speichern einer Rolle erhalten. Seite wird neu geladen.");
      return "rolle/rollen-form";
    }  else {
      rolleService.save(rolle);
      return "redirect:/mitarbeiter/admin/rollen/index";
    }
  }

  @GetMapping("/showFormForAdd")
  public String showFormForAdd(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {
    String loginName = authentication.getName();
    log.debug("RolleAddForm wird von Mitarbeiter: " + loginName + " aufgerufen.");

    Rolle rolle = new Rolle();
    rolle.setId(0L);
    model.addAttribute("rolle", rolle);
    model.addAttribute("activeLink", "AdminRolleList");

    return "rolle/rollen-form";
  }

  @GetMapping("/showFormForUpdate")
  public String showFormForUpdate(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                  @RequestParam("rolleId") Long theId, Model model) {
    String loginName = authentication.getName();
    log.debug("RolleUpdateForm wird von Mitarbeiter: " + loginName + " aufgerufen.");

    Rolle rolle = rolleService.findById(theId);
    model.addAttribute("rolle", rolle);
    model.addAttribute("activeLink", "AdminRolleList");

    return "rolle/rollen-form";
  }


  @GetMapping({"/showRolleDetail{rolleId}"})
  public String listRolleDetail(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                @Validated @RequestParam("rolleId") Long theRoleId, Model model) {
    String loginName = authentication.getName();
    log.debug("RolleDetail für Rolle: " + theRoleId + " wird von Mitarbeiter: " + loginName + " aufgerufen.");

    List<Mitarbeiter> mitarbeiterListe = rolleService.findMitarbeiterIdsByRoleId(theRoleId) ;
    Rolle rolle = rolleService.findById(theRoleId);

    model.addAttribute("rolleId", theRoleId);
    model.addAttribute("rolle",rolle);
    model.addAttribute("mitarbeiter", mitarbeiterListe) ;
    model.addAttribute("activeLink", "AdminRolleList");

    return "rolle/rolledetail";
  }


  @GetMapping("/removeRoleFromUser{rolleId}{mitarbeiterId}")
  public String removeRoleFromUser(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                   @Validated @RequestParam("rolleId") Long theId,
                                   @Validated @RequestParam("mitarbeiterId") Long theMitarbeiterId, Model model) {
    String loginName = authentication.getName();
    log.debug("Löschung RolleId: " + theId + " bei MitarbeiterId: " + theMitarbeiterId + " wird von Mitarbeiter: " + loginName + " angefordert.");

    rolleService.removeRoleFromUser(theId, theMitarbeiterId);

    Rolle rolle = rolleService.findById(theId);
    List<Mitarbeiter> mitarbeiterListe = rolleService.findMitarbeiterIdsByRoleId(theId) ;
    model.addAttribute("rolleId", theId);
    model.addAttribute("rolle",rolle);
    model.addAttribute("mitarbeiter", mitarbeiterListe) ;

    return "rolle/rolledetail";
  }



  @GetMapping("/addRoleToUserPage")
  public String addRoleToUserPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                  @Validated @RequestParam("rolleId") Long theRoleId, Model model) {
    String loginName = authentication.getName();
    log.debug("Hinzufügen einer Rolle-Page für Rolle: " + theRoleId + " wird von Mitarbeiter: " + loginName + " angefordert.");

    List<Mitarbeiter> mitarbeiterListe = rolleService.findMitarbeiterIdsByRoleIdExeptExisting(theRoleId);
    Rolle rolle = rolleService.findById(theRoleId);

    model.addAttribute("rolleId", theRoleId);
    model.addAttribute("mitarbeiter", mitarbeiterListe);
    model.addAttribute("rolle",rolle);

    return "rolle/add-user" ;
  }

  @GetMapping("/addRoleToUser")
  public String addRoleToUser(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                              @Validated @RequestParam("rolleId") Long theId,
                              @Validated @RequestParam("mitarbeiterId") Long theMitarbeiterId, Model model) {
    String loginName = authentication.getName();
    log.debug("Hinzufügen RolleId: " + theId + " bei MitarbeiterId: " + theMitarbeiterId + " wird von Mitarbeiter: " + loginName + " angefordert.");

    rolleService.addRoleToUser(theId, theMitarbeiterId);

    List<Mitarbeiter> mitarbeiterListe = rolleService.findMitarbeiterIdsByRoleIdExeptExisting(theId);
    Rolle rolle = rolleService.findById(theId);
    model.addAttribute("rolleId", theId);
    model.addAttribute("rolle",rolle);
    model.addAttribute("mitarbeiter", mitarbeiterListe) ;
    model.addAttribute("activeLink", "AdminRolleList");

    return "rolle/add-user";
  }
}
