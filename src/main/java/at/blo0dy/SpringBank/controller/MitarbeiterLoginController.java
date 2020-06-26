package at.blo0dy.SpringBank.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mitarbeiter")
public class MitarbeiterLoginController {

//  @Autowired
//  BCryptPasswordEncoder bCryptPasswordEncoder;

  @GetMapping({"/loginpage", "", "/"})
  public String loginpage() {

    return "mitarbeiter/loginpage";
  }

}
