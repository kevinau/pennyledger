package org.pennyledger.form.reflect;

import java.lang.reflect.Field;

public class ClassContainerReference implements IContainerReference {

  private final Object instance;
  private final Field field;
  
  
  public ClassContainerReference (Object instance, Field field) {
    if (instance == null) {
      throw new IllegalArgumentException("'instance' argument must not be null");
    }
    if (instance instanceof IContainerReference) {
      throw new IllegalArgumentException(instance.toString());
    }
    if (field == null) {
      throw new IllegalArgumentException("'field' argument must not be null");
    }
    this.instance = instance;
    this.field = field;
  }
  
    
  @Override
  public String toString() {
    return "GroupContainerObject [" + instance + ", " + field + "]";
  }


  @Override
  public <T> void setValue(T value) {
    try {
      field.setAccessible(true);
      field.set(instance, value);
    } catch (IllegalArgumentException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }


  @SuppressWarnings("unchecked")
  @Override
  public <T> T getValue() {
    T value;
    try {
      field.setAccessible(true);
      value = (T)field.get(instance);
    } catch (IllegalArgumentException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
    return value;
  }

}
