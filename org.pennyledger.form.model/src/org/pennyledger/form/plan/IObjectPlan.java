package org.pennyledger.form.plan;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.model.IContainerModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IObjectModel;


public interface IObjectPlan {

  public String getStaticLabel();
  
  public EntryMode getStaticMode();

  public void dump();

  public Object newValue ();
  
  public String getPathName();
  
  public IObjectModel createModel(IFormModel<?> ownerForm, IContainerModel parentModel, Object parentRef, String pathName, Object instance);

  public Object getParentRef();

  public void setStaticMode(EntryMode cm);

  public void setStaticLabel(String lx);

  public boolean isSolitary();
  
  public ObjectKind kind();
  
}
