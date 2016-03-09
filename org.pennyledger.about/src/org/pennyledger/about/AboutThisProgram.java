package org.pennyledger.about;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(configurationPolicy=ConfigurationPolicy.IGNORE, immediate=true)
public class AboutThisProgram {

  private static Logger logger = LoggerFactory.getLogger(AboutThisProgram.class);
  
  @Activate
  protected void activate () {
    IAboutLineAction.getAboutFile(this.getClass(), new IAboutLineAction() {
      @Override
      public void doLine(String line) {
        logger.info(line);
      }
    });
  }

  @Deactivate
  protected void deactivate () {
  }
}
