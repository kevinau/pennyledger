package org.pennyledger.about;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.log.LogService;


@Component(configurationPolicy=ConfigurationPolicy.IGNORE, immediate=true)
public class AboutThisProgram {

  @Activate
  protected void activate (BundleContext context) {
    ServiceReference<?> ref = context.getServiceReference(LogService.class.getName());
    if (ref == null) {
      throw new RuntimeException("No log service");
    }
    LogService logger = (LogService)context.getService(ref);
    
    IAboutLineAction.getAboutFile(this.getClass(), new IAboutLineAction() {
      @Override
      public void doLine(String line) {
        logger.log(LogService.LOG_INFO, line);
      }
    });
  }

  @Deactivate
  protected void deactivate () {
  }
}
