package at.blo0dy.SpringBank.security;

import at.blo0dy.SpringBank.service.kunde.KundeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@Profile("dev")
@Order(1)
public class DevKundeSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private KundeService kundeService;

  // add a reference to our security datasource
  @Autowired
  private DataSource ds;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    // User.UserBuilder users = User.withDefaultPasswordEncoder();

    auth.jdbcAuthentication().dataSource(ds)
            .usersByUsernameQuery(
                    "select kundennummer, password, is_active from kunde where kundennummer=?")
            .authoritiesByUsernameQuery(
                    "select kundennummer, rolle from kunde" +
                            " where kundennummer=?")
            .passwordEncoder(new BCryptPasswordEncoder() {
            });

//  auth.userDetailsService(kundeService);

  }

/*  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // add our user for in memory authentication
    User.UserBuilder users = User.withDefaultPasswordEncoder();

    auth.inMemoryAuthentication()
            .withUser(users.username("norole").password("test123").roles())
            .withUser(users.username("customer").password("test123").roles("CUSTOMER"))
            .withUser(users.username("employee").password("test123").roles("EMPLOYEE"))
            .withUser(users.username("manager").password("test123").roles("EMPLOYEE", "MANAGER"))
            .withUser(users.username("admin").password("test123").roles("EMPLOYEE", "ADMIN", "TESTER"));
  }*/


  // added for Custom Login Form: (ohne kommt schönere default page)
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
  }













/*  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .antMatchers("/mitarbeiter/**").hasRole("ADMIN")
*//*            .antMatchers("/leaders/**").hasRole("MANAGER")
            .antMatchers("/systems/**").hasRole("ADMIN")*//*
             .antMatchers("/**").authenticated();
             //.anyRequest().authenticated();

     *//*       http.authorizeRequests()
            .antMatchers("/preindex**").permitAll();*//*
*//*            .and()
            .formLogin()
            .loginPage("/showMyLoginPage")
            .loginProcessingUrl("/authenticateTheUser")
            .permitAll()
            .and()
            .logout().permitAll()
            .and()
            .exceptionHandling()
            .accessDeniedPage("/access-denied");*//*
  }*/

}
