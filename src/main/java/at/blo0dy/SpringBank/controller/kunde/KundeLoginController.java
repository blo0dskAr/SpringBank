package at.blo0dy.SpringBank.controller.kunde;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kunde/banking")
public class KundeLoginController {

//  @Autowired
//  BCryptPasswordEncoder bCryptPasswordEncoder;

  @GetMapping("/loginpage")
  public String loginpage() {

    return "kunde/login";
  }

}