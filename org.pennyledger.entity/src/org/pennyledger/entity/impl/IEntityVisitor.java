package org.pennyledger.entity.impl;

import org.pennyledger.form.plan.IEntityPlan;

public interface IEntityVisitor {

  public void processPlan (IEntityPlan<?> plan);
  
}
