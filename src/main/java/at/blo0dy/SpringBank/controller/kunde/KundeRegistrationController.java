package at.blo0dy.SpringBank.controller.kunde;


import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.model.person.kunde.KundeRegistrationForm;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/kunde/register")
public class KundeRegistrationController {

  private KundeService kundeService;
  private PasswordEncoder encoder;

  // TODO: gibt irgendwie autowiring probleme wenn ich versuch auf den globalen passwordencoder zuzugreifen
  // TODO: daher mal mit this.encoder = new BCryptPasswordEncoder(); gelöst. - reicht wegen eh default..
  @Autowired
//  public KundeRegistrationController(KundeRepository kundeRepository, PasswordEncoder encoder) {
  public KundeRegistrationController(KundeService kundeService) {
    this.kundeService = kundeService;
    this.encoder = new BCryptPasswordEncoder();
  }

  @GetMapping
  public String showRegisterForm(Model model) {
    log.debug("KundenRegistrierungsForm wird aufgerufen.");
    Kunde kunde = new Kunde();
    kunde.setId(0L);
    model.addAttribute("kunde", kunde);

    return "kunde/registration";
  }

  @PostMapping
  public String processRegistration(@Valid @ModelAttribute("kunde") KundeRegistrationForm form, BindingResult result, Model model, RedirectAttributes redirectAttrs) {
    log.debug("KundeRegistrationForm wird gespeichert");
    if (result.hasErrors()) {
      log.debug("KundeRegistrationController: Fehler beim speichern von Kunde erhalten. Anzahl:" + result.getErrorCount());
      log.warn("Errors: " + result.getAllErrors());
      model.addAttribute("kunde",form);
      return "kunde/registration";
    } else {
      log.debug("Neue KundeForm soll gespeichert werden: ermittle Kundennummer");
      String newKundennummer = kundeService.getLatestKundennummerPlusOne().toString();
      log.debug("Neue Kundennummer lautet: " + newKundennummer + " .. KundenForm wird gespeichert");
      kundeService.save(form.toUser(encoder, newKundennummer));

      Kunde neuerKunde = kundeService.findByKundennummer(newKundennummer);
      model.addAttribute("kunde", neuerKunde);

      return "kunde/registrationConfirmation";
    }
  }


}
