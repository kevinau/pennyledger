package org.pennyledger.form.model;

import org.pennyledger.form.EntryMode;

public enum EffectiveMode {

  ENTRY (true),
  VIEW (true),
  NA (false);

  private final boolean allowEntryEvents;
  
  private EffectiveMode (boolean allowEntryEvents) {
    this.allowEntryEvents = allowEntryEvents;
  }
  
  
  public boolean allowEntryEvents () {
    return allowEntryEvents;
  }
  
  
  public EffectiveMode getEffective (EntryMode entryMode) {
    EffectiveMode parentMode = this;
    switch (parentMode) {
    case ENTRY :
      if (entryMode == EntryMode.NA) {
        return NA;
      } else if (entryMode == EntryMode.VIEW) {
        return VIEW;
      } else {
        return ENTRY;
      }
    case VIEW :
      if (entryMode == EntryMode.NA) {
        return NA;
      } else {
        return VIEW;
      }
    default :
      return NA;
    }
  }
  

  public static EffectiveMode toEffective (EntryMode entryMode) {
    if (entryMode == EntryMode.VIEW) {
      return EffectiveMode.VIEW;
    } else if (entryMode == EntryMode.NA) {
      return EffectiveMode.NA;
    } else {
      return EffectiveMode.ENTRY;
    }
  }

}
