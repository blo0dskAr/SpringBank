package at.blo0dy.SpringBank.controller.mitarbeiter.crm.giro;

import at.blo0dy.SpringBank.service.konto.giro.GiroService;
import at.blo0dy.SpringBank.service.konto.sparen.SparService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("mitarbeiter/kunde/giro")
public class CrmGiroKontoController {

  GiroService giroService;
  KundeService kundeService;

  @Autowired
  public CrmGiroKontoController(KundeService kundeService, GiroService giroService) {
    this.kundeService = kundeService;
    this.giroService = giroService;
  }


  @GetMapping("/konto")
  public String showGiroKontoPage() {

    return "mitarbeiter/crm/giro/giroKonto";
  }


}
