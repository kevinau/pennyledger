package org.pennyledger.form.value;

import java.util.EventListener;


/**
 * @author Kevin Holloway
 * 
 */
public interface ModelChangeListener extends EventListener {

  /**
   * A model object has been added to the parent.
   */
  public void modelAdded (IContainerModel parent, IObjectModel addedModel);

  
  /**
   * A model object has been removed from the parent.
   */
  public void modelRemoved (IContainerModel parent, IObjectModel removedModel);
  
  
  /**
   * THe model object has replaced the previous model object of the parent.
   * This is equivalent to removing the old item, and adding the new one.  This is typically
   * called when implementations are provided for interface model objects.
   */
  public void modelReplaced (IContainerModel parent, IObjectModel removedModel, IObjectModel addedModel);
  
}
