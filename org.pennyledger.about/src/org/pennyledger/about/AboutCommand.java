package org.pennyledger.about;

import org.osgi.service.component.annotations.Component;

@Component(
    property = { "osgi.command.scope" + ":String=pl",
                 "osgi.command.function" + ":String=about" }, 
    service = Object.class)
public class AboutCommand {

  public Object about() {
    try {
      IAboutLineAction.getAboutFile(this.getClass(), new IAboutLineAction() {
        @Override
        public void doLine(String line) {
          System.out.println(line);
        }
      });
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

}
