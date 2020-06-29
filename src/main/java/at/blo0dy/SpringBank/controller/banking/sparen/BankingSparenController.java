package at.blo0dy.SpringBank.controller.banking.sparen;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kunde/banking/sparen")
public class BankingSparenController {


  @GetMapping("/eroeffnung")
  public String viewSparenEroeffnung() {

    return "kunde/sparen/eroeffnungsForm";
  }


}
