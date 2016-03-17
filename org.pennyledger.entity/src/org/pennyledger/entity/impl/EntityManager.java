package org.pennyledger.entity.impl;

import org.pennyledger.db.IConnection;
import org.pennyledger.entity.DatabaseBuilder;
import org.pennyledger.entity.IEntityManager;
import org.pennyledger.form.plan.IEntityPlan;

public class EntityManager<T> implements IEntityManager<T> {

  private final IConnection conn;
  private final IEntityPlan<?> entityPlan;
  
  EntityManager (IConnection conn, IEntityPlan<?> entityPlan) {
    this.conn = conn;
    this.entityPlan = entityPlan;
  }
  
  @Override
  public void dropAndCreateTableSet() {
    DatabaseBuilder builder = new DatabaseBuilder(conn);
    builder.dropAndCreateTableSet(entityPlan);
  }

}
