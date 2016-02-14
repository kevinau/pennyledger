package org.pennyledger.form.test.model;

import org.pennyledger.form.model.EffectiveMode;
import org.pennyledger.form.model.EffectiveModeListener;
import org.pennyledger.form.model.IObjectModel;

public class ModeChangeCounter implements EffectiveModeListener {

  public int modeChangedCount;
  public EffectiveMode mode;
  
  
  public void reset () {
    modeChangedCount = 0;
    mode = null;
  }
  
  
  public void print () {
    if (modeChangedCount > 0) {
      System.out.println("Mode changed " + modeChangedCount + " " + mode);
    }
  }
  
  @Override
  public void modeChange(IObjectModel model) {
    this.modeChangedCount++;
    this.mode = model.getEffectiveMode();
  }

}
