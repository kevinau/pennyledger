package org.pennyledger.form.plan;


public interface IReferencePlan<T> extends IObjectPlan {

  @Override
  public boolean isOptional();

  public IClassPlan<T> getReferencedPlan();
  
}
