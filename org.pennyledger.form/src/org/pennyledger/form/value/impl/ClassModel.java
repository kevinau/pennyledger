package org.pennyledger.form.value.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.pennyledger.form.plan.IClassPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.reflect.ClassContainerReference;
import org.pennyledger.form.reflect.IContainerReference;
import org.pennyledger.form.value.IClassModel;
import org.pennyledger.form.value.IForm;
import org.pennyledger.form.value.IObjectModel;
import org.pennyledger.util.DualAccessMap;

public class ClassModel extends ObjectModel implements IClassModel {

  private final IContainerReference container;
  private final IClassPlan<?> classPlan;
  
  private final DualAccessMap<String, IObjectModel> memberMap = new DualAccessMap<>();
  
  public ClassModel (IForm<?> form, IObjectModel parent, IContainerReference container, IClassPlan<?> classPlan) {
    super (form, parent);
    this.container = container;
    this.classPlan = classPlan;
  }
  
  @Override
  public void setValue (Object newValue) {
    Object oldValue = container.getValue();
    if (oldValue == null ? newValue == null : oldValue.equals(newValue)) {
      // No change of value.  Do nothing.
      return;
    }
    container.setValue(newValue);
    syncToCurrentValue();
  }

  @Override
  public void syncToCurrentValue () {
    Object currValue = container.getValue();
    if (currValue == null) {
      // Get rid of the old value.
      dispose();
    } else {
      // New value is not null.    
      for (IObjectPlan memberPlan : classPlan.getMemberPlans()) {
        // Get the new value for this member
        String name = memberPlan.getName();
        Field memberField = classPlan.getMemberField(name);
        
        Object v1;
        try {
          memberField.setAccessible(true);
          v1 = memberField.get(currValue);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
          throw new RuntimeException(ex);
        }
        
//        if (v1 == null) {
//          IObjectModel memberModel = memberMap.remove(name);
//          if (memberModel != null) {
//            memberModel.dispose();
//          }
//        } else {
          IObjectModel memberModel = memberMap.get(name);
          if (memberModel == null) {
            IContainerReference childContainer = new ClassContainerReference(currValue, memberField);
            memberModel = memberPlan.buildModel(getForm(), this, childContainer);
            memberMap.put(name, memberModel);
            getForm().fireModelAdded(this, memberModel);
            memberModel.syncToCurrentValue();
          } else {
            memberModel.setValue(v1);
          }
//        }
      }
    }
  }

  @Override
  public Map<String, IObjectModel> getMemberMap() {
    return memberMap;
  }
  
  @Override
  public IObjectModel getMember(String name) {
    return memberMap.get(name);
  }
  
  @Override
  public boolean isClass() {
    return true;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("ClassModel(");
    sb.append("{");
    boolean first = true;
    for (String key : memberMap.keySet()) {
      if (!first) {
        sb.append(",");
      }
      sb.append(key);
      first = false;
    }
    sb.append("})");
    return sb.toString();
  }

  @Override
  public List<IObjectModel> getChildren() {
    return memberMap.values();
  }

  @Override
  public <T> T getValue() {
    return container.getValue();
  }

  @Override
  public IClassPlan<?> getPlan() {
    return classPlan;
  }

  @Override
  public void dispose() {
    for (IObjectModel member : memberMap.values()) {
      member.dispose();
      getForm().fireModelRemoved(this, member);
    }
    memberMap.clear();
  }

  @Override
  public void dump(int level) {
    indent(level);
    System.out.println("ClassModel[" + memberMap.size() + "]");
    for (Map.Entry<String, IObjectModel> entry : memberMap.entrySet()) {
      indent(level + 1);
      System.out.println(entry.getKey() + ":");
      entry.getValue().dump(level + 2);
    }
  }

}
