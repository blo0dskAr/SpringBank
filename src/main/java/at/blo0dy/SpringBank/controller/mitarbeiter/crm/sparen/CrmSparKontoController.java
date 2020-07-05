package at.blo0dy.SpringBank.controller.mitarbeiter.crm.sparen;

import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import at.blo0dy.SpringBank.model.enums.AntragsStatusEnum;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.konto.sparen.SparKontoAntragService;
import at.blo0dy.SpringBank.service.konto.sparen.SparService;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;

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
