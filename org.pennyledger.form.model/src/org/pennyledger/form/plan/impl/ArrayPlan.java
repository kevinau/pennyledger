package org.pennyledger.form.plan.impl;

import java.lang.reflect.Array;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.model.ArrayModel;
import org.pennyledger.form.model.IContainerModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IObjectModel;
import org.pennyledger.form.plan.IArrayPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.ObjectKind;

public class ArrayPlan extends ObjectPlan implements IArrayPlan {

  private final Class<?> elemClass;
  private final int minSize;
  private final int maxSize;

  private IObjectPlan elemPlan;

  private Object staticFactoryValue;
  
  
  public ArrayPlan (Object parentRef, String pathName, Class<?> elemClass, int minSize, int maxSize) {
    super (parentRef, pathName, EntryMode.UNSPECIFIED);
    this.elemClass = elemClass;
    this.minSize = minSize;
    this.maxSize = maxSize;
  }
  

  @Override
  public IObjectPlan getElementPlan () {
    return elemPlan;
  }
  
  
  public void setElementPlan (IObjectPlan elemPlan) {
    this.elemPlan = elemPlan;
  }
  
  
  public int getMinSize () {
    return minSize;
  }
  
  
  @Override
  public int getMaxSize () {
    return maxSize;
  }
  
  
  public boolean isSizeContrained () {
    return maxSize != Integer.MAX_VALUE;
  }
  
  
  public Object getStaticFactoryValue () {
    return staticFactoryValue;
  }
  
  
  void setStaticFactoryValue (Object staticFactoryValue) {
    this.staticFactoryValue = staticFactoryValue;
  }
  
  
  @Override 
  public Object newValue () {
    if (maxSize != Integer.MAX_VALUE) {
      return Array.newInstance(elemClass, maxSize);
    } else {
//// TODO Occurs for should run before this...
////      throw new ArrayIndexOutOfBoundsException();
      return Array.newInstance(elemClass,  0);
    }
  }
  
  
  @Override
  protected void dump (int level) {
    for (int i = 0; i < level; i++) {
      System.out.print("  ");
    }
    System.out.println("Array: " + getStaticLabel() + "' " + getStaticMode() + " [" + minSize + ".." + maxSize + "]");
    ((ObjectPlan)elemPlan).dump(level + 1);
  }


  @Override
  public IObjectModel createModel(IFormModel<?> ownerForm, IContainerModel parentModel, Object parentRef, String pathName, Object instance) {
    return new ArrayModel(ownerForm, parentModel, parentRef, this, instance);
  }

  
  @Override
  public boolean isSolitary () {
    return false;
  }


  @Override
  public ObjectKind kind() {
    return ObjectKind.ARRAY;
  }
  
}
