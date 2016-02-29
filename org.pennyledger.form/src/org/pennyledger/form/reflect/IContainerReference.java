package org.pennyledger.form.reflect;

public interface IContainerReference {

  public <T> void setValue (T value);
  
  public <T> T getValue ();
  
}
