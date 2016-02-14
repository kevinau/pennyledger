package org.pennyledger.form.plan;


public interface IArrayPlan extends IContainerPlan {

  public IObjectPlan getElementPlan ();

  public int getMaxSize();
 
}
