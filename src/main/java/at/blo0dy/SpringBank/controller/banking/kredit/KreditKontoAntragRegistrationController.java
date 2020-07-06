package at.blo0dy.SpringBank.controller.banking.kredit;


import at.blo0dy.SpringBank.dao.KundeRepository;
import at.blo0dy.SpringBank.dao.konto.kredit.KreditKontoAntragRepository;
import at.blo0dy.SpringBank.dao.konto.kredit.KreditKontoRepository;
import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoRegistrationForm;
import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoRegistrationForm;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.model.person.kunde.KundeRegistrationForm;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerVorlage;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditUtility;
import at.blo0dy.SpringBank.service.konto.kredit.KreditKontoAntragService;
import at.blo0dy.SpringBank.service.konto.kredit.KreditService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.cert.PKIXReason;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("/kunde/banking/kredit")
public class KreditKontoAntragRegistrationController {

  private KreditKontoAntragService kreditKontoAntragService;
  private KundeService kundeService ;
  private KreditService kreditService;

  @Autowired
  public KreditKontoAntragRegistrationController(KreditKontoAntragService kreditKontoAntragService, KundeService kundeService, KreditService kreditService) {
    this.kreditKontoAntragService = kreditKontoAntragService;
    this.kundeService = kundeService;
    this.kreditService = kreditService;
  }

  @GetMapping("/register")
  public String registerForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication , Model model) {
    log.debug("KreditRegisterForm aufgerufen für kunde/user: " + authentication.getName());


    KreditRechnerVorlage kv = new KreditRechnerVorlage(BigInteger.valueOf(84), kreditService.getZinssatz().divide(BigDecimal.valueOf(100)), BigDecimal.valueOf(10000));
    KreditRechnerErgebnis ke = new KreditRechnerErgebnis(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

    model.addAttribute("kreditrechnervorlage", kv);
    model.addAttribute("ergebnis", ke);

    String kundennummer = authentication.getName();
    model.addAttribute("kundennummer", kundennummer);

    return "kunde/banking/kredit/registration";
}

  @PostMapping(value = "/register", params={"calculate"})
  public String processRegistration(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                    @Validated @ModelAttribute("kreditrechnervorlage") KreditRechnerVorlage kv,
                                    BindingResult bindingResult, Model model) {
      if (bindingResult.hasErrors()) {
        kv.setZinssatz(kv.getZinssatz().divide(BigDecimal.valueOf(100)));
        model.addAttribute("ergebnis", new KreditRechnerErgebnis(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        return "kunde/banking/kredit/registration";
      }  else {
        KreditRechnerErgebnis ke = kreditService.getKreditRechnerErgebnis(kv);
        // Workaround bis dieses % wird als *100 dargestellt (aber nicht gerechnet) gelöst wird
        kv.setZinssatz(kv.getZinssatz().divide(BigDecimal.valueOf(100)));
        model.addAttribute("kreditrechnervorlage", kv);
        model.addAttribute("ergebnis",ke);
        model.addAttribute("calculatedCorrectly", true);

        log.debug("vorlage: " + kv);
        log.debug("ergebnis: " + ke);

        return "kunde/banking/kredit/registration";
      }
    }

  @PostMapping(value = "/register", params={"saveKreditAntrag"})
  public String tralala(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                        @Validated @ModelAttribute("kreditrechnervorlage") KreditRechnerVorlage kv, Errors errors1,
                        @Validated @ModelAttribute("ergebnis") KreditRechnerErgebnis ke,
                         Errors errors2, Model model) {

    if (errors1.hasErrors() || errors2.hasErrors()) {
      kv.setZinssatz(kv.getZinssatz().divide(BigDecimal.valueOf(100)));
      model.addAttribute("ergebnis", new KreditRechnerErgebnis(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
      return "kunde/banking/kredit/registration";
    }
    ke = kreditService.getKreditRechnerErgebnis(kv);

    String kundennummer = authentication.getName();
    model.addAttribute("kundennummer", kundennummer);

    log.debug("KE ------------------> " + ke);
    log.debug("KV ------------------> " + kv);

    log.debug("KreditAntrag soll gespeichert werden für Kunde: " + kundennummer);
    KreditKontoRegistrationForm kreditKontoRegistrationForm = new KreditKontoRegistrationForm(LocalDateTime.now(), kv.getKreditBetrag(), ke.getMonatlicheRate(), kv.getLaufzeit(), kv.getZinssatz(), ke.getGesamtBelastung(), Long.valueOf(kundennummer) );
    log.debug("KreditKontoRegistrationForm wurde erstellt: " + kreditKontoRegistrationForm.toString());
    kreditKontoAntragService.save(kreditKontoRegistrationForm.toKreditKontoAntrag());
    log.debug("Kreditantrag wurde erfolgreich gespeichert.");


    return "kunde/banking/kredit/registration";
  }
}
