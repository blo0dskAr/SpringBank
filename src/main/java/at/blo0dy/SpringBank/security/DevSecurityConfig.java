package at.blo0dy.SpringBank.security;

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
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;


import javax.sql.DataSource;
import java.util.Locale;

@Configuration
@Profile({"dev", "prod"})
@EnableWebSecurity
public class DevSecurityConfig {

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  // sollte eigentlich nicht in der SecurityConfig sein..
  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver slr = new SessionLocaleResolver();
    slr.setDefaultLocale(Locale.GERMANY);
    return slr;
  }

  @Configuration
  @Profile({"dev", "prod"})
  @Order(1)
  public static class MitarbeiterConfigurationAdapter extends WebSecurityConfigurerAdapter {
    public MitarbeiterConfigurationAdapter() {
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
                      "select lc.login_name, r.name from login_credentials lc, mitarbeiter m," +
                                                       " rolle r, map_mita_role ur" +
                              " where lc.login_name=?" +
                              " and lc.mitarbeiter_id = m.id" +
                              " and m.id = ur.mita_id" +
                              " and r.id = ur.role_id")
              .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/mitarbeiter/*")
              .antMatcher("/mitarbeiter/**")
              .authorizeRequests()
                .antMatchers("/mitarbeiter/admin/**", "/mitarbeiter/admin*").hasAuthority("admin")
                .antMatchers("/mitarbeiter/kunde/**", "/mitarbeiter/kunde*").hasAuthority("mitarbeiter")
                .anyRequest()
                .authenticated()

              .and()
              .formLogin()
              .loginPage("/mitarbeiter/loginpage").permitAll()
              .loginProcessingUrl("/mitarbeiter/maauthenticationpage")
              .successForwardUrl("/mitarbeiter/index")

              .and()
              .logout()
              .logoutUrl("/mitarbeiter/logoutpage")
              .invalidateHttpSession(true)
              .deleteCookies("JSESSIONID")
              .logoutSuccessUrl("/mitarbeiter/loginpage?logout").permitAll()

              .and()
              .exceptionHandling()
              .accessDeniedPage("/access-denied")

              .and()
              .csrf().disable();

      http.headers().frameOptions().disable();
    }
  }


    @Configuration
    @Profile({"dev", "prod"})
    @Order(2)
    public static class KundeConfigurationAdapter extends WebSecurityConfigurerAdapter {

      @Autowired
      private UserDetailsService userDetailsService;

      @Autowired
      PasswordEncoder encoder;

      public KundeConfigurationAdapter() {
        super();
      }

      @Override
      protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
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
                .loginPage("/kunde/banking/loginpage")
                .usernameParameter("kundennummer")
                .passwordParameter("password")
                .loginProcessingUrl("/kunde/banking/kundeauthenticationpage")
                .successForwardUrl("/kunde/banking/index").permitAll()

                .and()
                .logout()
                .logoutUrl("/kunde/banking/logoutpage")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/kunde/banking/loginpage?logout").permitAll()

                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied")

                .and()
                .csrf().disable();

        http.headers().frameOptions().disable();

      }
  }


  // hinzugefügt um auch nur unter kunde/* einen security context zu bekommen, sollte sich noch mti dem obigen mergen lassen.
  // Kunden und mitarbeiter trennen lass ich vorerst (sollte ja ned in einer applikation stecken)
  @Configuration
  @Profile({"dev", "prod"})
  @Order(3)
  public static class InteressentConfigurationAdapter extends WebSecurityConfigurerAdapter {

    public InteressentConfigurationAdapter() {
      super();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/kunde*")
              .antMatcher("/kunde/**")
              .authorizeRequests()
              .anyRequest().permitAll()

              .and()
              .csrf().disable();

      http.headers().frameOptions().disable();

    }
  }
}
