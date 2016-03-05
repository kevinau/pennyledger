package org.pennyledger.form.plan;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import org.pennyledger.form.reflect.IContainerReference;
import org.pennyledger.form.value.IForm;
import org.pennyledger.form.value.IObjectModel;


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

}
