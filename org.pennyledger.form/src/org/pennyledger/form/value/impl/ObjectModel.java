package org.pennyledger.form.value.impl;

import java.util.ArrayList;
import java.util.List;

import org.pennyledger.form.path.StepPath;
import org.pennyledger.form.path.Trail;
import org.pennyledger.form.path.parser.ParseException;
import org.pennyledger.form.path.parser.SimplePathParser;
import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IFieldModel;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectModel;

public abstract class ObjectModel implements IObjectModel {

  
  private static void getAllObjectModels(IObjectModel parent, List<IObjectModel> objList) {
    parent.walkObjectModels(new IObjectVisitable() {
      @Override
      public void visit(IObjectModel model) {
        objList.add(model);
      }
    });
  }

  
  private static void getAllFieldModels(IObjectModel parent, List<IFieldModel> fieldList) {
    parent.walkFieldModels(new IFieldVisitable() {
      @Override
      public void visit(IFieldModel model) {
        fieldList.add(model);
      }
    });
  }

  
  @Override
  public void walkObjectModels (String pathExpr, IObjectVisitable x) {
    StepPath path;
    try {
      path = new SimplePathParser(pathExpr).parse();
    } catch (ParseException ex) {
      throw new RuntimeException(ex);
    }
    path.matches(this, new Trail(this), x);
  }

  
  @Override
  public void walkFieldModels (String pathExpr, IFieldVisitable x) {
    StepPath path;
    try {
      path = new SimplePathParser(pathExpr).parse();
    } catch (ParseException ex) {
      throw new RuntimeException(ex);
    }
    path.matches(this, x);
  }

  
//  public static IObjectModel wrapValue(IContainerReference container, String name, Field field, Type type, Object value) {
//    IObjectModel model;
//    if (klass.isArray()) {
//      Class<?> elemType = klass.getComponentType();
//      model = new ArrayModel(container, name, elemType, value);
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
//      model = new FieldModel(container, name, klass);
//    } else {
//      model = new ClassModel(container, name);
//    }
//    model.setValue(value);
//    return model;
//  }

  private final IObjectModel parent;

  protected ObjectModel(IObjectModel parent) {
    this.parent = parent;
  }
  
//  protected ObjectModel(IObjectModel parent, IContainerReference container) {
//    this.parent = parent;
//    this.container = container;
//  }

  @Override
  public List<IObjectModel> getObjectModels() {
    List<IObjectModel> modelList = new ArrayList<>();
    getAllObjectModels(this, modelList);
    return modelList;
  }

  @Override
  public boolean isClass() {
    return false;
  }

  @Override
  public IObjectModel getParent() {
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
  public IObjectModel getObjectModel(String pathExpr) {
    List<IObjectModel> found = getObjectModels(pathExpr);
    switch (found.size()) {
    case 0 :
      throw new IllegalArgumentException("'" + pathExpr + "' does not match any IObjectModel");
    case 1 :
      return found.get(0);
    default :
      throw new IllegalArgumentException("'" + pathExpr + "' matches more than one IObjectModel");
    }
  }

  @Override
  public List<IObjectModel> getObjectModels(String pathExpr) {
    StepPath path;
    try {
      path = new SimplePathParser(pathExpr).parse();
    } catch (ParseException ex) {
      throw new RuntimeException(ex);
    }
    final List<IObjectModel> found = new ArrayList<>();
    Trail trail = new Trail(this);
    path.matches(this, trail, new IObjectVisitable() {
      @Override
      public void visit(IObjectModel model) {
        if (!found.contains(model)) {
          found.add(model);
        }
      }
    });
    return found;
  }

  @Override
  public void walkObjectModels(IObjectVisitable x) {
    x.visit(this);
    for (IObjectModel model : getChildren()) {
      model.walkObjectModels(x);
    }
  }

  @Override
  public IFieldModel getFieldModel(String pathExpr) {
    List<IFieldModel> found = getFieldModels(pathExpr);
    switch (found.size()) {
    case 0 :
      throw new IllegalArgumentException("'" + pathExpr + "' does not match any IFieldModel");
    case 1 :
      return found.get(0);
    default :
      for (IFieldModel f : found) {
        System.out.println(">>> " + f);
      }
      throw new IllegalArgumentException("'" + pathExpr + "' matches more than one IFieldModel");
    }
  }

  @Override
  public List<IFieldModel> getFieldModels(String pathExpr) {
    StepPath path;
    try {
      path = new SimplePathParser(pathExpr).parse();
    } catch (ParseException ex) {
      throw new RuntimeException(ex);
    }
    final List<IFieldModel> found = new ArrayList<>();
    path.matches(this, new IFieldVisitable() {
      @Override
      public void visit(IFieldModel model) {
        found.add(model);
      }
    });
    return found;
  }

  @Override
  public void walkFieldModels(IFieldVisitable x) {
    for (IObjectModel model : getChildren()) {
      model.walkFieldModels(x);
    }
  }

  @Override
  public List<IFieldModel> getFieldModels() {
    List<IFieldModel> modelList = new ArrayList<>();
    getAllFieldModels(this, modelList);
    return modelList;
  }

  
//  public static IObjectModel buildObjectPlan (IObjectPlan parent, Field field, Class<?> parentClass, String name, boolean withinCollection, Type type, EntryMode entryMode, ArraySizeList arraySizes, Field lastEntryField) {
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
