package at.blo0dy.SpringBank.controller.bank;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kunde/bank")
public class BankController {

  @GetMapping("/philosophie")
  public String bankPhilosophieView(Model model) {
    model.addAttribute("activeLink", "kundeBankPhilosophie");

    return "kunde/bank/philosophie";
  }


  @GetMapping("/karriere")
  public String bankKarriereView(Model model) {
    model.addAttribute("activeLink", "kundeBankKarriere");

    return "kunde/bank/karriere";
  }


  @GetMapping("/kontakt")
  public String bankKontaktView(Model model) {
    model.addAttribute("activeLink", "kundeBankKontakt");

    return "kunde/bank/kontakt";
  }


  @GetMapping("/impressum")
  public String bankImpressumView(Model model) {
    model.addAttribute("activeLink", "kundeBankImpressum");

    return "kunde/bank/impressum";
  }

}
