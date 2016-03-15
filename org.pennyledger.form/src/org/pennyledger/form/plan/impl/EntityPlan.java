package org.pennyledger.form.plan.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Version;

import org.pennyledger.form.NaturalKey;
import org.pennyledger.form.UniqueConstraint;
import org.pennyledger.form.UniqueConstraints;
import org.pennyledger.form.plan.IClassPlan;
import org.pennyledger.form.plan.IEntityPlan;
import org.pennyledger.form.plan.IFieldPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.PlanKind;
import org.pennyledger.form.type.IType;
import org.pennyledger.form.type.builtin.EntityLifeType;


public class EntityPlan<T> extends ClassPlan<T> implements IEntityPlan<T> {

  private final Class<T> entityClass;

  private String entityName;
  private IFieldPlan idFieldPlan;
  private IFieldPlan versionFieldPlan;
  private List<IFieldPlan[]> uniqueConstraints;
  private IFieldPlan[] keyFields;
  private IObjectPlan[] dataFields;
  private IFieldPlan lifeFieldPlan;

  
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
  public IFieldPlan getIdField () {
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
  public IFieldPlan getVersionField () {
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
  public IFieldPlan getEntityLifeField () {
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
  public IFieldPlan[] getKeyFields () {
    return keyFields;
  }
  
  
  @Override
  public IObjectPlan[] getDataFields () {
    return dataFields;
  }

  
//  @Override
//  public List<IFieldPlan[]> getUniqueConstraints () {
//    return indexes;
//  }
  
  
  private void buildEntityFields () {
    List<IFieldPlan> memberPlans = new ArrayList<>();
    getAllFieldPlans(this, memberPlans);
    
    List<IFieldPlan> keyFields2 = new ArrayList<>();
    NaturalKey keyAnn = entityClass.getAnnotation(NaturalKey.class);
    if (keyAnn != null) {
      for (String keyName : keyAnn.value()) {
        IFieldPlan plan = getFieldPlan(memberPlans, keyName);
        keyFields2.add(plan);
      }
      keyFields = keyFields2.toArray(new IFieldPlan[keyFields2.size()]);
    }

    List<IFieldPlan> dataFields2 = getDataFields(memberPlans, keyFields2);
    if (keyFields == null) {
      keyFields = new IFieldPlan[1];
      keyFields[0] = dataFields2.get(0);
      dataFields2.remove(0);
    }
    dataFields = dataFields2.toArray(new IFieldPlan[dataFields2.size()]);
  }

  
  private static IFieldPlan getFieldPlan (List<IFieldPlan> fieldPlans, String name) {
    for (IFieldPlan plan : fieldPlans) {
      if (plan.getName().equals(name)) {
        return plan;
      }
    }
    throw new IllegalArgumentException(name);
  }
  
  
  private static void getAllFieldPlans (IClassPlan<?> parent, List<IFieldPlan> fieldPlans) {
    for (IObjectPlan plan : parent.getMemberPlans()) {
      switch (plan.kind()) {
      case FIELD :
        fieldPlans.add((IFieldPlan)plan);
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
  
  
  private List<IFieldPlan> getDataFields (List<IFieldPlan> memberPlans, List<IFieldPlan> keyPlans) {
    List<IFieldPlan> dataFieldList = new ArrayList<>();
    IFieldPlan idFieldPlan2 = null;
    
    for (IObjectPlan member : memberPlans) {
      if (member.kind() == PlanKind.FIELD) {
        IFieldPlan fieldPlan = (IFieldPlan)member;
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
  public List<IFieldPlan[]> getUniqueConstraints() {
    return uniqueConstraints;
  }
  
  
  private void buildUniqueConstraints() {
    UniqueConstraints ucsAnn = entityClass.getAnnotation(UniqueConstraints.class);
    if (ucsAnn != null) {
      uniqueConstraints = new ArrayList<>(ucsAnn.value().length);
      for (UniqueConstraint ucAnn : ucsAnn.value()) {
        IFieldPlan[] fields = new IFieldPlan[ucAnn.value().length];
        int i = 0;
        for (String name : ucAnn.value()) {
          fields[i] = getMemberPlan(name);
        }
        uniqueConstraints.add(fields);
      }
    }
  }
}
