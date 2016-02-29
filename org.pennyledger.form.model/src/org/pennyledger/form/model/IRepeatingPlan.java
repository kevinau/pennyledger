package org.pennyledger.form.model;

public interface IRepeatingPlan extends IObjectPlan {

  public int getMinOccurs();
  
  public int getMaxOccurs();

  public IObjectPlan getElementPlan ();
  
}
