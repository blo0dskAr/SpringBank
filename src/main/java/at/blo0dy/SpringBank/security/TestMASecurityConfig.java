package at.blo0dy.SpringBank.security;

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
@Profile("test")
@Order(1)
public class TestMASecurityConfig extends WebSecurityConfigurerAdapter {

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
            .passwordEncoder(new BCryptPasswordEncoder() {
            });
  }

  // added for Custom Login Form: (ohne kommt schönere default page)
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .antMatchers("/index").permitAll()
            .antMatchers("/").permitAll()
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

    // was das ?
    http.headers().frameOptions().disable();
  }
}