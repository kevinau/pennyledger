package org.pennyledger.form.plan;

import java.util.List;
import java.util.Set;


public interface IGroupPlan extends IContainerPlan {

  public IObjectPlan getMemberPlan (String name);

  public IObjectPlan[] getMemberPlans ();

  public List<IRuntimeModeProvider> getRuntimeModeProviders();

  public List<IRuntimeLabelProvider> getRuntimeLabelProviders();

  public List<IRuntimeDefaultProvider> getRuntimeDefaultProviders();

  public List<IRuntimeFactoryProvider> getRuntimeFactoryProviders();

  public List<IRuntimeTypeProvider> getRuntimeTypeProviders();

  public Set<IValidationMethod> getValidationMethods();

  public List<IRuntimeOccursProvider> getRuntimeOccursProviders();

  //public List<IRuntimeFactoryProvider> getRuntimeFactoryProviders();
  
}
