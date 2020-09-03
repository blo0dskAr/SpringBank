package at.blo0dy.SpringBank.controller.banking;

import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.model.person.legidoc.LegiDokument;
import at.blo0dy.SpringBank.model.person.passwordForm.ChangePasswordForm;
import at.blo0dy.SpringBank.service.adresse.AdresseService;
import at.blo0dy.SpringBank.service.konto.KontoAntragService;
import at.blo0dy.SpringBank.service.konto.KontoService;
import at.blo0dy.SpringBank.service.konto.giro.GiroKontoAntragService;
import at.blo0dy.SpringBank.service.konto.giro.GiroService;
import at.blo0dy.SpringBank.service.konto.kredit.KreditKontoAntragService;
import at.blo0dy.SpringBank.service.konto.kredit.KreditService;
import at.blo0dy.SpringBank.service.konto.sparen.SparKontoAntragService;
import at.blo0dy.SpringBank.service.konto.sparen.SparService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/kunde/banking")
public class KundeBankingController {

  KontoService kontoService;
  GiroService giroService;
  KreditService kreditService;
  SparService sparService;
  KontoAntragService kontoAntragService;
  GiroKontoAntragService giroKontoAntragService;
  KreditKontoAntragService kreditKontoAntragService;
  SparKontoAntragService sparKontoAntragService;
  KundeService kundeService;
  AdresseService adresseService;
  LegiDokumentService legiDokumentService;


  @Autowired
  public KundeBankingController(KontoService kontoService, GiroService giroService, KreditService kreditService, SparService sparService, KontoAntragService kontoAntragService,
                                GiroKontoAntragService giroKontoAntragService, KreditKontoAntragService kreditKontoAntragService, SparKontoAntragService sparKontoAntragService,
                                KundeService kundeService, AdresseService adresseService, LegiDokumentService legiDokumentService) {
    this.kontoService = kontoService;
    this.giroService = giroService;
    this.kreditService = kreditService;
    this.sparService = sparService;
    this.kontoAntragService = kontoAntragService;
    this.giroKontoAntragService = giroKontoAntragService;
    this.kreditKontoAntragService = kreditKontoAntragService;
    this.sparKontoAntragService = sparKontoAntragService;
    this.kundeService = kundeService;
    this.adresseService = adresseService;
    this.legiDokumentService = legiDokumentService;
  }

  @RequestMapping({"", "/", "/index"})
  public String getIndexPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

    // Login Workaround, irgendwie billig, aber es funktioniert (null pointer exception fliegt, scheinbar passt was ned mit der authenticationPage und dem loginForwardingUrl config ned...
    if (authentication==null) {
      return "redirect:/kunde/banking/index";
    }
    String authKundennummer = authentication.getName();
    log.debug("Showing Banking-IndexPage für Kunde: " + authKundennummer);

    BigDecimal gesamtSaldoOffenerKonten = kontoService.getGesamtSaldoOffenerKontenByKundennummer(authKundennummer);

    List<SparKontoAntrag> sparKontoAntragListe = sparKontoAntragService.findSparAntraegeByKundennummer(authKundennummer);
    List<KreditKontoAntrag> kreditKontoAntragListe = kreditKontoAntragService.findKreditAntraegeByKundennummer(authKundennummer);
    List<GiroKontoAntrag> giroKontoAntragListe = giroKontoAntragService.findGiroAntraegeByKundennummer(authKundennummer);
    List<SparKonto> sparKontenListe = sparService.findSparKontoByKundennummer(authKundennummer);
    List<KreditKonto> kreditKontenListe = kreditService.findKreditKontenByKundennummer(authKundennummer);
    List<GiroKonto> giroKontenListe = giroService.findGiroKontenByKundennummer(authKundennummer);

    Kunde kunde = kundeService.findByKundennummer(authKundennummer);

