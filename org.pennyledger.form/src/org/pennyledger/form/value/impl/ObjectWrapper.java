package org.pennyledger.form.value.impl;

import java.util.ArrayList;
import java.util.List;

import org.pennyledger.form.path.StepPath;
import org.pennyledger.form.path.Trail;
import org.pennyledger.form.path.parser.ParseException;
import org.pennyledger.form.path.parser.SimplePathParser;
import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IFieldWrapper;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectWrapper;

public abstract class ObjectWrapper implements IObjectWrapper {

  
  private static void getAllObjectWrappers(IObjectWrapper parent, List<IObjectWrapper> objList) {
    parent.walkObjectWrappers(new IObjectVisitable() {
      @Override
      public void visit(IObjectWrapper wrapper) {
        objList.add(wrapper);
      }
    });
  }

  
  private static void getAllFieldWrappers(IObjectWrapper parent, List<IFieldWrapper> fieldList) {
    parent.walkFieldWrappers(new IFieldVisitable() {
      @Override
      public void visit(IFieldWrapper wrapper) {
        fieldList.add(wrapper);
      }
    });
  }

  
  @Override
  public void walkObjectWrappers (String pathExpr, IObjectVisitable x) {
    StepPath path;
    try {
      path = new SimplePathParser(pathExpr).parse();
    } catch (ParseException ex) {
      throw new RuntimeException(ex);
    }
    path.matches(this, new Trail(this), x);
  }

  
  @Override
  public void walkFieldWrappers (String pathExpr, IFieldVisitable x) {
    StepPath path;
    try {
      path = new SimplePathParser(pathExpr).parse();
    } catch (ParseException ex) {
      throw new RuntimeException(ex);
    }
    path.matches(this, x);
  }

  
//  public static IObjectWrapper wrapValue(IContainerReference container, String name, Field field, Type type, Object value) {
//    IObjectWrapper wrapper;
//    if (klass.isArray()) {
//      Class<?> elemType = klass.getComponentType();
//      wrapper = new ArrayWrapper(container, name, elemType, value);
//    } else if (type instanceof ParameterizedType) {
//      ParameterizedType ptype = (ParameterizedType)type;
//      Type type1 = ptype.getRawType();
//      if (type1.equals(List.class)) {
//        Type[] typeArgs = ptype.getActualTypeArguments();
//        if (typeArgs.length != 1) {
//          throw new IllegalArgumentException("List must have one, and only one, type parameter");
//        }
//        Type type2 = typeArgs[0];
//        objPlan = listPlanDetail(parent, field, parentClass, name, type2, entryMode, arraySizes, lastEntryField);
//      } else {
//        throw new IllegalArgumentException("Parameterized type that is not a List");
//      }
//      
//    }
//    if (String.class.isAssignableFrom(klass) || Integer.class.isAssignableFrom(klass)) {
//      wrapper = new FieldWrapper(container, name, klass);
//    } else {
//      wrapper = new ClassWrapper(container, name);
//    }
//    wrapper.setValue(value);
//    return wrapper;
//  }

  private final IObjectWrapper parent;

  protected ObjectWrapper(IObjectWrapper parent) {
    this.parent = parent;
  }
  
//  protected ObjectWrapper(IObjectWrapper parent, IContainerReference container) {
//    this.parent = parent;
//    this.container = container;
//  }

  @Override
  public List<IObjectWrapper> getObjectWrappers() {
    List<IObjectWrapper> wrapperList = new ArrayList<>();
    getAllObjectWrappers(this, wrapperList);
    return wrapperList;
  }

  @Override
  public boolean isClass() {
    return false;
  }

  @Override
  public IObjectWrapper getParent() {
    return parent;
  }
  
  @Override
  public boolean isField() {
    return false;
  }

  @Override
  public boolean isInterface() {
    return false;
  }

  @Override
  public IObjectWrapper getObjectWrapper(String pathExpr) {
    List<IObjectWrapper> found = getObjectWrappers(pathExpr);
    switch (found.size()) {
    case 0 :
      throw new IllegalArgumentException("'" + pathExpr + "' does not match any IObjectWrapper");
    case 1 :
      return found.get(0);
    default :
      throw new IllegalArgumentException("'" + pathExpr + "' matches more than one IObjectWrapper");
    }
  }

