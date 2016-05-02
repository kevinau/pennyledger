package org.pennyledger.object.plan;

import java.util.List;


public interface IEntityPlan<T> extends IClassPlan<T> {

  public IObjectPlan[] getDataFields();
  
  public Class<?> getEntityClass();
  
  public String getEntityName();

  public IFieldPlan<?> getIdField();
  
//  public int getId(Object instance);
//  
//  public void setId(Object instance, int id);

  public IFieldPlan<?>[] getKeyFields();
  
  public List<IFieldPlan<?>[]> getUniqueConstraints();

  public IFieldPlan<?> getEntityLifeField();
  
//  public EntityLife getLife(Object instance);
//  
//  public void setLife(Object instance, EntityLife life);

  /** 
   * Returns the version field for this entity.  If the entity does not have
   * a version field, <code>null</code> is returned.
   * <p>
   * If an entity does not have a version field
   * @return
   */
  public IFieldPlan<?> getVersionField();
  
//  public Timestamp getVersion(Object instance);
//  
//  public void setVersion(Object instance, Timestamp version);

  @Override
  public <X extends IObjectPlan> X getMemberPlan (String name);
  
  @Override
  public <X extends IFieldPlan<?>> X getFieldPlan (String name);
  
}
