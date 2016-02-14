package org.pennyledger.form.model;

import org.pennyledger.util.UserEntryException;


public class FieldEventAdapter extends EffectiveModeAdapter implements FieldEventListener {

  @Override
  public void compareEqualityChange(IFieldModel model) {
  }

  @Override
  public void compareShowingChange(IFieldModel model, boolean isSourceTrigger) {
  }

  @Override
  public void errorCleared(IFieldModel model) {
  }

  @Override
  public void errorNoted(IFieldModel model, UserEntryException ex) {
  }

  @Override
  public void valueChange(IFieldModel model) {
  }

  @Override
  public void sourceChange(IFieldModel model) {
  }

  @Override
  public void comparisonBasisChange(IFieldModel model) {
  }

  @Override
  public String getOrigin() {
    return "unknown";
  }

}
