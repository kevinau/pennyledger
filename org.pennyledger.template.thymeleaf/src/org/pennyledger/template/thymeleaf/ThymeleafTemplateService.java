package org.pennyledger.template.thymeleaf;

import java.io.Writer;
import java.net.URL;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.pennyledger.template.ITemplate;
import org.pennyledger.template.ITemplateService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.UrlTemplateResolver;


@Component(configurationPolicy=ConfigurationPolicy.IGNORE)
public class ThymeleafTemplateService implements ITemplateService {

  private TemplateEngine templateEngine;

  @Activate
  protected void activate (ComponentContext componentContext) {
    // Initialize the template engine.
    UrlTemplateResolver templateResolver = new UrlTemplateResolver();
    templateResolver.setTemplateMode("HTML5");
    templateResolver.setSuffix("");
    templateResolver.setCharacterEncoding("UTF-8");
    // Template cache TTL=1h. If not set, entries would be cached until expelled by LRU
    templateResolver.setCacheTTLMs(3600000L);
    
    templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);
  }

  
  @Deactivate 
  protected void deactivate () {
    templateEngine = null;
  }
  
  
  @Override
  public ITemplate createTemplate(BundleContext bundleContext, String templateName) {
    Bundle bundle = bundleContext.getBundle();
    
    URL templateURL = bundle.getResource(templateName);
    if (templateURL == null) {
      throw new IllegalArgumentException("Template '" + templateName + "' not found in bundle: " + bundle.getSymbolicName());
    }
    return new ThymeleafTemplate(this, templateURL);
  }
  
  
  void process (URL templateURL, IContext ctx, Writer writer) {
    templateEngine.process(templateURL.toString(), ctx, writer);
  }

  String process (URL templateURL, IContext ctx) {
    return templateEngine.process(templateURL.toString(), ctx);
  }

}
