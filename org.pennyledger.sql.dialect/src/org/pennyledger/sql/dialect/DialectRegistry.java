package org.pennyledger.sql.dialect;

import java.util.Arrays;
import java.util.Collection;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;


@Component(service=DialectRegistry.class)
public class DialectRegistry {

  private BundleContext context;
  
  @Activate
  public void activate (BundleContext context) {
    this.context = context;
  }
  

  @Deactivate
  public void deactivate (BundleContext context) {
    this.context = null;
  }
  
  
  public String[] getDialectNames () {
    try {
      Collection<ServiceReference<IDialect>> serviceRefs = context.getServiceReferences(IDialect.class, null);
      String[] names = new String[serviceRefs.size()];
      
      int i = 0;
      for (ServiceReference<IDialect> serviceRef : serviceRefs) {
        names[i++] = (String)serviceRef.getProperty("dialect");
      }
      Arrays.sort(names);
      return names;
    } catch (InvalidSyntaxException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  public IDialect getDialect (String name) {
    try {
      Collection<ServiceReference<IDialect>> serviceRefs = context.getServiceReferences(IDialect.class, "(dialect=" + name + ")");
      if (serviceRefs.size() != 1) {
        System.out.println("Dialect name <" + name + ">");
        System.out.println("+++ service refs " + serviceRefs.size());
        throw new IllegalArgumentException("Unknown dialect: " + name);
      }
      ServiceReference<IDialect> serviceRef = serviceRefs.iterator().next();
      return (IDialect)context.getService(serviceRef);
    } catch (InvalidSyntaxException ex) {
      throw new RuntimeException(ex);
    }
  }

}
