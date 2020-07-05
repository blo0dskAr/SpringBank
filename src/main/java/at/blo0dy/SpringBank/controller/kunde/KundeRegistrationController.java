package at.blo0dy.SpringBank.controller.kunde;


import at.blo0dy.SpringBank.dao.KundeRepository;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.model.person.kunde.KundeRegistrationForm;
import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/kunde/register")
public class KundeRegistrationController {

  private KundeService kundeService;
  private PasswordEncoder encoder;

  // TODO: gibt irgendwie autowiring probleme wenn ich versuch auf den globalen passwordencoder zuzugreifen
  // TODO: daher mal mit this.encoder = new BCryptPasswordEncoder(); gelöst.
  @Autowired
//  public KundeRegistrationController(KundeRepository kundeRepository, PasswordEncoder encoder) {
  public KundeRegistrationController(KundeService kundeService) {
    this.kundeService = kundeService;
    this.encoder = new BCryptPasswordEncoder();
  }

  @GetMapping
  public String showRegisterForm(Model model) {
    Kunde kunde = new Kunde();
    kunde.setId(0L);
    model.addAttribute("kunde", kunde);

    return "kunde/registration";
  }

  // TODO: DA geh ich direkt ins Repository, nicht in den service dazwischen, ich schätz das werd ich noch nachholen.. sollen ,.. müssten ... so quasi ..
  @PostMapping
  public String processRegistration(@Valid @ModelAttribute("kunde") KundeRegistrationForm form, Errors errors, Model model ) {
    log.debug("KundeRegistrationForm wird gespeichert");
    if (errors.hasErrors()) {
      model.addAttribute("kunde",form);
      return "kunde/registration";
    } else {
      log.debug("KundenForm wird gespeichert: ermittle Kundennummer");
      String newKundennummer = kundeService.getLatestKundennummerPlusOne().toString();
      log.debug("Neue Kundennummer lautet: " + newKundennummer + " .. KundenForm wird gespeichert");
      kundeService.save(form.toUser(encoder, kundeService.getLatestKundennummerPlusOne().toString()));
      // TODO: bei zeiten auf bestätigungsPage leiten, damit die Kundennummer mitgeteilt wird.
      return "redirect:/kunde/index";
    }
  }


}
