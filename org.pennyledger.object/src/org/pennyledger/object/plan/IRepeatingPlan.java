package org.pennyledger.object.plan;

public interface IRepeatingPlan extends INodePlan {

  public int getMinOccurs();
  
  public int getMaxOccurs();

  public INodePlan getElementPlan ();

  public int getDimension();

  public int getElementCount(Object value);

  public Object getElementValue(Object value, int i);
  
}
