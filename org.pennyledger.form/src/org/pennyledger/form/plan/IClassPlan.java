package org.pennyledger.form.plan;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;


public interface IClassPlan<T> extends IObjectPlan {

  public Field getMemberField(String memberName);

  public <X extends IObjectPlan> X getMemberPlan (String name);

  public IObjectPlan[] getMemberPlans ();

  public List<IRuntimeDefaultProvider> getRuntimeDefaultProviders();

  public List<IRuntimeFactoryProvider> getRuntimeFactoryProviders();

  public List<IRuntimeImplementationProvider> getRuntimeImplementationProviders();

  public List<IRuntimeLabelProvider> getRuntimeLabelProviders();

  public List<IRuntimeModeProvider> getRuntimeModeProviders();

  public List<IRuntimeOccursProvider> getRuntimeOccursProviders();

  public List<IRuntimeTypeProvider> getRuntimeTypeProviders();

  public Set<IValidationMethod> getValidationMethods();
  
  public Class<?> getSourceClass();

}
