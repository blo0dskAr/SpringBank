package at.blo0dy.SpringBank.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mitarbeiter")
public class LoginController {


  // wann braucht er das ?  wegen custom form ?
  @GetMapping({"/loginpage", "", "/"})
  public String loginpage() {

    return "mitarbeiter/loginpage";
  }

}
