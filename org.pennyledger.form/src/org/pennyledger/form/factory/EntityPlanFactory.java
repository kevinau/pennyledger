package org.pennyledger.form.factory;

import org.pennyledger.form.plan.IEntityPlan;
import org.pennyledger.form.plan.impl.EntityPlan;

public class EntityPlanFactory {

  public static <T> IEntityPlan<T> create (Class<T> klass) {
    return new EntityPlan<T>(klass);
  }
  
}
