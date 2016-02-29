package org.pennyledger.form.plan;

public interface IRepeatingPlan extends IObjectPlan {

  public int getMinOccurs();
  
  public int getMaxOccurs();

  public IObjectPlan getElementPlan ();

  public int getDimension();
  
}
