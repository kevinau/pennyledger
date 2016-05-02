package org.pennyledger.entitydb.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.pennyledger.entitydb.IEntityDatabaseMapping;
import org.pennyledger.entitydb.IEntityDatabaseMapping.MappingEntry;
import org.pennyledger.entitydb.IEntityDatabaseMappingRegistry;


@Component
public class EntityDatabaseMappingRegistry implements IEntityDatabaseMappingRegistry {

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
  public String[] getMappingNames () {
    try {
      Collection<ServiceReference<IEntityDatabaseMapping>> serviceRefs = context.getServiceReferences(IEntityDatabaseMapping.class, null);
      String[] names = new String[serviceRefs.size()];
      
      int i = 0;
      for (ServiceReference<IEntityDatabaseMapping> serviceRef : serviceRefs) {
        names[i++] = (String)serviceRef.getProperty("name");
      }
      Arrays.sort(names);
      return names;
    } catch (InvalidSyntaxException ex) {
      throw new RuntimeException(ex);
    }
  }
  

  @Override
  public List<MappingEntry> getMappingEntries(String name) {
    try {
      Collection<ServiceReference<IEntityDatabaseMapping>> serviceRefs = context.getServiceReferences(IEntityDatabaseMapping.class, "(name=" + name + ")");
      if (serviceRefs.size() == 0) {
        throw new IllegalArgumentException("No entity-database mapping component named = '" + name + "'");
      }
      ServiceReference<IEntityDatabaseMapping> serviceRef = serviceRefs.iterator().next();
      IEntityDatabaseMapping mapping = context.getService(serviceRef);
      return mapping.getEntityMappings();
    } catch (InvalidSyntaxException ex) {
      throw new RuntimeException(ex);
    }
  }
  
}
