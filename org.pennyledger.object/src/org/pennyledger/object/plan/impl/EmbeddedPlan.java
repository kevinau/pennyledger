package org.pennyledger.object.plan.impl;

import org.pennyledger.object.EntryMode;
import org.pennyledger.object.plan.IObjectPlan;
import org.pennyledger.object.plan.PlanKind;

public class EmbeddedPlan<T> extends ClassPlan<T> {

  public EmbeddedPlan (IObjectPlan parent, String name, String label, Class<T> klass, EntryMode entryMode) {
    super (parent, name, label, klass, entryMode);
  }
  

  @Override
  public void dump(int level) {
    indent(level);
    System.out.println("EmbeddedPlan: " + getName());
    super.dump(level + 1);
  }

  
  @Override
  public PlanKind kind() {
    return PlanKind.EMBEDDED;
  }
  
}
