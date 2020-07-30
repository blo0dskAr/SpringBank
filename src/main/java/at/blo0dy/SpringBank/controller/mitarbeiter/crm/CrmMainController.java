package at.blo0dy.SpringBank.controller.mitarbeiter.crm;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("mitarbeiter/kunde")
public class CrmMainController {


  @GetMapping({"", "/", "/index"})
  public String showCrmIndexPage() {
    log.debug("MitarbeiterCRM-IndexPage wird aufgerufen");

    return "mitarbeiter/crm/index";
  }




}
