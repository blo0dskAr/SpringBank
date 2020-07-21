package at.blo0dy.SpringBank.controller.mitarbeiter.crm.kunde;

import at.blo0dy.SpringBank.model.enums.LegiDokumentStatusEnum;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.model.person.legidoc.LegiDokument;
import at.blo0dy.SpringBank.service.kunde.KundeService;
import at.blo0dy.SpringBank.service.legidoc.LegiDokumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/mitarbeiter/kunde/person/")

public class CrmKundeController {

  KundeService kundeService;
  LegiDokumentService legiDokumentService;

  @Autowired
  public CrmKundeController(KundeService kundeService, LegiDokumentService legiDokumentService) {
    this.kundeService = kundeService;
    this.legiDokumentService = legiDokumentService;
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


  @GetMapping("/sucheDokumente")
  public String showNeueDokumentePage(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                      Model model) {

    List<LegiDokument> legiDokumentList = legiDokumentService.getNewFiles();

    model.addAttribute("ergebnis", legiDokumentList);
    model.addAttribute("legiDokument", new LegiDokument(null, null, null, null, LegiDokumentStatusEnum.NEU, null));


    return "mitarbeiter/crm/person/sucheDokumente";
  }


  @PostMapping("/sucheDokumente")
  public String searchNeueDokumentePage(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                        @ModelAttribute LegiDokument legiDokument, Model model) {

    List<LegiDokument> legiDokumentList = legiDokumentService.getSearchedFiles(legiDokument);

    model.addAttribute("ergebnis", legiDokumentList);
//    model.addAttribute("legiDokument", legiDokument);

    return "mitarbeiter/crm/person/sucheDokumente";

  }

  @GetMapping("/showDokument")
  public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam Long legiDokumentId) {

    LegiDokument legiDokument = legiDokumentService.getFile(legiDokumentId).get();
    return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(legiDokument.getDocType()))
            .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+legiDokument.getDocName())
            .body(new ByteArrayResource(legiDokument.getData()));
  }


  @GetMapping("/rejectDokument")
  public String rejectFile(@RequestParam Long legiDokumentId, RedirectAttributes redirectAttrs) {

    Optional<LegiDokument> legiDokument = legiDokumentService.getFile(legiDokumentId);
    LegiDokument tmpLegiDokument = legiDokument.get();

    String actionErgebnis = legiDokumentService.delete(tmpLegiDokument);

    // TODO: Mal evtl. irgendwann sowas wie ein Nachrichten system einführen ? :) Sonst kriegt der Kunde das Rejecten ned mit: und wär auch so ein nettes gimmick was ned sooo schwer sein sollt? (Nachrichten, und text jeweils appenden
    //  vielleicht das Textstream BloB ? =) )

    redirectAttrs.addFlashAttribute("actionErgebnis", actionErgebnis);

    return "redirect:/mitarbeiter/kunde/person/sucheDokumente";
  }


  @GetMapping("/acceptDokument")
  public String acceptFile(@RequestParam Long legiDokumentId, RedirectAttributes redirectAttrs) {

    Optional<LegiDokument> legiDokument = legiDokumentService.getFile(legiDokumentId);
    LegiDokument tmpLegiDokument = legiDokument.get();

    legiDokumentService.acceptLegiDokumentById(legiDokumentId);
    redirectAttrs.addFlashAttribute("actionErgebnis", "successfullyAccepted");

    Kunde kunde = kundeService.findById(tmpLegiDokument.getKunde().getId());

    kundeService.updateLegiStatusById(kunde.getId(), true);

    return "redirect:/mitarbeiter/kunde/person/sucheDokumente";
  }
}
