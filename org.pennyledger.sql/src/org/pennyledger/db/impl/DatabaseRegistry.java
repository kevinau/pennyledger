package org.pennyledger.db.impl;

import java.util.Arrays;
import java.util.Collection;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.pennyledger.db.IDatabase;
import org.pennyledger.db.IDatabaseRegistry;


@Component
public class DatabaseRegistry implements IDatabaseRegistry {

  private BundleContext context;
  
  @Activate
  public void activate (BundleContext context) {
    this.context = context;
  }
  

  @Deactivate
  public void deactivate (BundleContext context) {
    this.context = null;
  }
  
  
  @Override
  public String[] getDatabaseNames () {
    try {
      Collection<ServiceReference<IDatabase>> serviceRefs = context.getServiceReferences(IDatabase.class, null);
      String[] names = new String[serviceRefs.size()];
      
      int i = 0;
      for (ServiceReference<IDatabase> serviceRef : serviceRefs) {
        names[i++] = (String)serviceRef.getProperty("name");
      }
      Arrays.sort(names);
      return names;
    } catch (InvalidSyntaxException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public IDatabase getDatabase (String name) {
    try {
      Collection<ServiceReference<IDatabase>> serviceRefs = context.getServiceReferences(IDatabase.class, "(name=" + name + ")");
      if (serviceRefs.size() != 1) {
        throw new IllegalArgumentException(name);
      }
      ServiceReference<IDatabase> serviceRef = serviceRefs.iterator().next();
      return (IDatabase)context.getService(serviceRef);
    } catch (InvalidSyntaxException ex) {
      throw new RuntimeException(ex);
    }
  }

}
