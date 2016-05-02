package org.pennyledger.object.plan;

public interface IRepeatingPlan extends IObjectPlan {

  public int getMinOccurs();
  
  public int getMaxOccurs();

  public IObjectPlan getElementPlan ();

  public int getDimension();

  public int getElementCount(Object value);

  public Object getElementValue(Object value, int i);
  
}
