package at.blo0dy.SpringBank.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class DevSecurityConfig {

  @Configuration
  @Order(1)
  public static class App1ConfigurationAdapter extends WebSecurityConfigurerAdapter {
    public App1ConfigurationAdapter() {
      super();
    }

    @Autowired
    private DataSource ds;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
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
              .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/mitarbeiter*")
              .antMatcher("/mitarbeiter/**")
              .authorizeRequests()
              .anyRequest()
              .hasAuthority("admin")

              .and()
              .formLogin()
              .loginPage("/mitarbeiter/loginpage").permitAll()
              .loginProcessingUrl("/mitarbeiter/maauthenticationpage")
              .successForwardUrl("/mitarbeiter/index")
              //.defaultSuccessUrl()
              //.failureUrl()

              .and()
              .logout()
              .logoutUrl("/mitarbeiter/logoutpage")
              .invalidateHttpSession(true)
              .deleteCookies("JSESSIONID")
              .logoutSuccessUrl("/mitarbeiter/loginpage?logout")

              .and()
              .exceptionHandling()
              .accessDeniedPage("/access-denied")

              .and()
              .csrf().disable();

      http.headers().frameOptions().disable();
    }
  }


    @Configuration
    @Order(2)
    public static class App2ConfigurationAdapter extends WebSecurityConfigurerAdapter {

      @Autowired
      private UserDetailsService userDetailsService;

      @Bean
      public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
      }


      public App2ConfigurationAdapter() {
        super();
      }

      @Override
      protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
      }

      @Override
      protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/kunde/banking*")
                .antMatcher("/kunde/banking/**")
                .authorizeRequests()
                .anyRequest()
                .hasAuthority("customer")

                .and()
                .formLogin()
                .loginPage("/kunde/loginpage")
                .usernameParameter("kundennummer")
                .passwordParameter("password")
                .loginProcessingUrl("/kunde/banking/kundeauthenticationpage")
//              .failureUrl("/loginUser?error=loginError")

                .failureUrl("/kunde/loginpage?error=loginError")
                .defaultSuccessUrl("/kunde/banking/index")
                .successForwardUrl("/kunde/banking/index")
                .and()
                .logout()
//              .logoutUrl("/user_logout")
                .logoutSuccessUrl("/kunde/loginpage?logout")
                .deleteCookies("JSESSIONID")

                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied")

                .and()
                .csrf().disable();

        http.headers().frameOptions().disable();

      }


  }

}
