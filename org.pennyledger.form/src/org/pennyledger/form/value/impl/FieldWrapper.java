package org.pennyledger.form.value.impl;

import java.util.Collections;
import java.util.List;

import org.pennyledger.form.plan.IFieldPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.reflect.IContainerReference;
import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IFieldWrapper;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectWrapper;

public class FieldWrapper extends ObjectWrapper implements IFieldWrapper {

  private static final List<IObjectWrapper> noChildren = Collections.emptyList();

  private final IContainerReference container;
  private final IFieldPlan plan;
  
  public FieldWrapper(IObjectWrapper parent, IContainerReference container, IFieldPlan plan) {
    super(parent);
    this.container = container;
    this.plan = plan;
  }

  @Override
  public boolean isField () {
    return true;
  }

  @Override
  public void walkObjectWrappers (IObjectVisitable x) {
    x.visit(this);
  }

  @Override
  public String toString() {
    return "FieldModel(" + plan + ")";
  }
  
  @Override
  public List<IObjectWrapper> getChildren() {
    return noChildren;
  }

  @Override
  public void walkFieldWrappers(IFieldVisitable x) {
    x.visit(this);
  }

  @Override
  public IObjectPlan getPlan() {
    return plan;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getValue () {
    return (T)container.getValue();
  }
  
  @Override
  public void setValue(Object value) {
    container.setValue(value);
    // TODO fire events, etc.
  }

  @Override
  public void dispose() {
    // Remove any field not-equal events.
    // TODO Auto-generated method stub
  }

  @Override
  public void dump(int level) {
    indent(level);
    System.out.println("FieldModel[" + plan + "]");
  }

  @Override
  public void syncCurrentValue() {
    // Do nothing.
  }

}
