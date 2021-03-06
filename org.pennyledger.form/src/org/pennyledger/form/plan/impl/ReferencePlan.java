package org.pennyledger.form.plan.impl;

import java.util.List;

import org.pennyledger.form.Entity;
import org.pennyledger.form.EntryMode;
import org.pennyledger.form.plan.IEntityPlan;
import org.pennyledger.form.plan.IFieldPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.IReferencePlan;
import org.pennyledger.form.plan.PlanKind;


public class ReferencePlan<T> extends ObjectPlan implements IReferencePlan<T> {

  private final IEntityPlan<T> referencedPlan;
  private final boolean optional;
  
  
  public ReferencePlan(IObjectPlan parent, String pathName, Class<T> referencedClass, EntryMode entryMode, boolean optional) {
    super(parent, pathName, entryMode);
    if (!referencedClass.isAnnotationPresent(Entity.class)) {
      throw new IllegalArgumentException("Referenced class is not annotated with @Entity");
    }
    this.referencedPlan = new EntityPlan<T>(referencedClass);
    this.optional = optional;
  }
  
  
  @Override
  public boolean isOptional () {
    return optional;
  }

  
  @Override
  public IEntityPlan<T> getReferencedPlan() {
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


  @Override
  public void accumulateFieldPlans(List<IFieldPlan> fieldPlans) {
    referencedPlan.accumulateFieldPlans(fieldPlans);
  }


//  @Override
//  public IObjectModel buildModel(IForm<?> form, IObjectModel parent, IContainerReference container) {
//    return new ReferenceModel(form, parent, container, this);
//  }
//
//
//  @Override
//  public Object newValue() {
//    return referencedPlan.getIdField().newValue();
//  }

}
