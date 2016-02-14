package org.pennyledger.form.plan.impl;

import java.util.Map;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.IReferencePlan;

public class ReferencePlan extends GroupPlan implements IReferencePlan {

  private final boolean optional;
  
  
  public ReferencePlan(Object parentRef, String pathName, Class<?> groupClass, boolean optional, EntryMode initialEntryMode) {
    super(parentRef, pathName, groupClass, initialEntryMode);
    this.optional = optional;
  }
  
  
  @Override
  public boolean isOptional () {
    return optional;
  }

  
  @Override
  protected void dump (int level) {
    for (int i = 0; i < level; i++) {
      System.out.print("  ");
    }
    System.out.println("Reference: " + getStaticLabel() + " " + getStaticMode() + " (" + groupClass.getSimpleName() + ")");
    
    for (Map.Entry<String, IObjectPlan> entry : members.entrySet()) {
      for (int i = 0; i < level + 1; i++) {
        System.out.print("  ");
      }
      System.out.println(entry.getKey() + ":");
      IObjectPlan member = entry.getValue();
      ((ObjectPlan)member).dump(level + 1);
    }
  }


}
