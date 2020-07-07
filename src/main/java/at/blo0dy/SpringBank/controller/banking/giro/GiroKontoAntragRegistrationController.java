package at.blo0dy.SpringBank.controller.banking.giro;


import at.blo0dy.SpringBank.dao.KundeRepository;
import at.blo0dy.SpringBank.dao.konto.giro.GiroKontoAntragRepository;
import at.blo0dy.SpringBank.dao.konto.sparen.SparKontoAntragRepository;
import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoRegistrationForm;
import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoRegistrationForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/kunde/banking/giro")
public class GiroKontoAntragRegistrationController {

  private GiroKontoAntragRepository giroKontoAntragRepository;
  private KundeRepository kundeRepository ;

  @Autowired
  public GiroKontoAntragRegistrationController(GiroKontoAntragRepository giroKontoAntragRepository, KundeRepository kundeRepository) {
    this.giroKontoAntragRepository = giroKontoAntragRepository;
    this.kundeRepository = kundeRepository;
  }


  @GetMapping("/register")
  public String registerForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication , Model model) {

    String kundennummer = authentication.getName();
    model.addAttribute("kundennummer", kundennummer);

    GiroKontoAntrag giroKontoAntrag = new GiroKontoAntrag();

    model.addAttribute("girokontoantrag", giroKontoAntrag);

    return "/kunde/banking/giro/registration";
  }

  @PostMapping("/register")
  public String processRegistration(@ModelAttribute("girokontoantrag") GiroKontoRegistrationForm form) {
    log.debug("GIROFORM -------------> " + form);
    log.debug("GiroKontoAntragRegistrationController: GiroKontoRegistrationForm wird als GirokontoAntrag gespeichert");
    giroKontoAntragRepository.save(form.toGiroKontoAntrag());
    return "redirect:/kunde/banking/index";
  }

}
