package at.blo0dy.SpringBank.controller.kunde.giro;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kunde/giro")
public class KundeGiroController {

  @GetMapping({"/index", "/", ""})
  public String viewGiroIndex(Model model) {
    model.addAttribute("activeLink", "kundeGiroHome");

    return "kunde/giro/index";
  }

}
