package org.pennyledger.object;

import java.util.HashMap;
import java.util.Map;

import org.pennyledger.object.plan.IClassPlan;
import org.pennyledger.object.plan.IEntityPlan;
import org.pennyledger.object.plan.impl.EntityPlan;

public class EntityPlanFactory {

  private static Map<Class<?>, IClassPlan<?>> planCache = new HashMap<>(20);
  
  @SuppressWarnings("unchecked")
  public static <T> IEntityPlan<T> getEntityPlan (Class<T> klass) {
    IEntityPlan<T> plan = (IEntityPlan<T>)planCache.get(klass);
    if (plan == null) {
      plan = new EntityPlan<T>(klass);
      planCache.put(klass, plan);
    }
    return plan;
  }
  
  
  @SuppressWarnings("unchecked")
  public static <T> IEntityPlan<T> getEntityPlan (T value) {
    return (IEntityPlan<T>)getEntityPlan(value.getClass());
  }
  
}
