package org.pennyledger.form.model;

public interface IContainerModel extends IObjectModel {
  
  public String getLabel();

  public void addContainerEventListener (ContainerEventListener x);

  public void removeContainerEventListener (ContainerEventListener x);

  public void fireContainerCreate (IContainerModel eventSource);
  
  public void fireContainerDestroy (IContainerModel eventSource);
  
  public void fireContainerOccursReduced (IContainerModel eventSource);

  /**
   * Get the name or index of a child object.  The return value will be either 
   * a String or an Integer.
   */
  public Object getKey(IObjectModel child);
}
