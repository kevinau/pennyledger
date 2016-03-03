package org.pennyledger.form.value.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.pennyledger.form.plan.IClassPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.reflect.ClassContainerReference;
import org.pennyledger.form.reflect.IContainerReference;
import org.pennyledger.form.value.IClassWrapper;
import org.pennyledger.form.value.IObjectWrapper;
import org.pennyledger.util.DualAccessMap;

public class ClassWrapper extends ObjectWrapper implements IClassWrapper {

  private final IContainerReference container;
  private final IClassPlan<?> classPlan;
  
  private final DualAccessMap<String, IObjectWrapper> memberMap = new DualAccessMap<>();
  
  public ClassWrapper (IObjectWrapper parent, IContainerReference container, IClassPlan<?> classPlan) {
    super (parent);
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
    syncCurrentValue();
  }

  @Override
  public void syncCurrentValue () {
    Object currValue = container.getValue();
    if (currValue == null) {
      if (!classPlan.isOptional()) {
        throw new IllegalArgumentException("A non-null value is required for not-optional model");
      }
      // The new value can be null.
      // Get rid of the old value.
      dispose();
    } else {
      // New value is not null.    
      for (IObjectPlan memberPlan : classPlan.getMemberPlans()) {
        // Get the new value for this member
        String name = memberPlan.getName();
        Field memberField = classPlan.getMemberField(name);
        memberField.setAccessible(true);
        
        Object v1;
        try {
          v1 = memberField.get(currValue);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
          throw new RuntimeException(ex);
        }
        
        if (v1 == null && memberPlan.isOptional()) {
          IObjectWrapper memberModel = memberMap.remove(name);
          if (memberModel != null) {
            memberModel.dispose();
          }
        } else {
          IObjectWrapper memberModel = memberMap.get(name);
          if (memberModel == null) {
            IContainerReference childContainer = new ClassContainerReference(currValue, memberField);
            memberModel = memberPlan.buildModel(this, childContainer);
            memberMap.put(name, memberModel);
            memberModel.syncCurrentValue();
          } else {
            memberModel.setValue(v1);
          }
        }
      }
    }
  }

  @Override
  public Map<String, IObjectWrapper> getMemberMap() {
    return memberMap;
  }
  
  @Override
  public IObjectWrapper getMember(String name) {
    return memberMap.get(name);
  }
  
  @Override
  public boolean isClass() {
    return true;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("ClassWrapper(");
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
  public List<IObjectWrapper> getChildren() {
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
    for (IObjectWrapper member : memberMap.values()) {
      member.dispose();
    }
    memberMap.clear();
  }

  @Override
  public void dump(int level) {
    indent(level);
    System.out.println("ClassModel[" + memberMap.size() + "]");
    for (Map.Entry<String, IObjectWrapper> entry : memberMap.entrySet()) {
      indent(level + 1);
      System.out.println(entry.getKey() + ":");
      entry.getValue().dump(level + 2);
    }
  }

}
