package org.pennyledger.form.value.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.pennyledger.form.plan.IInterfacePlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.impl.ClassPlan;
import org.pennyledger.form.reflect.IContainerReference;
import org.pennyledger.form.value.IInterfaceModel;
import org.pennyledger.form.value.IObjectModel;

public class InterfaceModel extends ObjectModel implements IInterfaceModel{

  private final IContainerReference container;
  private final IInterfacePlan plan;
  
  private IObjectPlan implPlan;
  private Class<?> implClass;
  private IObjectModel implModel;
  
//  private Map<String, Object> priorValues = new HashMap<>();
  
  public InterfaceModel (IObjectModel parent, IContainerReference container, IInterfacePlan plan) {
    super (parent);
    this.container = container;
    this.plan = plan;
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
      if (!plan.isOptional()) {
        throw new IllegalArgumentException("A non-null value is required for not-optional model");
      }
      // The new value can be null.
      // Get rid of the old value.
      dispose();
      implModel = null;
    } else {
      // New value is not null.
      if (implModel == null) {
        implClass = currValue.getClass();
        implPlan = ClassPlan.buildObjectPlan(implClass);
        implModel = implPlan.buildModel(this, container);
        implModel.setValue(currValue);
      } else {
        // We have new and old values that are both not null.
        Class<?> newClass = currValue.getClass();
        if (implClass.equals(newClass)) {
          // The classes are the same, so simply update the existing model with the new value.
          implModel.setValue(currValue);
        } else {
          // The classes are different, so: save all existing values, create a new
          // object model, and then set the values from what was saved.
          //collectPriorValues ("", implModel);
          //IContainerReference container = new ClassContainerReference(instance, field);
          dispose();

          // Build a new plan and model, and then set the new value on that.
          implClass = newClass;
          implPlan = ClassPlan.buildObjectPlan(newClass);
          implModel = implPlan.buildModel(this, container);
          //reapplyPriorValues ("", implModel);
          implModel.setValue(currValue);
        }
      }
    }
  }
  
  @Override
  public void dispose () {
    if (implModel != null) {
      implModel.dispose();
      implModel = null;
    }
  }
  
  @Override
  public IObjectModel getImplementationModel() {
    return implModel;
  }
  
  @Override
  public boolean isInterface() {
    return true;
  }
  
//  private void collectPriorValues (String path, IObjectModel model) {
//    if (model.isField()) {
//      priorValues.put(path, model.getValue());
//    } else if (model.isClass()) {
//      IClassModel classModel = (IClassModel)model;
//      Map<String, IObjectModel> memberMap = classModel.getMemberMap();
//      for (Entry<String, IObjectModel> entry : memberMap.entrySet()) {
//        collectPriorValues(path + "." + entry.getKey(), entry.getValue());
//      }
//    } else {
//      throw new IllegalArgumentException(model.getClass().toGenericString());
//    }
//  }
//  
//  private void reapplyPriorValues (String path, IObjectModel model) {
//    if (model.isField()) {
//      Object value = priorValues.get(path);
//      model.setValue(value);
//    } else if (model.isClass()) {
//      IClassModel classModel = (IClassModel)model;
//      Map<String, IObjectModel> memberMap = classModel.getMemberMap();
//      for (Entry<String, IObjectModel> entry : memberMap.entrySet()) {
//        reapplyPriorValues(path + "." + entry.getKey(), entry.getValue());
//      }
//    } else {
//      throw new IllegalArgumentException(model.getClass().toGenericString());
//    }
//  }

  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("InterfaceModel()");
    return sb.toString();
  }

  private static final List<IObjectModel> noChildren = Collections.emptyList();

  @Override
  public List<IObjectModel> getChildren() {
    if (implModel == null) {
      return noChildren;
    } else {
      return Collections.singletonList(implModel);
    }
  }

  @Override
  public IObjectPlan getPlan() {
    return implPlan;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getValue() {
    return (T)container.getValue();
  }

  @Override
  public void dump(int level) {
    indent(level);
    System.out.println("InterfaceModel(" + implPlan + ")");
    if (implModel != null) {
      implModel.dump(level + 1);
    }
  }

}
