package org.pennyledger.form.plan.impl;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.plan.IClassPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.IReferencePlan;
import org.pennyledger.form.plan.PlanKind;


public class ReferencePlan<T> extends ObjectPlan implements IReferencePlan<T> {

  private final IClassPlan<T> referencedPlan;
  private final boolean optional;
  
  
  public ReferencePlan(IObjectPlan parent, String pathName, Class<T> referencedClass, EntryMode entryMode, boolean optional) {
    super(parent, pathName, entryMode);
    this.referencedPlan = new ClassPlan<T>(referencedClass);
    this.optional = optional;
  }
  
  
  @Override
  public boolean isOptional () {
    return optional;
  }

  
  @Override
  public IClassPlan<T> getReferencedPlan() {
    return referencedPlan;
  }
  
  
  @Override
  public void dump (int level) {
    indent(level);
    System.out.println("Reference: " + referencedPlan.getName());
  }


  @Override
  public PlanKind kind() {
    return PlanKind.REFERENCE;
  }

}
