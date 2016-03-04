package org.pennyledger.form.value.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.IRepeatingPlan;
import org.pennyledger.form.reflect.ArrayContainerReference;
import org.pennyledger.form.reflect.IContainerReference;
import org.pennyledger.form.value.IObjectModel;
import org.pennyledger.form.value.IRepeatingModel;

/**
 * An array of values.
 *
 */
public class ArrayModel extends ObjectModel implements IRepeatingModel{

  private final IContainerReference container;
  private final IRepeatingPlan plan;
  
  private List<IObjectModel> elemModels;
  
  public ArrayModel (IObjectModel parent, IContainerReference container, IRepeatingPlan plan) {
    super (parent);
    this.container = container;
    this.plan = plan;
  }
  
  @Override
  public void setValue (Object newValue) {
    Object oldValue = container.getValue();
    if (oldValue == null && newValue == null) {
      // No change of value.  Do nothing.
      return;
    }
    Object[] oldArray = (Object[])oldValue;
    Object[] newArray = (Object[])newValue;
    if (Arrays.equals(oldArray, newArray)) {
      // No change of value.  Do nothing.
    }
    container.setValue(newArray);
    syncToCurrentValue();
  }
  
  @Override
  public void syncToCurrentValue () {
    Object[] newArray = container.getValue();
    if (newArray == null) {
      dispose();
    } else {
      if (elemModels == null) {
        elemModels = new ArrayList<>(newArray.length);
      }
      int i = 0;
      int j = 0;
      while (i < elemModels.size() && j < newArray.length) {
        IObjectModel elemModel = elemModels.get(i);
        elemModel.setValue(newArray[j]);
        i++;
        j++;
      }
      while (i < elemModels.size()) {
        elemModels.remove(i).dispose(); 
      }
      while (j < newArray.length) {
        IContainerReference elemContainer = new ArrayContainerReference(newArray, j);
        IObjectModel elemModel = plan.getElementPlan().buildModel(this, elemContainer);
        elemModels.add(elemModel);
        j++;
      }
    }
  }
  
  @Override
  public void dispose () {
    if (elemModels != null) {
      for (IObjectModel elemModel : elemModels) {
        elemModel.dispose();
      }
      elemModels = null;
    }
  }
  
  @Override
  public boolean isRepeating() {
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
    sb.append("ArrayModel()");
    return sb.toString();
  }

  private static final List<IObjectModel> noChildren = Collections.emptyList();

  @Override
  public List<IObjectModel> getChildren() {
    if (elemModels == null) {
      return noChildren;
    } else {
      return elemModels;
    }
  }

  @Override
  public IObjectPlan getPlan() {
    return plan;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getValue() {
    return (T)container.getValue();
  }

  @Override
  public void dump(int level) {
    indent(level);
    System.out.println("RepeatingModel" + elemModels == null ? " null" : "[" + Integer.toString(elemModels.size()) + "]");
    if (elemModels != null) {
      for (IObjectModel elemModel : elemModels) {
        elemModel.dump(level + 1);
      }
    }
  }

  @Override
  public int getSize() {
    return elemModels.size();
  }

  @Override
  public void setRepeatingSize(int newSize) {
    Object[] currArray = container.getValue();
    Object[] newArray = Arrays.copyOf(currArray, newSize);
    for (int i = currArray.length; i < newSize; i++) {
      newArray[i] = plan.getElementPlan().newValue();
    }
    container.setValue(newArray);
    syncToCurrentValue();
  }

  @Override
  public IObjectModel getMember(int i) {
    return elemModels.get(i);
  }

  @Override
  public int indexOf(IObjectModel child) {
    return elemModels.indexOf(child);
  }

}
