package org.pennyledger.form.reflect;

public class FormContainerReference implements IContainerReference {

  @Override
  public String toString() {
    return "FormContainerObject []";
  }

  
  @Override
  public <T> void setValue(T value) {
    //throw new RuntimeException("IContainerObject.setValue should not be called on the form container");
  }

  
  @Override
  public <T> T getValue() {
    throw new RuntimeException("IContainerObject.getValue should not be called on the form container");
  }

}
