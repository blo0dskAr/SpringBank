package at.blo0dy.SpringBank.controller.banking.kredit;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kunde/banking/kredit")
public class BankingKreditController {


  @GetMapping("/eroeffnung")
  public String viewKreditEroeffnung() {

    return "kunde/banking/kredit/eroeffnungsForm";
  }




}
