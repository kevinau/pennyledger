package org.pennyledger.form.model;

import java.util.EventListener;

/**
 * @author Kevin Holloway
 * 
 */
public interface ContainerEventListener extends EventListener {

  /**
   * A form container has been created.
   * @param model The container model that has been created.  This is based on the instance object.
   * @param instance The instance object that has been created.
   * 
   */
  public void containerCreate(IContainerModel eventSource);

  /**
   * A form container has been destroyed.
   * @param model
   * @param isEqual
   */
  public void containerDestroy(IContainerModel eventSource);

  
  /**
   * An array or list has had the number of elements reduced.
   */
  public void containerOccursReduced(IContainerModel eventSource);

}
