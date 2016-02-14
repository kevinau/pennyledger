package org.pennyledger.form.test.model;

import org.pennyledger.form.model.ComparisonBasis;
import org.pennyledger.form.model.EffectiveMode;
import org.pennyledger.form.model.EffectiveModeListener;
import org.pennyledger.form.model.FieldEventListener;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IObjectModel;
import org.pennyledger.util.UserEntryException;

public class EventCounter implements FieldEventListener, EffectiveModeListener {

  public int modeChangedCount;
  public EffectiveMode mode;
  public int compareEqualityChangeCount;
  public int compareShowingChangeCount;
  public boolean isCompareEqual;
  public boolean isCompareShowing;
  public int valueChangeCount;
  public Object valueValue;
  public String valueSource;
  public int errorClearedCount;
  public int errorNotedCount;
  public UserEntryException exception;
  public int sourceChangeCount;
  public String valueChangeSource;
  public int comparisonBasisChangeCount;
  public ComparisonBasis comparisonBasis;
  
  
  public void reset () {
    modeChangedCount = 0;
    mode = null;
    compareEqualityChangeCount = 0;
    isCompareEqual = false;
    compareShowingChangeCount = 0;
    isCompareShowing = false;
    valueChangeCount = 0;
    valueValue = null;
    valueSource = null;
    errorClearedCount = 0;
    errorNotedCount = 0;
    exception = null;
    sourceChangeCount = 0;
    valueChangeSource = null;
    comparisonBasisChangeCount = 0;
    comparisonBasis = null;
  }
  
  
  public void print () {
    if (modeChangedCount > 0) {
      System.out.println("Mode changed " + modeChangedCount + " " + mode);
    }
    if (compareEqualityChangeCount > 0) {
      System.out.println("Compare equality changes " + compareEqualityChangeCount + " " + isCompareEqual);
    }
    if (compareShowingChangeCount > 0) {
      System.out.println("Compare showing changes " + compareShowingChangeCount + " " + isCompareShowing);
    }
    if (valueChangeCount > 0) {
      System.out.println("Value changed " + valueChangeCount + " " + valueValue + " " + valueSource);
    }
    if (sourceChangeCount > 0) {
      System.out.println("Value change attempted " + sourceChangeCount + " " + valueChangeSource);
    }
    if (errorClearedCount > 0) {
      System.out.println("Error cleared " + errorClearedCount);
    }
    if (errorNotedCount > 0) {
      System.out.println("Error noted " + errorNotedCount + " " + exception);
    }
  }
  
  @Override
  public void modeChange(IObjectModel model) {
    this.modeChangedCount++;
    this.mode = model.getEffectiveMode();
  }

  @Override
  public void valueChange(IFieldModel model) {
    this.valueChangeCount++;
    this.valueValue = model.getValue();
    this.valueSource = model.getSource();
  }

  @Override
  public void errorCleared(IFieldModel model) {
    this.errorClearedCount++;
  }

  @Override
  public void errorNoted(IFieldModel model, UserEntryException ex) {
    this.errorNotedCount++;
    this.exception = ex;
  }

  @Override
  public void compareEqualityChange(IFieldModel model) {
    this.compareEqualityChangeCount++;
    this.isCompareEqual = model.isComparedValueEqual();
  }

  @Override
  public void compareShowingChange(IFieldModel model, boolean isSourceTrigger) {
    this.compareShowingChangeCount++;
    this.isCompareShowing = model.isComparedSourceEqual();
  }

  @Override
  public void sourceChange(IFieldModel model) {
    this.sourceChangeCount++;
    this.valueChangeSource = model.getSource();
  }


  @Override
  public void comparisonBasisChange(IFieldModel model) {
    this.comparisonBasisChangeCount++;
    this.comparisonBasis = model.getCompareBasis();
  }


  @Override
  public String getOrigin() {
    return "EventCounter";
  }

}
