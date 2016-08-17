package org.pennyledger.template.pebble;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.pennyledger.template.ITemplate;
import org.pennyledger.template.ITemplateService;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.loader.Loader;


@Component(configurationPolicy=ConfigurationPolicy.IGNORE)
public class PebbleTemplateService implements ITemplateService {

  @Activate
  protected void activate (ComponentContext componentContext) {
  }

  
  @Deactivate 
  protected void deactivate () {
  }
  
  
  @Override
  public ITemplate createTemplate(BundleContext bundleContext, String templateName) {
    // Initialize the template engine.
    Loader<String> loader = new BundleContextLoader(bundleContext);
    PebbleEngine templateEngine = new PebbleEngine.Builder().loader(loader).build();

    try {
      com.mitchellbosecke.pebble.template.PebbleTemplate compiledTemplate = templateEngine.getTemplate(templateName);
      return new PebbleTemplate(compiledTemplate);
    } catch (PebbleException ex) {
      throw new RuntimeException(ex);
    }
  }

}
