package at.blo0dy.SpringBank.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class PropertyServiceForJasyptStarter {

  @Value("${spring.datasource.password}")
  private String property;

  public String getProperty() {
    return property;
  }

  public String getPasswordUsingEnvironment(Environment environment) {
    return environment.getProperty("spring.datasource.password");
  }

}

