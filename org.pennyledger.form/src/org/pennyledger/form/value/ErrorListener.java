package org.pennyledger.form.value;

import java.util.EventListener;

import org.pennyledger.util.UserEntryException;


/**
 * @author Kevin Holloway
 * 
 */
public interface ErrorListener extends EventListener {

  /**
   * The control has left the error state--all previously noted errors have been
   * cleared.
   * <p>
   * This event is only fired when all previously noted errors have been
   * cleared. It a control is in the error state because of 2 noted errors, when
   * one is cleared this event does NOT fire. It would fire when the second of
   * the 2 noted errors has been cleared.
   * <p>
   * If the field, default or reference values have changed since the control
   * entered the error state, this event will be followed by the appropriate
   * equality shown or equality changed events.
   * 
   * @param ev
   */
  public void errorCleared(IObjectWrapper model);

  /**
   * The control has gone into an error state. This is either because the user
   * entry is wrong, or some other validation method has noted an error on this
   * control.
   * <p>
   * This event is only fired when the control first goes into the error state.
   * Subsequent error notifications will not cause this event to fire again.
   * 
   */
  public void errorNoted(IObjectWrapper model, UserEntryException ex);

}
