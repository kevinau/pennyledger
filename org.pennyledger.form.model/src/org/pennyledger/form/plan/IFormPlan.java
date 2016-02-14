package org.pennyledger.form.plan;

import org.pennyledger.form.EntryMode;


public interface IFormPlan {

  public String getFormName();

  public String getIconName();

  public <X extends IObjectPlan> X getRootPlan();

  public EntryMode getEntryMode();

  public Class<?> getFormClass();

}
