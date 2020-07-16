package at.blo0dy.SpringBank.controller.mitarbeiter.crm.kunde;

import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/mitarbeiter/kunde/person/")

public class CrmKundeController {

  KundeService kundeService;

  @Autowired
  public CrmKundeController(KundeService kundeService) {
    this.kundeService = kundeService;
  }


  @GetMapping("showKundeDetailPage")
  public String showKundeDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                    @RequestParam("kundeId") Long kundeId, Model model) {

    log.debug("Showing SparKontoDetailPage for Mitarbeiter: " + authentication.getName() + " und KundeId: " + kundeId);

    Kunde kunde = kundeService.findById(kundeId);
    model.addAttribute("kunde", kunde);

    return "mitarbeiter/crm/person/person-detail" ;
  }


  @PostMapping("saveKundeDetailPage")
  public String saveKundeDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                    @Valid @ModelAttribute Kunde kunde, BindingResult result,
                                    Model model, RedirectAttributes redirectAttrs) {

    String tmpMitarbeiter = authentication.getName();
    String tmpKundennummer = kunde.getKundennummer();
    log.debug("KundeDetails sollen gespeichert werden für Mitarbeiter: " + tmpMitarbeiter + " und KundeNr: " + tmpKundennummer);

    if(result.hasErrors()) {
      log.debug("Fehler beim speichern der KundeDetails für Mitarbeiter: " + tmpMitarbeiter + " und KundeNr: " + tmpKundennummer + " erhalten. Wird mit Fehler neu geladen. (count=" + result.getErrorCount() + ")");
      model.addAttribute("kunde", kunde);
      return "mitarbeiter/crm/person/person-detail";
    }

    log.debug("KundeDetails werden gespeichert für Mitarbeiter: " + tmpMitarbeiter + " und KundeNr: " + tmpKundennummer);
    kundeService.saveWithoutPassword(kunde);
    log.debug("KundeDetails wurden erfolgreich gespeichert für Mitarbeiter: " + tmpMitarbeiter + " und KundeNr: " + tmpKundennummer);

    model.addAttribute("kunde", kunde);
    redirectAttrs.addFlashAttribute("personDatenGespeichert", true);

    return "redirect:/mitarbeiter/kunde/person/showKundeDetailPage?kundeId=" + kunde.getId() ;
  }





}
