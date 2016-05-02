package org.pennyledger.object.plan.impl;

import java.lang.reflect.Field;
import java.util.List;

import org.pennyledger.object.EntryMode;
import org.pennyledger.object.Occurs;
import org.pennyledger.object.plan.IFieldPlan;
import org.pennyledger.object.plan.IObjectPlan;
import org.pennyledger.object.plan.IRepeatingPlan;
import org.pennyledger.object.plan.PlanKind;

public class ListPlan extends ObjectPlan implements IRepeatingPlan {

  private final IObjectPlan elemPlan;

  private final int dimension;
  private final int minOccurs;
  private final int maxOccurs;  
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public ListPlan (Class<?> elemClass) {
    super (null, entityName(elemClass), entityLabel(elemClass), entityEntryMode(elemClass));
    elemPlan = new ClassPlan(elemClass);
    this.dimension = 0;
    this.minOccurs = 0;
    this.maxOccurs = Integer.MAX_VALUE;
  }
  
  
  public ListPlan (IObjectPlan parent, Field field, String name, String label, Class<?> elemClass, int dimension, EntryMode entryMode) {
    super (parent, name, label, entryMode);
    System.out.println("ListPlan " + name + "[" + dimension + "]");
    elemPlan = ClassPlan.buildObjectPlan(this, field, name, label, elemClass, dimension, entryMode, false);
    this.dimension = dimension;
    System.out.println("ListPlan... " + elemPlan);
    
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
    System.out.println("List: " + " [" + minOccurs + "," + maxOccurs + "]");
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
    List<?> listValue = (List<?>)value;
    return listValue.size();
  }


  @Override
  public Object getElementValue(Object value, int i) {
    List<?> listValue = (List<?>)value;
    return listValue.get(i);
  }

}
