package org.pennyledger.sql.conn;

import java.util.Arrays;
import java.util.Collection;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.pennyledger.sql.INamedConnectionFactory;


public class NamedDatabaseRegistry {

  private BundleContext context;
  
  @Activate
  public void activate (BundleContext context) {
    this.context = context;
  }
  

  @Deactivate
  public void deactivate (BundleContext context) {
    this.context = null;
  }
  
  
  public String[] getConnectionFactoryNames () {
    try {
      Collection<ServiceReference<INamedConnectionFactory>> serviceRefs = context.getServiceReferences(INamedConnectionFactory.class, null);
      String[] names = new String[serviceRefs.size()];
      
      int i = 0;
      for (ServiceReference<INamedConnectionFactory> serviceRef : serviceRefs) {
        names[i++] = (String)serviceRef.getProperty("name");
      }
      Arrays.sort(names);
      return names;
    } catch (InvalidSyntaxException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  public INamedConnectionFactory getConnectionFactory (String name) {
    try {
      Collection<ServiceReference<INamedConnectionFactory>> serviceRefs = context.getServiceReferences(INamedConnectionFactory.class, "(name=" + name + ")");
      if (serviceRefs.size() != 1) {
        throw new IllegalArgumentException(name);
      }
      ServiceReference<INamedConnectionFactory> serviceRef = serviceRefs.iterator().next();
      return (INamedConnectionFactory)context.getService(serviceRef);
    } catch (InvalidSyntaxException ex) {
      throw new RuntimeException(ex);
    }
  }

}
