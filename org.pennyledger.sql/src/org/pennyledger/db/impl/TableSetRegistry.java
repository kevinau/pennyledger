package org.pennyledger.db.impl;

import java.util.Arrays;
import java.util.Collection;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.pennyledger.db.ITableSet;
import org.pennyledger.db.ITableSetRegistry;


@Component
public class TableSetRegistry implements ITableSetRegistry {

  private BundleContext context;
  
  @Activate
  public void activate (BundleContext context) {
    this.context = context;
  }
  

  @Deactivate
  public void deactivate (BundleContext context) {
    this.context = null;
  }
  
  
  /* (non-Javadoc)
   * @see org.pennyledger.db.ITableSetRegistry#getTableSetNames()
   */
  @Override
  public String[] getTableSetNames () {
    try {
      Collection<ServiceReference<ITableSet>> serviceRefs = context.getServiceReferences(ITableSet.class, null);
      String[] names = new String[serviceRefs.size()];
      
      int i = 0;
      for (ServiceReference<ITableSet> serviceRef : serviceRefs) {
        names[i++] = (String)serviceRef.getProperty("name");
      }
      Arrays.sort(names);
      return names;
    } catch (InvalidSyntaxException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  /* (non-Javadoc)
   * @see org.pennyledger.db.ITableSetRegistry#getTableSet(java.lang.String)
   */
  @Override
  public ITableSet getTableSet (String name) {
    try {
      Collection<ServiceReference<ITableSet>> serviceRefs = context.getServiceReferences(ITableSet.class, "(name=" + name + ")");
      if (serviceRefs.size() != 1) {
        throw new IllegalArgumentException(name);
      }
      ServiceReference<ITableSet> serviceRef = serviceRefs.iterator().next();
      return (ITableSet)context.getService(serviceRef);
    } catch (InvalidSyntaxException ex) {
      throw new RuntimeException(ex);
    }
  }

}
