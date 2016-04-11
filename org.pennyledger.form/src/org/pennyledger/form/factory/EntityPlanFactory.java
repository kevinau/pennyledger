package org.pennyledger.form.factory;

import java.util.HashMap;
import java.util.Map;

import org.pennyledger.form.plan.IEntityPlan;
import org.pennyledger.form.plan.impl.EntityPlan;

public class EntityPlanFactory {

  private static Map<Class<?>, IEntityPlan<?>> planCache = new HashMap<>(20);
  
  @SuppressWarnings("unchecked")
  public static <T> IEntityPlan<T> getPlan (Class<T> klass) {
    EntityPlan<T> plan = (EntityPlan<T>)planCache.get(klass);
    if (plan == null) {
      plan = new EntityPlan<T>(klass);
      planCache.put(klass, plan);
    }
    return plan;
  }
  
  
  @SuppressWarnings("unchecked")
  public static <T> IEntityPlan<T> getPlan (T value) {
    return (IEntityPlan<T>)getPlan(value.getClass());
  }
  
}