  @Override
  public List<IObjectWrapper> getObjectWrappers(String pathExpr) {
    StepPath path;
    try {
      path = new SimplePathParser(pathExpr).parse();
    } catch (ParseException ex) {
      throw new RuntimeException(ex);
    }
    final List<IObjectWrapper> found = new ArrayList<>();
    Trail trail = new Trail(this);
    path.matches(this, trail, new IObjectVisitable() {
      @Override
      public void visit(IObjectWrapper wrapper) {
        if (!found.contains(wrapper)) {
          found.add(wrapper);
        }
      }
    });
    return found;
  }

  @Override
  public void walkObjectWrappers(IObjectVisitable x) {
    x.visit(this);
    for (IObjectWrapper wrapper : getChildren()) {
      wrapper.walkObjectWrappers(x);
    }
  }

  @Override
  public IFieldWrapper getFieldWrapper(String pathExpr) {
    List<IFieldWrapper> found = getFieldWrappers(pathExpr);
    switch (found.size()) {
    case 0 :
      throw new IllegalArgumentException("'" + pathExpr + "' does not match any IFieldWrapper");
    case 1 :
      return found.get(0);
    default :
      for (IFieldWrapper f : found) {
        System.out.println(">>> " + f);
      }
      throw new IllegalArgumentException("'" + pathExpr + "' matches more than one IFieldWrapper");
    }
  }

  @Override
  public List<IFieldWrapper> getFieldWrappers(String pathExpr) {
    StepPath path;
    try {
      path = new SimplePathParser(pathExpr).parse();
    } catch (ParseException ex) {
      throw new RuntimeException(ex);
    }
    final List<IFieldWrapper> found = new ArrayList<>();
    path.matches(this, new IFieldVisitable() {
      @Override
      public void visit(IFieldWrapper wrapper) {
        found.add(wrapper);
      }
    });
    return found;
  }

  @Override
  public void walkFieldWrappers(IFieldVisitable x) {
    for (IObjectWrapper wrapper : getChildren()) {
      wrapper.walkFieldWrappers(x);
    }
  }

  @Override
  public List<IFieldWrapper> getFieldWrappers() {
    List<IFieldWrapper> wrapperList = new ArrayList<>();
    getAllFieldWrappers(this, wrapperList);
    return wrapperList;
  }

  
//  public static IObjectWrapper buildObjectPlan (IObjectPlan parent, Field field, Class<?> parentClass, String name, boolean withinCollection, Type type, EntryMode entryMode, ArraySizeList arraySizes, Field lastEntryField) {
//    IObjectPlan objPlan;
//    
//    if (type instanceof GenericArrayType) {
//      Type type1 = ((GenericArrayType)type).getGenericComponentType();
//      objPlan = arrayPlanDetail(parent, field, parentClass, name, type1, entryMode, arraySizes, lastEntryField);
//    } else if (type instanceof ParameterizedType) {
//      ParameterizedType ptype = (ParameterizedType)type;
//      Type type1 = ptype.getRawType();
//      if (type1.equals(List.class)) {
//        Type[] typeArgs = ptype.getActualTypeArguments();
//        if (typeArgs.length != 1) {
//          throw new IllegalArgumentException("List must have one, and only one, type parameter");
//        }
//        Type type2 = typeArgs[0];
//        objPlan = listPlanDetail(parent, field, parentClass, name, type2, entryMode, arraySizes, lastEntryField);
//      } else {
//        throw new IllegalArgumentException("Parameterized type that is not a List");
//      }
//    } else if (type instanceof Class) {
//      Class<?> klass = (Class<?>)type;
//      if (klass.isArray()) {
//        Type type1 = klass.getComponentType();
//        objPlan = arrayPlanDetail(parent, field, parentClass, name, type1, entryMode, arraySizes, lastEntryField);
//      } else {
//        objPlan = fieldPlanDetail(parent, field, parentClass, name, withinCollection, type, entryMode, arraySizes, lastEntryField);
//      }
//    } else {
//      throw new IllegalArgumentException("Unsupported type: " + type);
//    }
//    return objPlan;
//  }
  
  

}
