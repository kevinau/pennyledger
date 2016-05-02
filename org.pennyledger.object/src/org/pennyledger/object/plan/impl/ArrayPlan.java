package org.pennyledger.object.plan.impl;

import java.lang.reflect.Field;
import java.util.List;

import org.pennyledger.object.EntryMode;
import org.pennyledger.object.Occurs;
import org.pennyledger.object.plan.IFieldPlan;
import org.pennyledger.object.plan.IObjectPlan;
import org.pennyledger.object.plan.IRepeatingPlan;
import org.pennyledger.object.plan.PlanKind;

public class ArrayPlan extends ObjectPlan implements IRepeatingPlan {

  private final static int DEFAULT_MAX_OCCURS = 10;
  
  private final IObjectPlan elemPlan;

  private final int dimension;
  private final int minOccurs;
  private final int maxOccurs;  
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public ArrayPlan (Class<?> elemClass) {
    super (null, entityName(elemClass), entityLabel(elemClass), entityEntryMode(elemClass));
    elemPlan = new ClassPlan(elemClass);
    this.dimension = 0;
    this.minOccurs = 0;
    this.maxOccurs = DEFAULT_MAX_OCCURS;
  }
  
  
  public ArrayPlan (IObjectPlan parent, Field field, String name, String label, Class<?> elemClass, int dimension, EntryMode entryMode) {
    super (parent, name, label, entryMode);
    System.out.println("ArrayPlan " + name + "[" + dimension + "]");
    elemPlan = ClassPlan.buildObjectPlan(this, field, name, label, elemClass, dimension, entryMode, false);
    this.dimension = dimension;
    System.out.println("ArrayPlan... " + elemPlan);
    
    Occurs occursAnn = field.getAnnotation(Occurs.class);
    if (occursAnn != null) {
      int[] minAnn = occursAnn.min();
      if (dimension < minAnn.length) {
        this.minOccurs = minAnn[dimension];
      } else {
        this.minOccurs = 0;
      }
      int[] maxAnn = occursAnn.max();
      if (dimension < maxAnn.length) {
        this.maxOccurs = maxAnn[dimension];
      } else {
        this.maxOccurs = DEFAULT_MAX_OCCURS;
      }
    } else {
      this.minOccurs = 0;
      this.maxOccurs = DEFAULT_MAX_OCCURS;
    }
  }
  

  @Override
  public IObjectPlan getElementPlan () {
    return elemPlan;
  }
  
  
  @Override
  public int getMinOccurs () {
    return minOccurs;
  }
  
  
  @Override
  public int getMaxOccurs () {
    return maxOccurs;
  }
  
  @Override
  public void dump (int level) {
    indent(level);
    System.out.println("Array: " + " [" + minOccurs + "," + maxOccurs + "]");
    elemPlan.dump(level + 1);
  }


  @Override
  public PlanKind kind() {
    return PlanKind.REPEATING;
  }


  @Override
  public int getDimension() {
    return dimension;
  }


  @Override
  public void accumulateFieldPlans(List<IFieldPlan<?>> fieldPlans) {
   elemPlan.accumulateFieldPlans(fieldPlans);
  }


  @Override
  public int getElementCount(Object value) {
    Object[] arrayValue = (Object[])value;
    return arrayValue.length;
  }


  @Override
  public Object getElementValue(Object value, int i) {
    Object[] arrayValue = (Object[])value;
    return arrayValue[i];
  }


//  @Override
//  public IObjectModel buildModel(IForm<?> form, IObjectModel parent, IContainerReference container) {
//    return new ArrayModel(form, parent, container, this);
//  }
//
//
//  @Override
//  public boolean isOptional() {
//    return false;
//  }
//
//
//  @Override
//  public Object[] newValue() {
//    Object[] newArray = (Object[])Array.newInstance(elemClass, minOccurs);
//    for (int i = 0; i < minOccurs; i++) {
//      newArray[i] = elemPlan.newValue();
//    }
//    return newArray;
//  }

}
