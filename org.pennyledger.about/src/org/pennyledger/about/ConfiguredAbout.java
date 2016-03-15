package org.pennyledger.about;

import java.util.Dictionary;
import java.util.Enumeration;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(configurationPolicy=ConfigurationPolicy.REQUIRE)
public class ConfiguredAbout {

  private static Logger logger = LoggerFactory.getLogger(ConfiguredAbout.class);
  
  @Activate
  protected void activate (ComponentContext context) {
    Dictionary<String, Object> dict = context.getProperties();
    for (Enumeration<String> e = dict.keys(); e.hasMoreElements(); ) {
      String key = e.nextElement();
      String value = dict.get(key).toString();
      logger.info(key + " = " + value);
    }
  }

}
