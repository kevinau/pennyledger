package org.pennyledger.about;

import java.util.Dictionary;
import java.util.Enumeration;

import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.log.LogService;

@Component(configurationPolicy=ConfigurationPolicy.REQUIRE)
public class ConfiguredAbout {

  @Activate
  protected void activate (ComponentContext context) {
    ServiceReference<?> ref = context.getBundleContext().getServiceReference(LogService.class.getName());
    if (ref == null) {
      throw new RuntimeException("No log service");
    }
    LogService logger = (LogService)context.getBundleContext().getService(ref);
    
    Dictionary<String, Object> dict = context.getProperties();
    for (Enumeration<String> e = dict.keys(); e.hasMoreElements(); ) {
      String key = e.nextElement();
      String value = dict.get(key).toString();
      logger.log(LogService.LOG_INFO, key + " = " + value);
    }
  }

}
