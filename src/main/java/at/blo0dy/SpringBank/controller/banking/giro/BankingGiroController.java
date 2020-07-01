package at.blo0dy.SpringBank.controller.banking.giro;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kunde/banking/giro")
public class BankingGiroController {


  @GetMapping("/eroeffnung")
  public String viewGiroEroeffnung() {

    return "kunde/banking/giro/eroeffnungsForm";
  }




}
