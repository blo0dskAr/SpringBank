package at.blo0dy.SpringBank.controller.banking.sparen;


import at.blo0dy.SpringBank.dao.KundeRepository;
import at.blo0dy.SpringBank.dao.konto.sparen.SparKontoAntragRepository;
import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoRegistrationForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Controller
@RequestMapping("/kunde/banking/sparen")
public class SparKontoAntragRegistrationController {

  private SparKontoAntragRepository sparKontoAntragRepository;
  private KundeRepository kundeRepository ;

  @Autowired
  public SparKontoAntragRegistrationController(SparKontoAntragRepository sparKontoAntragRepository, KundeRepository kundeRepository) {
    this.sparKontoAntragRepository = sparKontoAntragRepository;
    this.kundeRepository = kundeRepository;
  }

  @GetMapping("/register")
  public String registerForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication , Model model) {

    SparKontoRegistrationForm sparKontoRegistrationForm = new SparKontoRegistrationForm();

    // TODO: authentication.getname als name mitschicken sollt ich mal als controller advice für authentifizierte KundenPages definieren
    String kundennummer = authentication.getName();
    log.debug("SparKontoAntragRegistrationController: Kunde " + kundennummer + " ruft die SparKontoRegistrierungsSeite auf" );
    model.addAttribute("kundennummer", kundennummer);
    model.addAttribute("sparkontoantrag", sparKontoRegistrationForm);

    return "/kunde/banking/sparen/registration";
  }

  @PostMapping("/register")
  public String processRegistration(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                    @Valid @ModelAttribute("sparkontoantrag") SparKontoRegistrationForm form, BindingResult result, Model model) {

    if (result.hasErrors()) {
      model.addAttribute("kundennummer", authentication.getName());
      return "/kunde/banking/sparen/registration";
    }

    // AntragsDatum generieren und für die Eingabe optimieren.
    // TODO: guggen wie man das global aktiviert, die bisherigen versuche waren eher mäßig..
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    form.setAntragsDatum(LocalDateTime.parse(LocalDateTime.now().format(formatter)));

    log.debug("SparKontoAntragRegistrationController: SparKontoRegistrationForm erhalten: " + form);
    log.debug("SparKontoAntragRegistrationController: SparKontoRegistrationForm wird als SparkontoAntrag gespeichert");
    sparKontoAntragRepository.save(form.toSparKontoAntrag());
    log.debug("SparKontoAntragRegistrationController: SparKontoRegistrationForm wurde erfolgreich als SparkontoAntrag gespeichert");
    return "redirect:/kunde/banking/index";
  }





}
