package org.pennyledger.entity.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.plan.IClassPlan;
import org.pennyledger.form.plan.IFieldPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.IRuntimeDefaultProvider;
import org.pennyledger.form.plan.IRuntimeFactoryProvider;
import org.pennyledger.form.plan.IRuntimeImplementationProvider;
import org.pennyledger.form.plan.IRuntimeLabelProvider;
import org.pennyledger.form.plan.IRuntimeModeProvider;
import org.pennyledger.form.plan.IRuntimeOccursProvider;
import org.pennyledger.form.plan.IRuntimeTypeProvider;
import org.pennyledger.form.plan.IValidationMethod;
import org.pennyledger.form.plan.PlanKind;

public class ClassPlanAdapter implements IClassPlan<Object> {

  private final IClassPlan<?> classPlan;
  
  ClassPlanAdapter (IClassPlan<?> classPlan) {
    this.classPlan = classPlan;
  }

  @Override
  public Class<?> getSourceClass() {
    return classPlan.getSourceClass();
  }
  
  @Override
  public void dump(int level) {
    classPlan.dump(level);
  }

  @Override
  public String getDeclaredLabel() {
    return classPlan.getDeclaredLabel();
  }

  @Override
  public EntryMode getDeclaredMode() {
    return classPlan.getDeclaredMode();
  }

  @Override
  public String getName() {
    return classPlan.getName();
  }

  @Override
  public IObjectPlan getParent() {
    return classPlan.getParent();
  }

  @Override
  public PlanKind kind() {
    return classPlan.kind();
  }

  @Override
  public Field getMemberField(String memberName) {
    return classPlan.getMemberField(memberName);
  }

  @Override
  public <X extends IObjectPlan> X getMemberPlan(String name) {
    return classPlan.getMemberPlan(name);
  }

  @Override
  public IObjectPlan[] getMemberPlans() {
    return classPlan.getMemberPlans();
  }

  @Override
  public List<IRuntimeDefaultProvider> getRuntimeDefaultProviders() {
    return classPlan.getRuntimeDefaultProviders();
  }

  @Override
  public List<IRuntimeFactoryProvider> getRuntimeFactoryProviders() {
    return classPlan.getRuntimeFactoryProviders();
  }

  @Override
  public List<IRuntimeImplementationProvider> getRuntimeImplementationProviders() {
    return classPlan.getRuntimeImplementationProviders();
  }

  @Override
  public List<IRuntimeLabelProvider> getRuntimeLabelProviders() {
    return classPlan.getRuntimeLabelProviders();
  }

  @Override
  public List<IRuntimeModeProvider> getRuntimeModeProviders() {
    return classPlan.getRuntimeModeProviders();
  }

  @Override
  public List<IRuntimeOccursProvider> getRuntimeOccursProviders() {
    return classPlan.getRuntimeOccursProviders();
  }

  @Override
  public List<IRuntimeTypeProvider> getRuntimeTypeProviders() {
    return classPlan.getRuntimeTypeProviders();
  }

  @Override
  public Set<IValidationMethod> getValidationMethods() {
    return classPlan.getValidationMethods();
  }
  
  @Override
  public void accumulateFieldPlans(List<IFieldPlan> fieldPlans) {
    classPlan.accumulateFieldPlans(fieldPlans);
  }
}

