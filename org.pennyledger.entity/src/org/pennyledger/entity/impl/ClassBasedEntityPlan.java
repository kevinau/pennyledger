package org.pennyledger.entity.impl;

import java.util.Collections;
import java.util.List;

import org.pennyledger.form.plan.IClassPlan;
import org.pennyledger.form.plan.IEntityPlan;
import org.pennyledger.form.plan.IFieldPlan;
import org.pennyledger.form.plan.IObjectPlan;

public class ClassBasedEntityPlan extends ClassPlanAdapter implements IEntityPlan<Object> {

  ClassBasedEntityPlan(IClassPlan<?> classPlan) {
    super(classPlan);
  }

  @Override
  public IObjectPlan[] getDataFields() {
    return super.getMemberPlans();
  }

  @Override
  public Class<Object> getEntityClass() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getEntityName() {
    
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IFieldPlan getIdField() {
    return new IdFieldPlan();
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IFieldPlan[] getKeyFields() {
    return new IFieldPlan[0];
  }

  @Override
  public List<IFieldPlan[]> getUniqueConstraints() {
    return Collections.emptyList();
  }

  @Override
  public IFieldPlan getLifeField() {
    // There is no entity life field
    return null;
  }

  @Override
  public IFieldPlan getVersionField() {
    // There is no version field
    return null;
  }
  
}
