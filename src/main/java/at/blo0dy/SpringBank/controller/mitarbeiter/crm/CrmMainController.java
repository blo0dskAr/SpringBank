package at.blo0dy.SpringBank.controller.mitarbeiter.crm;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("mitarbeiter/kunde")
public class CrmMainController {


  @GetMapping({"", "/", "/index"})
  public String showCrmIndexPage() {

    return "mitarbeiter/crm/index";
  }




}
