package org.pennyledger.form.plan;

import java.util.List;


public interface IEntityPlan<T> extends IClassPlan<T> {

  public IObjectPlan[] getDataFields();
  
  public Class<T> getEntityClass();
  
  public String getEntityName();

  public IFieldPlan getIdField();
  
//  public int getId(Object instance);
//  
//  public void setId(Object instance, int id);

  public IFieldPlan[] getKeyFields();
  
  public List<IFieldPlan[]> getUniqueConstraints();

  public IFieldPlan getLifeField();
  
//  public EntityLife getLife(Object instance);
//  
//  public void setLife(Object instance, EntityLife life);

  public IFieldPlan getVersionField();
  
//  public Timestamp getVersion(Object instance);
//  
//  public void setVersion(Object instance, Timestamp version);

  @Override
  public <X extends IObjectPlan> X getMemberPlan (String name);
  
}
