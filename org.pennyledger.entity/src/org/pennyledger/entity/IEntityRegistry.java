package org.pennyledger.entity;

import java.util.List;

public interface IEntityRegistry {

  public List<Class<?>> getEntityClasses (String packagePrefix);
  
}
