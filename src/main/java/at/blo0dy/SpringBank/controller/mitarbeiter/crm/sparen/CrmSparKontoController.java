package at.blo0dy.SpringBank.controller.mitarbeiter.crm.sparen;

import at.blo0dy.SpringBank.service.konto.sparen.SparService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("mitarbeiter/kunde/sparen")
public class CrmSparKontoController {

  SparService sparService;
  KundeService kundeService;

  @Autowired
  public CrmSparKontoController(KundeService kundeService, SparService sparService) {
    this.kundeService = kundeService;
    this.sparService = sparService;
  }


  @GetMapping("/konto")
  public String showSparKontoPage() {

    return "mitarbeiter/crm/sparen/sparKonto";
  }


}
