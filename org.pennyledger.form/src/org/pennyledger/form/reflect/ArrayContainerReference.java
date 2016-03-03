package org.pennyledger.form.reflect;

import java.lang.reflect.Array;

public class ArrayContainerReference implements IContainerReference {

  private final Object container;
  private final int index;
  
  
  public ArrayContainerReference (Object container, int index) {
    this.container = container;
    this.index = index;
  }
  
  
  @Override
  public String toString() {
    return "ArrayContainerObject[" + container + ", " + index + "]";
  }


  @Override
  public <T> void setValue(T value) {
    Array.set(container, index, value);
  }


  @SuppressWarnings("unchecked")
  @Override
  public <T> T getValue() {
    return (T)Array.get(container, index);
  }

}
