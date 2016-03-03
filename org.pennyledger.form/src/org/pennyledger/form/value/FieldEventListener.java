package org.pennyledger.form.value;

import java.util.EventListener;

/**
 * @author Kevin Holloway
 * 
 */
public interface FieldEventListener extends EventListener {

  /** 
   * Return a string that describes the origin of this event listener.  This is used for debugging only.
   */
  public String getOrigin();
  
  /**
   * The field or default values have changed so they are now equal, or no
   * longer equal.
   * <p>
   * This method will only be called if there are no errors. If there are
   * errors, this method will be called when the error is cleared (reflecting
   * the equality of the field and default value).
   * 
   */
  public void valueEqualityChange(IFieldModel model);

  /**
   * The field or default <b>source</b> values have changed so they are now equal, or no
   * longer equal.
   * <p>
   * This method <b>will</b> be called regardless of whether there are errors or not. If there are
   * errors, equality will be determined by the source of the value, rather
   * than the value itself.
   * 
   */
  public void sourceEqualityChange(IFieldModel model, boolean isDataTrigger);

  /**
   * The field value has changed, either by user data entry or by the
   * application setting the field value.
   * <p>
   * This method will only be called if there are no errors. If there are
   * errors, this method will be called when the error is cleared.
   * 
   * @param model
   * @param value
   */
  public void valueChange(IFieldModel model);

  /**
   * An attempt was made to change the field value by the user, but no change
   * was made because the user entry was in error for some reason. This event
   * will be fired regardless of error status of the control.
   * 
   */
  public void sourceChange(IFieldModel model);

  /**
   * The reference applicability of the object model has changed.
   */
  public void equalityBasisChange (IFieldModel model);
  
}
