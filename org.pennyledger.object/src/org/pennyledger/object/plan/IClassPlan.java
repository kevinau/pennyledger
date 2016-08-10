package org.pennyledger.object.plan;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;


public interface IClassPlan<T> extends INodePlan {

  public Field getMemberField(String memberName);

  public <X extends INodePlan> X getNodePlan (String name);
  
  public <X extends IItemPlan<?>> X getLeafPlan (String name);
  
  public Object getMemberValue (Object instance, String name);

  public void setMemberValue (Object instance, String name, Object value);

  public T newInstance ();
  
  public INodePlan[] getMemberPlans ();

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
