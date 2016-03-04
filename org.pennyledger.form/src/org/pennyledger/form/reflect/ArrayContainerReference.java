package org.pennyledger.form.reflect;

public class ArrayContainerReference implements IContainerReference {

  private final Object[] instance;
  private final int index;
  
  
  public ArrayContainerReference (Object[] instance, int index) {
    this.instance = instance;
    this.index = index;
  }
  
  
  @Override
  public String toString() {
    return "ArrayContainerObject[" + instance + ", " + index + "]";
  }


  @Override
  public <T> void setValue(T value) {
    instance[index] = value;
  }


  @SuppressWarnings("unchecked")
  @Override
  public <T> T getValue() {
    return (T)instance[index];
  }

}