    model.addAttribute("activeLink", "KundeBankingHome");
    model.addAttribute("kunde", kunde);
    model.addAttribute("sparkontoantragliste", sparKontoAntragListe);
    model.addAttribute("kreditkontoantragliste", kreditKontoAntragListe);
    model.addAttribute("girokontoantragliste", giroKontoAntragListe);
    model.addAttribute("sparkontenliste", sparKontenListe);
    model.addAttribute("kreditkontenliste", kreditKontenListe);
    model.addAttribute("girokontenliste", giroKontenListe);


    // TODO: Ich möcht zwar gern eine seite wo viel passiert (wie man performance spürt), aber trotzdem sollten die untrigen mal gruppiert in listen oder ähnliches durchgeführt werden
    // TODO: Damit nicht zu viele einzelrequest in die Datenbank notwendig sind.
    model.addAttribute("antraegeGesamt", kontoAntragService.countAntraegeGesamtByKundennummer(authKundennummer));
    model.addAttribute("offeneAntraegeGesamt", kontoAntragService.countOffeneAntraegeByKundennummer(authKundennummer));
    model.addAttribute("abgelehnteAntraegeGesamt", kontoAntragService.countAbgelehnteAntraegeByKundennummer(authKundennummer));
    model.addAttribute("durchgefuehrteAntraegeGesamt", kontoAntragService.countDurchgefuehrteAntraegeByKundennummer(authKundennummer));

    model.addAttribute("kontenGesamt", kontoService.countKontenGesamtByKundennummer(authKundennummer));
    model.addAttribute("offeneKontenGesamt", kontoService.countOffeneKontenGesamtByKundennummer(authKundennummer));


    model.addAttribute("gesamtSaldoOffenerKonten", gesamtSaldoOffenerKonten);

