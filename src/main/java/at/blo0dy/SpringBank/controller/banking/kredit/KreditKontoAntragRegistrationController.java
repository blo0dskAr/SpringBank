package at.blo0dy.SpringBank.controller.banking.kredit;


import at.blo0dy.SpringBank.dao.KundeRepository;
import at.blo0dy.SpringBank.dao.konto.kredit.KreditKontoAntragRepository;
import at.blo0dy.SpringBank.dao.konto.kredit.KreditKontoRepository;
import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoRegistrationForm;
import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoRegistrationForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/kunde/banking/kredit")
public class KreditKontoAntragRegistrationController {

  private KreditKontoAntragRepository kreditKontoAntragRepository;
  private KundeRepository kundeRepository ;

  @Autowired
  public KreditKontoAntragRegistrationController(KreditKontoAntragRepository kreditKontoRepository, KundeRepository kundeRepository) {
    this.kreditKontoAntragRepository = kreditKontoRepository;
    this.kundeRepository = kundeRepository;
  }

  @GetMapping("/register")
  public String registerForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication , Model model) {
    log.debug("KreditRegisterForm aufgerufen für kunde/user: " + authentication.getName());

    String kundennummer = authentication.getName();
    model.addAttribute("kundennummer", kundennummer);

    return "/kunde/banking/kredit/registration";
}

  @PostMapping("/register")
  public String processRegistration(KreditKontoRegistrationForm form) {
    kreditKontoAntragRepository.save(form.toKreditKontoAntrag());
    return "redirect:/kunde/banking/index";
}


}
