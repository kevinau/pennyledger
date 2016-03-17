package org.pennyledger.entity;

import org.pennyledger.form.plan.IEntityPlan;

public interface IEntityManagerFactory {

  public <T> IEntityManager<T> createEntityManager (IEntityPlan<T> plan);

}
