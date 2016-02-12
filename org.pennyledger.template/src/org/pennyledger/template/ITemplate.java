package org.pennyledger.template;

import java.io.Writer;


public interface ITemplate {

  public void setParameter (String name, Object value);
  
  public String merge();
  
  public void merge(Writer writer);
  
}
