package org.pennyledger.form.plan.impl;

import java.lang.reflect.Field;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.Occurs;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.IRepeatingPlan;
import org.pennyledger.form.plan.PlanKind;
import org.pennyledger.form.reflect.IContainerReference;
import org.pennyledger.form.value.IObjectModel;

public class RepeatingPlan extends ObjectPlan implements IRepeatingPlan {

  private final IObjectPlan elemPlan;

  private final int dimension;
  private final int minOccurs;
  private final int maxOccurs;  
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public RepeatingPlan (Class<?> elemClass) {
    super (null, entityName(elemClass), entityEntryMode(elemClass));
    elemPlan = new ClassPlan(elemClass);
    this.dimension = 0;
    this.minOccurs = 0;
    this.maxOccurs = Integer.MAX_VALUE;
  }
  
  
  public RepeatingPlan (IObjectPlan parent, Field field, String name, Class<?> elemClass, int dimension, EntryMode entryMode) {
    super (parent, name, entryMode);
    elemPlan = ClassPlan.buildObjectPlan(this, field, name, elemClass, dimension, entryMode, false);
    this.dimension = dimension;
    
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
        this.maxOccurs = Integer.MAX_VALUE;
      }
    } else {
      this.minOccurs = 0;
      this.maxOccurs = Integer.MAX_VALUE;
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
  public IObjectModel buildModel(IObjectModel parent, IContainerReference container) {
    return new RepeatingWrapper(parent, container, this);
  }

}
