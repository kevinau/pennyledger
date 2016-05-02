package org.pennyledger.object.plan.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.pennyledger.object.EntryMode;
import org.pennyledger.object.type.IType;

public interface WalkPlanTarget {

  public void field (String name, IType<?> type, Field field, EntryMode entryMode, boolean optional);
  
  public void reference (String name, Class<?> referencedClass, EntryMode entryMode, boolean optional);
  
  public void embedded (String name, Class<?> referencedClass, EntryMode entryMode);
  
  public void array (String name, Class<?> arrayClass, int dimension, EntryMode entryMode);
  
  public void list (String name, Type listType, Field field, int dimension, EntryMode entryMode);
  
}
