package at.blo0dy.SpringBank.controller.mitarbeiter;

import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.model.person.passwordForm.ChangePasswordForm;
import at.blo0dy.SpringBank.service.MitarbeiterService;
import at.blo0dy.SpringBank.service.bank.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/mitarbeiter")
public class MitarbeiterController {

  private MitarbeiterService mitarbeiterService;
  private BankService bankservice;

  @Autowired
  public MitarbeiterController(MitarbeiterService mitarbeiterService, BankService bankservice) {
    this.mitarbeiterService = mitarbeiterService;
    this.bankservice = bankservice;
  }

  @RequestMapping({"", "/", "/index"})
  public String getIndexPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

    String authName;
    Mitarbeiter loggedUser;

    if (authentication == null) {
      return "redirect:/mitarbeiter/index";
    } else {
      authName = authentication.getName();
      log.debug("Index Page von Mitarbeiter: " + authName + " aufgerufen.");
      loggedUser = mitarbeiterService.findByUserName(authName);
      model.addAttribute("loggedUser", loggedUser);
    }

    model.addAttribute("bank", bankservice.getBank());
    model.addAttribute("mitarbeiter",mitarbeiterService.findAll());
    model.addAttribute("mitarbeitercount",mitarbeiterService.count());
    model.addAttribute("activeLink", "MitarbeiterHome");

    return "mitarbeiter/index";
  }



  @GetMapping("/showChangePasswordForm")
  public String showChangePasswordForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

    log.debug("Passwort Ändern Page von Mitarbeiter: " + authentication.getName() + " aufgerufen.");
    ChangePasswordForm changePasswordForm = new ChangePasswordForm();
    model.addAttribute("changePasswordForm", changePasswordForm);
    model.addAttribute("activeLink", "changePasswordPage");

    return "mitarbeiter/changepassword-form";
  }



  @PostMapping("/saveChangePasswordForm")
  public String saveChangePasswordForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                       @Valid @ModelAttribute ChangePasswordForm changePasswordForm, BindingResult result,
                                       Model model, RedirectAttributes redirectAttrs) {

    String tmpUser = authentication.getName();
    Mitarbeiter tmpMitarbeiter = mitarbeiterService.findByUserName(tmpUser);
    final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    log.debug("Neues Passwort für " + authentication.getName() + " wird geprüft");
    if (!changePasswordForm.getConfirmPassword().equals(changePasswordForm.getNewPassword())) {
      result.rejectValue("confirmPassword", "error.changePasswordForm","Die neuen Passwörter stimmen nicht überrein");
      result.rejectValue("newPassword","error.changePasswordForm","Die neuen Passwörter stimmen nicht überein");
      log.debug("Neues Passwort für " + tmpUser + " stimmt mit ConfirmPassword nicht überrein ");
    }

    log.debug("Altes Passwort für " + tmpUser + " wird geprüft");
    if (!passwordEncoder.matches(changePasswordForm.getOldPassword(), tmpMitarbeiter.getPassword() )) {
      result.rejectValue("oldPassword", "error.changePasswordForm", "Ein Fehler ist aufgetreten");
      log.debug("Altes Passwort für " + tmpUser + " stimmt mit dem eingebenen Passwort nicht überrein ");
    }

    if (result.hasErrors()) {
      log.debug("Neues Passwort für " + tmpUser + " kann nicht geändert werden, Seite wird neu ausgeliefert");
      model.addAttribute("changePasswordForm", changePasswordForm);
      model.addAttribute("activeLink", "changePasswordPage");

      return "mitarbeiter/changepassword-form";
    } else {

      log.debug("Neues Passwort für " + tmpUser + " wird gespeichert");
      mitarbeiterService.updatePasswordByMitarbeiterId(tmpMitarbeiter.getId(), passwordEncoder.encode(changePasswordForm.getNewPassword()));
      log.debug("Neues Passwort für " + tmpUser + " wurde erfolgreich gespeichert");
      redirectAttrs.addFlashAttribute("passwordGeaendert", true);

      return "redirect:/mitarbeiter/index";
    }
  }




}
