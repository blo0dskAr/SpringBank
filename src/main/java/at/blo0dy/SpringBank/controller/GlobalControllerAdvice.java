package at.blo0dy.SpringBank.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerAdvice {

  @ModelAttribute
  public void handleRequest(HttpServletRequest request, Model model) {
    String requestURI = request.getRequestURI();
    String contextPath = request.getContextPath();
    String pathInfo = request.getPathInfo();
    String queryString = request.getQueryString();

    model.addAttribute("requestURI", requestURI);
    model.addAttribute("contextPath", contextPath);
    model.addAttribute("pathInfo", pathInfo);
    model.addAttribute("queryString", queryString);


  }


}
