package at.blo0dy.SpringBank.controller.kunde.sparen;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kunde/sparen")
public class KundeSparenController {

  @GetMapping({"/index", "/", ""})
  public String viewSparenIndex() {

    return "kunde/sparen/index";
  }

  @GetMapping("/rechner")
  public String viewSparenRechner() {

    return "kunde/sparen/rechner";
  }

}
