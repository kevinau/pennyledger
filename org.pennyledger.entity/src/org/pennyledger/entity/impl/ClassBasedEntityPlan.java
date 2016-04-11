package org.pennyledger.entity.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
    IObjectPlan[] members = super.getMemberPlans();
    IObjectPlan[] dataFields = Arrays.copyOfRange(members, 1, members.length - 1);
    return dataFields;
  }
  

  @Override
  public Class<?> getEntityClass() {
    return super.getSourceClass();
  }

  
  @Override
  public String getEntityName() {
    
    // TODO Auto-generated method stub
    return null;
  }

  
  @Override
  public IFieldPlan getIdField() {
    // There is not ID field
    return null;
  }

  
  @Override
  public IFieldPlan[] getKeyFields() {
    IObjectPlan[] members = super.getMemberPlans();
    IObjectPlan keyMember = members[0];
    
    List<IFieldPlan> fieldPlans = new ArrayList<>();
    keyMember.accumulateFieldPlans(fieldPlans);
    int n = fieldPlans.size();
    
    return fieldPlans.toArray(new IFieldPlan[n]);
  }

  
  @Override
  public List<IFieldPlan[]> getUniqueConstraints() {
    return Collections.emptyList();
  }

  
  @Override
  public IFieldPlan getEntityLifeField() {
    // There is no entity life field
    return null;
  }

  @Override
  public IFieldPlan getVersionField() {
    // There is no version field
    return null;
  }
  
}
