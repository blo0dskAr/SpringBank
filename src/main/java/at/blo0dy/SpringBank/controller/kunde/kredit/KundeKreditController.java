package at.blo0dy.SpringBank.controller.kunde.kredit;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kunde/kredit")
public class KundeKreditController {

  @GetMapping({"/index", "/", ""})
  public String viewKreditIndex() {

    return "kunde/kredit/index";
  }

  @GetMapping("/rechner")
  public String viewKreditRechner() {

    return "kunde/kredit/rechner";
  }

  @GetMapping("/eroeffnung")
  public String viewKreditEroeffnung() {

    return "kunde/banking/kredit/eroeffnungsForm";
  }

}
