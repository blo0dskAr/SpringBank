package at.blo0dy.SpringBank.controller.kunde.giro;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kunde/giro")
public class KundeGiroController {

  @GetMapping({"/index", "/", ""})
  public String viewGiroIndex() {

    return "kunde/giro/index";
  }

  @GetMapping("/eroeffnung")
  public String viewGiroEroeffnung() {

    return "kunde/banking/giro/eroeffnungsForm";
  }

}
