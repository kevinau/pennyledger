package org.pennyledger.form.plan;


public interface IReferencePlan<T> extends IObjectPlan {

  public boolean isOptional();

  public IEntityPlan<T> getReferencedPlan();
  
}
