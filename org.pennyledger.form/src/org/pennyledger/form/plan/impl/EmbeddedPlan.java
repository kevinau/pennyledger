package org.pennyledger.form.plan.impl;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.PlanKind;

public class EmbeddedPlan<T> extends ClassPlan<T> {

  public EmbeddedPlan (IObjectPlan parent, String name, Class<T> klass, EntryMode entryMode, boolean optional) {
    super (parent, name, klass, entryMode, optional);
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
