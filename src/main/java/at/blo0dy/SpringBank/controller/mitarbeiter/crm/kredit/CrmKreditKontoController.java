package at.blo0dy.SpringBank.controller.mitarbeiter.crm.kredit;

import at.blo0dy.SpringBank.service.konto.kredit.KreditService;
import at.blo0dy.SpringBank.service.konto.sparen.SparService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("mitarbeiter/kunde/kredit")
public class CrmKreditKontoController {

  KreditService kreditService;
  KundeService kundeService;

  @Autowired
  public CrmKreditKontoController(KundeService kundeService, KreditService kreditService) {
    this.kundeService = kundeService;
    this.kreditService = kreditService;
  }


  @GetMapping("/konto")
  public String showKreditKontoPage() {

    return "mitarbeiter/crm/kredit/kreditKonto";
  }


}
