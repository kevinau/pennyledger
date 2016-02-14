package org.pennyledger.form.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.pennyledger.form.plan.IListPlan;
import org.pennyledger.form.plan.IObjectPlan;

public class ListModel extends ContainerModel implements IListModel {
  
  private final IListPlan listPlan;
  private List<Object> elems;
  
  private List<IObjectModel> memberModels;
  

  public ListModel (IFormModel<?> ownerForm, IContainerModel parentModel, Object parentRef, IListPlan listPlan, Object instance) {
    super (ownerForm, parentModel, parentRef, listPlan.getPathName(), listPlan);
    this.listPlan = listPlan;
    setValue(instance);
  }

  
  @SuppressWarnings("unchecked")
  @Override
  public void setValue(Object instance) {
    IObjectPlan elemPlan = listPlan.getElementPlan();
    
    if (instance == null) {
      elems = new ArrayList<Object>();
    } else {
      if (instance instanceof List<?>) {
        elems = (List<Object>)instance;
      } else {
        throw new IllegalArgumentException("Instance value is not a List (" + instance.getClass() + ")");
      }
    }
    
    int n = elems.size();
    memberModels = new ArrayList<IObjectModel>(n);
    for (int i = 0; i < n; i++) {
      Object value = elems.get(i);
      memberModels.add(elemPlan.createModel(getOwnerForm(), this, i, "elem", value));
    }
    setUnderlyingValue(elems);
  }
  
  
  @Override
  public Object getValue () {
    return elems;
  }


  public void setListSize (int size) {
    IObjectPlan elemPlan = listPlan.getElementPlan();

    // Clean up any excess model elements
    for (int i = size; i < memberModels.size(); i++) {
      memberModels.get(i).detach();
    }
    
    int n = elems.size();
    while (n > size) {
      elems.remove(n - 1);
      memberModels.remove(n - 1);
      n--;
    }
    while (n < size) {
      // Add additional elements if necessary
      Object elemValue = elemPlan.newValue();
      elems.add(elemValue);
      memberModels.add(elemPlan.createModel(getOwnerForm(), this, n, "elem", elemValue));
      n++;
    }
  }
  
  
  @Override
  public int getListSize () {
    return memberModels.size();
  }
  
  
  @Override
  public void resetToInitial () {
    for (IObjectModel member : memberModels) {
      member.resetToInitial();
    }
  }
  

  @Override
  public Collection<IObjectModel> getChildren() {
    return memberModels;
  }

  
  @Override 
  public IObjectModel getMember (int index) {
    return memberModels.get(index);
  }

  
  @Override
  public List<IObjectModel> getAllMembers () {
    return memberModels;
  }
  
    
  public Object getFieldValue () {
    return elems;
  }
  
  
  @Override
  public void dump(int level) {
    for (int i = 0; i < level; i++) {
      System.out.print("  ");
    }
    if (getFieldValue() == null) {
      System.out.println(getPathName() + " = null");
    } else {
      System.out.println(getPathName() + " {");
    }
    for (IObjectModel model : getChildren()) {
      model.dump(level + 1);
    }
    for (int i = 0; i < level; i++) {
      System.out.print("  ");
    }
    System.out.println("}");
  }


  @Override
  public void fireInitialFieldEvents (FieldEventListener x, boolean isSourceTrigger) {
    for (IObjectModel model : getChildren()) {
      model.fireInitialFieldEvents(x, isSourceTrigger);
    }
  }


  @Override
  public Object getUnderlyingValue(Object parentRef) {
    int index = (Integer)parentRef;
    return elems.get(index);
  }


  @Override
  public void setUnderlyingValue(Object parentRef, Object value) {
    int index = (Integer)parentRef;
    elems.set(index, value);
  }

  
  @Override
  public String getLabel() {
    return listPlan.getStaticLabel();
  }

  
  @Override
  public boolean isSimpleList() {
    return listPlan.getElementPlan().isSolitary();
  }
  

  @Override
  public Object getKey(IObjectModel child) {
    int n = memberModels.indexOf(child);
    if (n != -1) {
      return new Integer(n);
    }
    throw new IllegalArgumentException(child.toString());
  }

}
