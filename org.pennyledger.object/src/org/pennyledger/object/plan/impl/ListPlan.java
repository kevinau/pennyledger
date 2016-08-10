package org.pennyledger.object.plan.impl;

import java.lang.reflect.Field;
import java.util.List;

import org.pennyledger.object.EntryMode;
import org.pennyledger.object.Occurs;
import org.pennyledger.object.plan.IItemPlan;
import org.pennyledger.object.plan.INodePlan;
import org.pennyledger.object.plan.IRepeatingPlan;
import org.pennyledger.object.plan.PlanKind;

public class ListPlan extends FieldPlan implements IRepeatingPlan {

  private final INodePlan elemPlan;

  private final int dimension;
  private final int minOccurs;
  private final int maxOccurs;  
  
  public ListPlan (Class<?> elemClass) {
    super (null, entityName(elemClass), entityLabel(elemClass), entityEntryMode(elemClass));
    elemPlan = ClassPlan.getClassPlan(elemClass);
    this.dimension = 0;
    this.minOccurs = 0;
    this.maxOccurs = Integer.MAX_VALUE;
  }
  
  
  public ListPlan (INodePlan parent, Field field, String name, String label, Class<?> elemClass, EntryMode entryMode, int dimension) {
    super (parent, name, label, entryMode);
    System.out.println("ListPlan " + name + "[" + dimension + "]");
    elemPlan = ClassPlan.getClassPlan(this, name, label, elemClass, entryMode);
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
  public INodePlan getElementPlan () {
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
  public void accumulateFieldPlans(List<IItemPlan<?>> fieldPlans) {
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
