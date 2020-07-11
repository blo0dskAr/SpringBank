//package at.blo0dy.SpringBank.exception;
//
//import lombok.extern.slf4j.Slf4j;
//import org.dom4j.rule.Mode;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.http.HttpStatus;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//
//@ControllerAdvice
//@Slf4j
//public class GlobalExceptionHandler {
//
////  @ResponseStatus(HttpStatus.BAD_REQUEST)
//  @ExceptionHandler(EmptyResultDataAccessException.class)
//  public ModelAndView inputOutputError(HttpServletRequest req, Exception e, Model model) {
////    ModelAndView error = new ModelAndView("error");
//    ModelAndView error = new ModelAndView();
//    error.addObject("errorObj", e);
//
//    get
//
//    log.error("Got Exception: " + e );
//    log.error("RequestStuff: " + req.getContextPath());
//    log.error("RequestStuff: " + req.getRequestURI());
//    log.error("RequestStuff: " + req.getPathInfo());
//    log.error("RequestStuff: " + req.getServletPath());
//    log.error("RequestStuff: " + req.getMethod());
//    log.error("RequestStuff: " + req.getRequestURL());
//    log.error(error.getViewName());
//
//    error.addObject("errorMesg", "CustomError: Entity not found Error");
//    return error;
//  }
//
//}
