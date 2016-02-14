package org.pennyledger.form.plan.impl;

import java.util.ArrayList;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.model.IContainerModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IObjectModel;
import org.pennyledger.form.model.ListModel;
import org.pennyledger.form.plan.IListPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.ObjectKind;

public class ListPlan extends ObjectPlan implements IListPlan {

  private IObjectPlan elemPlan;

  private Object staticFactoryValue;
  
  
  public ListPlan (Object parentRef, String pathName) {
    super (parentRef, pathName, EntryMode.UNSPECIFIED);
  }
  

  @Override
  public IObjectPlan getElementPlan () {
    return elemPlan;
  }

  
  public void setElementPlan (IObjectPlan elemPlan) {
    this.elemPlan = elemPlan;
  }
  
  
  public Object getStaticFactoryValue () {
    return staticFactoryValue;
  }
  
  
  void setStaticFactoryValue (Object staticFactoryValue) {
    this.staticFactoryValue = staticFactoryValue;
  }
  
  
  @Override 
  public Object newValue () {
    return new ArrayList<Object>();
  }
  
  
  @Override
  protected void dump (int level) {
    for (int i = 0; i < level; i++) {
      System.out.print("  ");
    }
    System.out.println("List: " + getStaticLabel() + "' " + getStaticMode());
    ((ObjectPlan)elemPlan).dump(level + 1);
  }


  @Override
  public IObjectModel createModel(IFormModel<?> ownerForm, IContainerModel parentModel, Object parentRef, String pathName, Object instance) {
    return new ListModel(ownerForm, parentModel, parentRef, this, instance);
  }
  
  
  @Override
  public boolean isSolitary () {
    return false;
  }

  

  @Override
  public ObjectKind kind() {
    return ObjectKind.LIST;
  }

}
