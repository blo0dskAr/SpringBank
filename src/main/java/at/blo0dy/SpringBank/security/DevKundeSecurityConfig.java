package at.blo0dy.SpringBank.security;

import at.blo0dy.SpringBank.service.kunde.KundeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@Profile("dev")
@Order(1)
public class DevKundeSecurityConfig extends WebSecurityConfigurerAdapter {

//  @Autowired
//  private KundeService kundeService;

  @Autowired
  private UserDetailsService userDetailsService;

  // add a reference to our security datasource
  @Autowired
  private DataSource ds;

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {

  auth.userDetailsService(userDetailsService).passwordEncoder(encoder());

    // working jdbc implementation
/*    auth.jdbcAuthentication().dataSource(ds)
            .usersByUsernameQuery(
                    "select kundennummer, password, is_active from kunde where kundennummer=?")
            .authoritiesByUsernameQuery(
                    "select kundennummer, rolle from kunde" +
                            " where kundennummer=?")
            .passwordEncoder(new BCryptPasswordEncoder() {
            });*/
  }




  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .antMatchers("/kunde/banking/index")
            .hasAuthority("customer")
          .antMatchers("/kunde/**").permitAll()

          .and()
            .formLogin()
            .loginPage("/kunde/loginpage")
            .loginProcessingUrl("/kunde/kundeauthenticationpage")
            .usernameParameter("kundennummer")
            .passwordParameter("password")
            .successForwardUrl("/kunde/index");


    http.csrf().disable();
    http.headers().frameOptions().disable();

  }


/*  // LAST WORKING
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.requestMatchers()
//            .antMatchers("/kunde/*").permitAll()
            .antMatchers("/kunde/banking/**")
            .and()
            .authorizeRequests().anyRequest().hasAuthority("customer")
            // .anyRequest().authenticated()      // auskommentiert weil wir oben auf Rollen prüfen, und nicht nur
            // aufs authentifiziert ja/nein schaun
            .and()
            .formLogin()
            .loginPage("/kunde/loginpage")
            .loginProcessingUrl("/kunde/kundeauthenticationpage")
            .successForwardUrl("/kunde/banking/index")
            .permitAll()
            .and()
            .logout().permitAll()
            .and()
            .exceptionHandling()
            .accessDeniedPage("/access-denied");

//    http.csrf().disable();
//    http.headers().frameOptions().disable();
  }*/

}