    return "kunde/banking/index";
  }


  @GetMapping("/kunde-detailpage")
  public String getKundeDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

    String authKundennummer = authentication.getName();
    Kunde kunde = kundeService.findByKundennummer(authKundennummer);
    log.debug("Showing viewKundeDetailPage for Kunde: " + authKundennummer);

    LegiDokument legiDokument = legiDokumentService.findByKunde(kunde.getId());
    if (legiDokument == null) {
      legiDokument = new LegiDokument();
    }

    model.addAttribute("kunde", kunde);
    model.addAttribute("legiDokument", legiDokument);


    return "kunde/banking/kunde-detail";
  }


  @PostMapping("/kunde-detailpage")
  public String saveKundeDetailPage(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                    @Valid @ModelAttribute Kunde kunde, BindingResult result,
                                    Model model, RedirectAttributes redirectAttrs) {
    String authKundennummer = authentication.getName();
    log.debug("Saving viewKundeDetailPage for Kunde: " + authKundennummer);

    if (result.hasErrors()) {
      model.addAttribute("kunde", kunde);
      LegiDokument legiDokument = legiDokumentService.findByKunde(kunde.getId());
      if (legiDokument == null) {
        legiDokument = new LegiDokument();
      }
      model.addAttribute("legiDokument", legiDokument);

      log.debug("Fehler beim Speichern der saveKundeDetailPage for Kunde: " + authKundennummer);
      return "kunde/banking/kunde-detail";
    }

    adresseService.save(kunde.getAdresse());
    kundeService.updateChangeableDataByKundennummer(authKundennummer, kunde.getEmailAdresse(), kunde.getTelefonNummer(), kunde.getConnectedGiro(), kunde.isHasAcceptedAGB());

    model.addAttribute("kunde", kunde);
    redirectAttrs.addFlashAttribute("persDatenGespeichert",true);

    kundeService.setKundeActiveIfRequirementsMetByKundennummer(authKundennummer);

    return "redirect:/kunde/banking/index";
  }

  @PostMapping("/kunde-detailpage/uploadLegi")
  public String processLegiUpload(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                  @RequestParam("legiDokument") MultipartFile file,
                                  Model model, RedirectAttributes redirectAttrs) {

    String authKundennummer = authentication.getName();
    log.debug("Legitimation für Kunde: " + authKundennummer + " wird hochgeladen.");
    Kunde kunde = kundeService.findByKundennummer(authKundennummer);

    legiDokumentService.saveFile(file, kunde);
    redirectAttrs.addFlashAttribute("uploadSuccessful", true);

    return "redirect:/kunde/banking/kunde-detailpage";
  }

  @GetMapping("/kunde-detailpage/downloadLegi/{legiDokumentId}")
  public ResponseEntity<ByteArrayResource> downloadFile(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                                        @PathVariable Long legiDokumentId) {
    String authKundennummer = authentication.getName();
    log.debug("Legitimation für Kunde: " + authKundennummer + " und LegiDocId: " + legiDokumentId + " wird abgerufen.");

    LegiDokument legiDokument = legiDokumentService.getFile(legiDokumentId).get();
    return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(legiDokument.getDocType()))
            .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+legiDokument.getDocName())
            .body(new ByteArrayResource(legiDokument.getData()));
  }



  @GetMapping("/showChangePasswordForm")
  public String showChangePasswordForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

    log.debug("Passwort Ändern Page von Mitarbeiter: " + authentication.getName() + " aufgerufen.");
    ChangePasswordForm changePasswordForm = new ChangePasswordForm();
    model.addAttribute("changePasswordForm", changePasswordForm);
    model.addAttribute("activeLink", "changePasswordPage");

    return "kunde/banking/changepassword-form";
  }



  @PostMapping("/saveChangePasswordForm")
  public String saveChangePasswordForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                       @Valid @ModelAttribute ChangePasswordForm changePasswordForm, BindingResult result,
                                       Model model, RedirectAttributes redirectAttrs) {

    String authKundennummer = authentication.getName();
    log.debug("Neues Passwort für Kunde: " + authKundennummer + " wurde erstellt.");

    Kunde tmpKunde = kundeService.findByKundennummer(authKundennummer);
    final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    log.debug("Neues Passwort für " + authKundennummer + " wird geprüft");
    compareNewPasswordWithConfirmPassword(changePasswordForm, result, authKundennummer);

    log.debug("Altes Passwort für " + authKundennummer + " wird geprüft");
    if (!passwordEncoder.matches(changePasswordForm.getOldPassword(), tmpKunde.getPassword() )) {
      result.rejectValue("oldPassword", "error.changePasswordForm", "Ein Fehler ist aufgetreten");
      log.debug("Altes Passwort für " + authKundennummer + " stimmt mit dem eingebenen Passwort nicht überrein ");
    }

    if (result.hasErrors()) {
      log.debug("Neues Passwort für " + authKundennummer + " kann nicht geändert werden, Seite wird neu ausgeliefert");
      model.addAttribute("changePasswordForm", changePasswordForm);
      model.addAttribute("activeLink", "changePasswordPage");

      return "kunde/banking/changepassword-form";
    } else {

      log.debug("Neues Passwort für " + authKundennummer + " wird gespeichert");
      kundeService.updatePasswordByKundeId(tmpKunde.getId(), passwordEncoder.encode(changePasswordForm.getNewPassword()));
      log.debug("Neues Passwort für " + authKundennummer + " wurde erfolgreich gespeichert");
      redirectAttrs.addFlashAttribute("passwordGeaendert", true);

      return "redirect:/kunde/banking/index";
    }
  }

  private void compareNewPasswordWithConfirmPassword(@ModelAttribute @Valid ChangePasswordForm changePasswordForm, BindingResult result, String authKundennummer) {
    if (!changePasswordForm.getConfirmPassword().equals(changePasswordForm.getNewPassword())) {
      result.rejectValue("confirmPassword", "error.changePasswordForm","Die neuen Passwörter stimmen nicht überrein");
      result.rejectValue("newPassword","error.changePasswordForm","Die neuen Passwörter stimmen nicht überein");
      log.debug("Neues Passwort für " + authKundennummer + " stimmt mit ConfirmPassword nicht überrein ");
    }
  }


}
