package at.blo0dy.SpringBank.controller.kunde;


import at.blo0dy.SpringBank.dao.KundeRepository;
import at.blo0dy.SpringBank.security.KundeRegistrationForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kunde/register")
public class KundeRegistrationController {

  private KundeRepository kundeRepository;
  private PasswordEncoder passwordEncoder;

  public KundeRegistrationController(KundeRepository kundeRepository, PasswordEncoder passwordEncoder) {
    this.kundeRepository = kundeRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @GetMapping
  public String registerForm() {
    return "kunde/registration";
  }

  @PostMapping
  public String processRegistration(KundeRegistrationForm form) {
    kundeRepository.save(form.toUser(passwordEncoder));
    return "redirect:/kunde/index";
  }


}
