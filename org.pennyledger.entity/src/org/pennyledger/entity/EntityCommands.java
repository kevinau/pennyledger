package org.pennyledger.entity;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(property = { 
    "osgi.command.scope=entity",
//    "osgi.command.function=create",
    "osgi.command.function=list",
  }, 
  service = Object.class)
public class EntityCommands {
  
  private IEntityRegistry entityRegistry;
  
  @Reference
  protected void setEntityRegistry (IEntityRegistry entityRegistry) {
    this.entityRegistry = entityRegistry;  
  }

  protected void unsetEntityRegistry (IEntityRegistry entityRegistry) {
    this.entityRegistry = null;  
  }

  public Object list() {
    List<Class<?>> klasses = entityRegistry.getEntityClasses("");
    for (Class<?> klass : klasses) {
      System.out.println(klass.getCanonicalName());
    }
    return null;
  }

}
