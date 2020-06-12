package at.blo0dy.SpringBank.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@Profile("test")
public class TestSecurityConfig extends WebSecurityConfigurerAdapter {

  // add a reference to our security datasource
  @Autowired
  private DataSource ds;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication().dataSource(ds);
  }


  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .antMatchers("/mitarbeiter/**").authenticated()
            //.antMatchers("/mitarbeiter/**").hasRole("ADMIN")
/*            .antMatchers("/leaders/**").hasRole("MANAGER")
            .antMatchers("/systems/**").hasRole("ADMIN")*/
             //.antMatchers("/**").authenticated().
             .anyRequest().permitAll();

     /*       http.authorizeRequests()
            .antMatchers("/preindex**").permitAll();*/
/*            .and()
            .formLogin()
            .loginPage("/showMyLoginPage")
            .loginProcessingUrl("/authenticateTheUser")
            .permitAll()
            .and()
            .logout().permitAll()
            .and()
            .exceptionHandling()
            .accessDeniedPage("/access-denied");*/
  }

}
