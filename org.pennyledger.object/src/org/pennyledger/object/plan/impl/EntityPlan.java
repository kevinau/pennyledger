package org.pennyledger.object.plan.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Version;

import org.pennyledger.object.NaturalKey;
import org.pennyledger.object.UniqueConstraint;
import org.pennyledger.object.plan.IClassPlan;
import org.pennyledger.object.plan.IEntityPlan;
import org.pennyledger.object.plan.IItemPlan;
import org.pennyledger.object.plan.INodePlan;
import org.pennyledger.object.plan.PlanKind;
import org.pennyledger.object.type.IType;
import org.pennyledger.object.type.builtin.EntityLifeType;


public class EntityPlan<T> extends ClassPlan<T> implements IEntityPlan<T> {

  private final Class<T> entityClass;

  private String entityName;
  private IItemPlan<?> idFieldPlan;
  private IItemPlan<?> versionFieldPlan;
  private List<IItemPlan<?>[]> uniqueConstraints;
  private IItemPlan<?>[] keyFields;
  private INodePlan[] dataFields;
  private IItemPlan<?> lifeFieldPlan;

  
  public EntityPlan (Class<T> entityClass) {
    super (entityClass);
    this.entityClass = entityClass;
    this.entityName = entityClass.getSimpleName();
    buildEntityFields();
    buildUniqueConstraints();
  }


  @Override
  public Class<T> getEntityClass() {
    return entityClass;
  }


  @Override
  public String getEntityName() {
    return entityName;
  }


  @Override
  public IItemPlan<?> getIdField () {
    return idFieldPlan;
  }
  
//  @Override
//  public int getId (Object instance) {
//    return idFieldPlan.get(instance);
//  }
//  
//  @Override
//  public void setId (Object instance, int id) {
//    idFieldPlan.setValue(instance, id);
//  }
  
  @Override
  public IItemPlan<?> getVersionField () {
    return versionFieldPlan;
  }
  
//  @Override
//  public Timestamp getVersion (Object instance) {
//    return versionFieldPlan.get(instance);
//  }
//  
//  @Override
//  public void setVersion (Object instance, Timestamp version) {
//    if (versionFieldPlan != null) {
//      versionFieldPlan.setValue(instance, version);
//    }
//  }
  
  
  @Override
  public IItemPlan<?> getEntityLifeField () {
    return lifeFieldPlan;
  }
  
  
//  @Override
//  public EntityLife getLife (Object instance) {
//    if (lifeFieldPlan == null) {
//      return null;
//    } else {
//      return lifeFieldPlan.get(instance);
//    }
//  }
//  
//  @Override
//  public void setLife (Object instance, EntityLife life) {
//    if (lifeFieldPlan != null) {
//      lifeFieldPlan.setValue(instance, life);
//    }
//  }
  
  
  @Override
  public IItemPlan<?>[] getKeyFields () {
    return keyFields;
  }
  
  
  @Override
  public INodePlan[] getDataFields () {
    return dataFields;
  }

  
//  @Override
//  public List<IFieldPlan[]> getUniqueConstraints () {
//    return indexes;
//  }
  
  
  private void buildEntityFields () {
    List<IItemPlan<?>> memberPlans = new ArrayList<>();
    getAllFieldPlans(this, memberPlans);
    
    List<IItemPlan<?>> keyFields2 = new ArrayList<>();
    NaturalKey keyAnn = entityClass.getAnnotation(NaturalKey.class);
    if (keyAnn != null) {
      for (String keyName : keyAnn.value()) {
        IItemPlan<?> plan = getFieldPlan(memberPlans, keyName);
        keyFields2.add(plan);
      }
      keyFields = keyFields2.toArray(new IItemPlan[keyFields2.size()]);
    }

    List<IItemPlan<?>> dataFields2 = getDataFields(memberPlans, keyFields2);
    if (keyFields == null) {
      keyFields = new IItemPlan[1];
      keyFields[0] = dataFields2.get(0);
      dataFields2.remove(0);
    }
    dataFields = dataFields2.toArray(new IItemPlan[dataFields2.size()]);
  }

  
  private static IItemPlan<?> getFieldPlan (List<IItemPlan<?>> fieldPlans, String name) {
    for (IItemPlan<?> plan : fieldPlans) {
      if (plan.getName().equals(name)) {
        return plan;
      }
    }
    throw new IllegalArgumentException(name);
  }
  
  
  private static void getAllFieldPlans (IClassPlan<?> parent, List<IItemPlan<?>> fieldPlans) {
    for (INodePlan plan : parent.getMemberPlans()) {
      switch (plan.kind()) {
      case FIELD :
        fieldPlans.add((IItemPlan<?>)plan);
        break;
      case CLASS :
      case EMBEDDED :
        getAllFieldPlans((IClassPlan<?>)plan, fieldPlans);
        break;
      default :
        break;
      }
    }
  }
  
  
  private List<IItemPlan<?>> getDataFields (List<IItemPlan<?>> memberPlans, List<IItemPlan<?>> keyPlans) {
    List<IItemPlan<?>> dataFieldList = new ArrayList<>();
    IItemPlan<?> idFieldPlan2 = null;
    
    for (INodePlan member : memberPlans) {
      if (member.kind() == PlanKind.FIELD) {
        IItemPlan<?> fieldPlan = (IItemPlan<?>)member;
        Id idann = fieldPlan.getAnnotation(Id.class);
        if (idann != null) {
          idFieldPlan = fieldPlan;
          // Id fields are not key or data columns
          continue;
        }
        String name = fieldPlan.getName();
        if (name.equals("id")) {
          idFieldPlan2 = fieldPlan;
          // Fields named id are assumed to be id fields, not key or data columns
          continue;
        }
        
        Version vann = fieldPlan.getAnnotation(Version.class);
        if (vann != null) {
          versionFieldPlan = fieldPlan;
          // Version fields are not key or data columns
          continue;
        }
        
        IType<?> type = fieldPlan.getType();
        if (EntityLifeType.class.equals(type)) {
          // Entity life fields are not key or data columns
          // TODO The above statement may not be correct
          lifeFieldPlan = fieldPlan;
          continue;
        }
     
        // Is this field a key field?
        boolean isKeyField = keyPlans.contains(fieldPlan);
        if (!isKeyField) {
          dataFieldList.add(fieldPlan);
        }
      }
    }
    if (idFieldPlan == null) {
      idFieldPlan = idFieldPlan2;
    }
    return dataFieldList;
 }


  @Override
  public List<IItemPlan<?>[]> getUniqueConstraints() {
    return uniqueConstraints;
  }
  
  
  private void buildUniqueConstraints() {
    UniqueConstraint[] ucAnnx = entityClass.getAnnotationsByType(UniqueConstraint.class);
    uniqueConstraints = new ArrayList<>(ucAnnx.length);
    for (UniqueConstraint ucAnn : ucAnnx) {
      IItemPlan<?>[] fields = new IItemPlan[ucAnn.value().length];
      int i = 0;
      for (String name : ucAnn.value()) {
        fields[i] = getMemberPlan(name);
      }
      uniqueConstraints.add(fields);
    }
  }
}
