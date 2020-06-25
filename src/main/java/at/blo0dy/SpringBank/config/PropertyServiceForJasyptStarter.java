package at.blo0dy.SpringBank.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Profile({"test", "dev"})
public class PropertyServiceForJasyptStarter {

  @Value("${spring.datasource.password}")
  private String property;

  @Value("${server.ssl.key-store-password}")
  private String keyStoreProperty;

  public String getProperty() {
    return property;
  }

  public String getKeyStoreProperty() {
    return keyStoreProperty;
  }

//  public String getPasswordUsingEnvironment(Environment environment) {
//    return environment.getProperty("spring.datasource.password");
//  }

//  public String getPasswordUsaingEnvironment(Environment environment) {
//    return environment.getProperty("server.ssl.key-store-password");
//  }

}

