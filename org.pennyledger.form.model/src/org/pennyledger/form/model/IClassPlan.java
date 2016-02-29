package org.pennyledger.form.model;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;


public interface IClassPlan<T> extends IObjectPlan {

  public <X extends IObjectPlan> X getMemberPlan (String name);

  public IObjectPlan[] getMemberPlans ();

  public Field getMemberField(String memberName);

//  public List<IRuntimeModeProvider> getRuntimeModeProviders();
//
//  public List<IRuntimeImplementationProvider> getRuntimeImplementationProviders();
//
//  public List<IRuntimeLabelProvider> getRuntimeLabelProviders();
//
//  public List<IRuntimeDefaultProvider> getRuntimeDefaultProviders();
//
//  public List<IRuntimeFactoryProvider> getRuntimeFactoryProviders();
//
//  public List<IRuntimeTypeProvider> getRuntimeTypeProviders();
//
//  public Set<IValidationMethod> getValidationMethods();
//
//  public List<IRuntimeOccursProvider> getRuntimeOccursProviders();

}
