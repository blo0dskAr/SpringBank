package at.blo0dy.SpringBank.controller.bank;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/kunde/bank")
public class BankController {

  @GetMapping("/philosophie")
  public String bankPhilosophieView(Model model) {
    log.debug("BankPhilosophieView wird geladen.");
    model.addAttribute("activeLink", "kundeBankPhilosophie");

    return "kunde/bank/philosophie";
  }


  @GetMapping("/karriere")
  public String bankKarriereView(Model model) {
    log.debug("BankKarriereView wird geladen.");
    model.addAttribute("activeLink", "kundeBankKarriere");

    return "kunde/bank/karriere";
  }


  @GetMapping("/kontakt")
  public String bankKontaktView(Model model) {
    log.debug("BankKontaktView wird geladen.");
    model.addAttribute("activeLink", "kundeBankKontakt");

    return "kunde/bank/kontakt";
  }


  @GetMapping("/impressum")
  public String bankImpressumView(Model model) {
    log.debug("BankImpressumView wird geladen.");
    model.addAttribute("activeLink", "kundeBankImpressum");

    return "kunde/bank/impressum";
  }

}
