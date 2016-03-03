package org.pennyledger.form.reflect;

public class FormContainerReference implements IContainerReference {

  private Object value;
  
  
  public FormContainerReference () {
  }
  
    
  @Override
  public String toString() {
    return "FormContainerObject[]";
  }


  @Override
  public <T> void setValue(T value) {
    this.value = value;
  }


  @SuppressWarnings("unchecked")
  @Override
  public <T> T getValue() {
    return (T)value;
  }

}
