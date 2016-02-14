package org.pennyledger.form.test.model;

import org.pennyledger.form.EntryMode;
import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.model.EffectiveMode;


public class EffectiveModeTest {

  private static EntryMode[] rawModes = new EntryMode[] {
    EntryMode.UNSPECIFIED, EntryMode.ENTRY, EntryMode.VIEW, EntryMode.NA,
  };
  
  private static EffectiveMode[] parentNA = new EffectiveMode[] {
    EffectiveMode.NA, EffectiveMode.NA, EffectiveMode.NA, EffectiveMode.NA, 
  };
  
  private static EffectiveMode[] parentVIEW = new EffectiveMode[] {
    EffectiveMode.VIEW, EffectiveMode.VIEW, EffectiveMode.VIEW, EffectiveMode.NA, 
  };
  
  private static EffectiveMode[] parentENTRY = new EffectiveMode[] {
    EffectiveMode.ENTRY, EffectiveMode.ENTRY, EffectiveMode.VIEW, EffectiveMode.NA, 
  };
  
  
  @Test
  public void parentNA () {
    for (int i = 0; i < rawModes.length; i++) {
      EntryMode rawMode = rawModes[i];
      EffectiveMode effMode = EffectiveMode.NA.getEffective(rawMode);
      Assert.assertEquals(parentNA[i], effMode);
    }
  }
  
  @Test
  public void parentView () {
    for (int i = 0; i < rawModes.length; i++) {
      EntryMode rawMode = rawModes[i];
      EffectiveMode effMode = EffectiveMode.VIEW.getEffective(rawMode);
      Assert.assertEquals(parentVIEW[i], effMode);
    }
  }
  
  @Test
  public void parentEntry () {
    for (int i = 0; i < rawModes.length; i++) {
      EntryMode rawMode = rawModes[i];
      EffectiveMode effMode = EffectiveMode.ENTRY.getEffective(rawMode);
      Assert.assertEquals(parentENTRY[i], effMode);
    }
  }
  
}
