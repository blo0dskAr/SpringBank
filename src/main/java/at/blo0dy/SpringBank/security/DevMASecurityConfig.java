package at.blo0dy.SpringBank.security;

import com.sun.xml.bind.api.impl.NameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@Profile("dev")
@Order(1)
public class DevMASecurityConfig extends WebSecurityConfigurerAdapter {



  // add a reference to our security datasource
  @Autowired
  private DataSource ds;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {

/*
    User.UserBuilder users = User.withDefaultPasswordEncoder();
*/

    auth.jdbcAuthentication()
            .dataSource(ds)
            .usersByUsernameQuery(
                    "select login_name, password, is_active from login_credentials where login_name=?")
            .authoritiesByUsernameQuery(
                    "select lc.login_name, r.name from login_credentials lc, mitarbeiter m, rolle r, map_mita_role ur" +
                    " where lc.login_name=?" +
                    " and lc.mitarbeiter_id = m.id" +
                    " and m.id = ur.mita_id" +
                    " and r.id = ur.role_id")
            .passwordEncoder(new BCryptPasswordEncoder() {
            });

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
    http.authorizeRequests()
            .antMatchers("/index").permitAll()
            .antMatchers("/").permitAll()
            .antMatchers("/h2-console**").permitAll()
            .antMatchers("/mitarbeiter/index").authenticated()
            .antMatchers("/mitarbeiter/").authenticated()
            .antMatchers("/mitarbeiter/**").hasAuthority("admin")
            .and()
            .formLogin()
              .loginPage("/mitarbeiter/loginpage")
              .loginProcessingUrl("/mitarbeiter/maauthenticationpage")
              .successForwardUrl("/mitarbeiter/index")
              .permitAll()
            .and()
              .logout()
              .logoutUrl("/mitarbeiter/logoutpage")
              .invalidateHttpSession(true)
              .deleteCookies("JSESSIONID")
              .logoutSuccessUrl("/mitarbeiter/loginpage?logout").permitAll()
            .and()
              .exceptionHandling()
              .accessDeniedPage("/access-denied");

    // disabled for dev-h2-console
    http.csrf().disable();
    http.headers().frameOptions().disable();
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
