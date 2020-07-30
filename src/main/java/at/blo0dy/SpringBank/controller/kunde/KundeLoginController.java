package at.blo0dy.SpringBank.controller.kunde;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/kunde/banking")
public class KundeLoginController {

//  @Autowired
//  BCryptPasswordEncoder bCryptPasswordEncoder;

  @GetMapping("/loginpage")
  public String loginpage() {
    log.debug("LoginPage wird aufgerufen.");

    return "kunde/login";
  }

}
