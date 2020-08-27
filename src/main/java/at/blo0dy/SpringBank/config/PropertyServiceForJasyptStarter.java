package at.blo0dy.SpringBank.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"test", "dev","prod"})
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

}

