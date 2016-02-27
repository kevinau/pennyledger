package org.pennyledger.form.value.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.pennyledger.form.reflect.ClassContainerReference;
import org.pennyledger.form.reflect.IContainerReference;
import org.pennyledger.form.value.IClassWrapper;
import org.pennyledger.form.value.IObjectWrapper;

public class ClassWrapper extends ObjectWrapper implements IClassWrapper {

  private final String name;
  private final DualAccessMap<String, IObjectWrapper> memberMap = new DualAccessMap<>();
  
  private Map<String, Object> priorValues = new HashMap<>();
  
  public ClassWrapper (IContainerReference container, String name) {
    super(container);
    this.name = name;
  }
  
  @Override
  public void setValue (Object instance) {
    super.setValue(instance);

    if (instance == null) {
      // Remove all member wrappers, and fire appropriate events
      memberMap.clear();
      // TODO fire events
    } else {
      Class<?> klass = instance.getClass();
      Field[] fields = klass.getDeclaredFields();
      for (Field field : fields) {
        if (field.isSynthetic()) {
          continue;
        }
        if ((field.getModifiers() & (Modifier.STATIC | Modifier.TRANSIENT | Modifier.VOLATILE | Modifier.FINAL)) != 0) {
          continue;
        }
        Object v1;
        try {
          field.setAccessible(true);
          v1 = field.get(instance);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
          throw new RuntimeException(ex);
        }
  
        String name = field.getName();
        IObjectWrapper memberWrapper = memberMap.get(name);
        if (memberWrapper == null) {
          IContainerReference container = new ClassContainerReference(instance, field);
          memberWrapper = wrapValue(container, field.getName(), field.getType(), v1);
          memberMap.put(name, memberWrapper);
          // TODO fire events
        } else {
          Object v0 = memberWrapper.getValue();
          if (v0 == null) {
            if (v1 == null) {
              // Do nothing.
            } else {
              // Create a new value, and fire value creation events
              Class<?> klass1 = v1.getClass();
              IContainerReference container = new ClassContainerReference(instance, field);
              memberWrapper = wrapValue(container, field.getName(), klass1, v1);
              memberMap.put(name, memberWrapper);
              // TODO fire events
            }
          } else {
            if (v1 == null) {
              // Remove this value, and fire value removal events
              IContainerReference container = new ClassContainerReference(instance, field);
              memberWrapper = wrapValue(container, field.getName(), field.getType(), null);
              memberMap.put(name, memberWrapper);
              // TODO fire events
            } else {
              if (v0.equals(v1)) {
                // Do nothing.
              } else {
                Class<?> klass0 = v0.getClass();
                Class<?> klass1 = v1.getClass();
                if (klass0.equals(klass1)) {
                  // The classes are the same, so simply update v0 with v1 field values.
                  memberWrapper.setValue(v1);
                } else {
                  // The classes are different, so: save all existing values, create a new
                  // object wrapper, and then set the values from what was saved.
                  collectPriorValues (name, memberWrapper);
                  IContainerReference container = new ClassContainerReference(instance, field);
                  memberWrapper = wrapValue(container, field.getName(), klass1, v1);
                  memberMap.put(name, memberWrapper);
                  reapplyPriorValues (name, memberWrapper);
                }
              }
            }
          }
        }
      }
    }
  }

  @Override
  public Map<String, IObjectWrapper> getMemberMap() {
    return memberMap;
  }
  
  @Override
  public IObjectWrapper getMember(String name) {
    return memberMap.get(name);
  }
  
  @Override
  public boolean isClass() {
    return true;
  }
  
  private void collectPriorValues (String path, IObjectWrapper wrapper) {
    if (wrapper.isField()) {
      priorValues.put(path, wrapper.getValue());
    } else if (wrapper.isClass()) {
      IClassWrapper classWrapper = (IClassWrapper)wrapper;
      Map<String, IObjectWrapper> memberMap = classWrapper.getMemberMap();
      for (Entry<String, IObjectWrapper> entry : memberMap.entrySet()) {
        collectPriorValues(path + "." + entry.getKey(), entry.getValue());
      }
    } else {
      throw new IllegalArgumentException(wrapper.getClass().toGenericString());
    }
  }
  
  private void reapplyPriorValues (String path, IObjectWrapper wrapper) {
    if (wrapper.isField()) {
      Object value = priorValues.get(path);
      wrapper.setValue(value);
    } else if (wrapper.isClass()) {
      IClassWrapper classWrapper = (IClassWrapper)wrapper;
      Map<String, IObjectWrapper> memberMap = classWrapper.getMemberMap();
      for (Entry<String, IObjectWrapper> entry : memberMap.entrySet()) {
        reapplyPriorValues(path + "." + entry.getKey(), entry.getValue());
      }
    } else {
      throw new IllegalArgumentException(wrapper.getClass().toGenericString());
    }
  }

  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("ClassWrapper(");
    sb.append(name);
    sb.append("{");
    boolean first = true;
    for (String key : memberMap.keySet()) {
      if (!first) {
        sb.append(",");
      }
      sb.append(key);
      first = false;
    }
    sb.append("})");
    return sb.toString();
  }

  @Override
  public List<IObjectWrapper> getChildren() {
    return memberMap.values();
  }
}
