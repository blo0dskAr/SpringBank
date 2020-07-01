package at.blo0dy.SpringBank.controller.banking.sparen;


import at.blo0dy.SpringBank.dao.KundeRepository;
import at.blo0dy.SpringBank.dao.konto.sparen.SparKontoAntragRepository;
import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoRegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

  @Controller
  @RequestMapping("/kunde/banking/sparen")
  public class SparKontoRegistrationController {

    private SparKontoAntragRepository sparKontoAntragRepository;
    private KundeRepository kundeRepository ;

    @Autowired
    public SparKontoRegistrationController(SparKontoAntragRepository sparKontoAntragRepository, KundeRepository kundeRepository) {
      this.sparKontoAntragRepository = sparKontoAntragRepository;
      this.kundeRepository = kundeRepository;
    }

    @GetMapping("/register")
  public String registerForm(@CurrentSecurityContext(expression = "authentication") Authentication authentication , Model model) {

      String kundennummer = kundeRepository.findByKundennummer("123").getKundennummer();

    System.out.println(kundennummer);

    model.addAttribute("kundennummer", kundeRepository.findByKundennummer(authentication.getName()).getKundennummer());
      System.out.println(model);

    return "/kunde/banking/sparen/registration";
  }

  @PostMapping("/register")
  public String processRegistration(SparKontoRegistrationForm form) {
    System.out.println(form);
    sparKontoAntragRepository.save(form.toSparKontoAntrag());
    return "redirect:/kunde/banking/index";
  }


}
