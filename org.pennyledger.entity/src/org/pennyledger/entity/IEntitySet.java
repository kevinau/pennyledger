package org.pennyledger.entity;

import java.util.Map;

public interface IEntitySet {

  public String getName();
  
  public Map<String, String> getEntityDbMapping();
    
}
