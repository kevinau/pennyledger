package org.pennyledger.fs;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.pennyledger.osgi.ComponentConfiguration;
import org.pennyledger.osgi.Configurable;

@Component(service=BaseDirectory.class, configurationPolicy=ConfigurationPolicy.OPTIONAL)
public class BaseDirectory {

  @Configurable
  private Path baseDir = Paths.get(System.getProperty("user.home"), "PennyLedger");
  
  
  @Activate 
  public void activate (ComponentContext context) {
    ComponentConfiguration.load(this, context);
  }
  
  
  @Deactivate
  public void deactivate () {
  }

  
  public Path resolve (String rest) {
    return baseDir.resolve(rest);
  }

}
