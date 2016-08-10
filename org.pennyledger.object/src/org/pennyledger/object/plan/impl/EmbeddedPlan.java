package org.pennyledger.object.plan.impl;

import java.lang.reflect.Field;
import java.util.List;

import org.pennyledger.object.EntryMode;
import org.pennyledger.object.plan.IItemPlan;
import org.pennyledger.object.plan.INodePlan;
import org.pennyledger.object.plan.PlanKind;

public class EmbeddedPlan<T> extends FieldPlan {

  private final INodePlan embeddedPlan;
  
  
  public EmbeddedPlan (INodePlan parent, Field field, String name, String label, Class<T> embeddedClass, EntryMode entryMode) {
    super (parent, name, label, entryMode);
    embeddedPlan = ClassPlan.getClassPlan(this, name, label, embeddedClass, entryMode);
    System.out.println("EmbeddedPlan... " + embeddedPlan);
  }
  

  @Override
  public void dump(int level) {
    indent(level);
    System.out.println("EmbeddedPlan: " + getName());
    embeddedPlan.dump(level + 1);
  }

  
  @Override
  public PlanKind kind() {
    return PlanKind.EMBEDDED;
  }


  @Override
  public void accumulateFieldPlans(List<IItemPlan<?>> fieldPlans) {
    embeddedPlan.accumulateFieldPlans(fieldPlans);
  }
  
}
