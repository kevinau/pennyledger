package org.pennyledger.object.plan;


public interface IReferencePlan<T> extends INodePlan {

  public boolean isOptional();

  public IEntityPlan<T> getReferencedPlan();
  
}
