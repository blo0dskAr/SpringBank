package at.blo0dy.SpringBank.controller.bank;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kunde/bank")
public class BankController {

  @GetMapping("/philosophie")
  public String bankPhilosophieView() {

    return "kunde/bank/philosophie";
  }


  @GetMapping("/karriere")
  public String bankKarriereView() {

    return "kunde/bank/philosophie";
  }


  @GetMapping("/kontakt")
  public String bankKontaktView() {

    return "kunde/bank/kontakt";
  }


  @GetMapping("/impressum")
  public String bankImpressumView() {

    return "kunde/bank/impressum";
  }

}
