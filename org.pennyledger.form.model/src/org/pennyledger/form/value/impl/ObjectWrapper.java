package org.pennyledger.form.value.impl;

import java.util.ArrayList;
import java.util.List;

import org.pennyledger.form.path.StepPath;
import org.pennyledger.form.path.Trail;
import org.pennyledger.form.path.parser.ParseException;
import org.pennyledger.form.path.parser.SimplePathParser;
import org.pennyledger.form.reflect.IContainerReference;
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
      path = new SimplePathParser().parse(pathExpr);
    } catch (ParseException ex) {
      throw new RuntimeException(ex);
    }
    path.matches(this, new Trail(this), x);
  }

  
  @Override
  public void walkFieldWrappers (String pathExpr, IFieldVisitable x) {
    StepPath path;
    try {
      path = new SimplePathParser().parse(pathExpr);
    } catch (ParseException ex) {
      throw new RuntimeException(ex);
    }
    path.matches(this, x);
  }

  
  public static IObjectWrapper wrapValue(IContainerReference container, String name, Class<?> klass, Object value) {
    IObjectWrapper wrapper;
    if (String.class.isAssignableFrom(klass) || Integer.class.isAssignableFrom(klass)) {
      wrapper = new FieldWrapper(container, name, klass);
    } else {
      wrapper = new ClassWrapper(container, name);
    }
    wrapper.setValue(value);
    return wrapper;
  }

  private final IContainerReference container;

  protected ObjectWrapper(IContainerReference container) {
    this.container = container;
  }

  @Override
  public List<IObjectWrapper> getObjectWrappers() {
    List<IObjectWrapper> wrapperList = new ArrayList<>();
    getAllObjectWrappers(this, wrapperList);
    return wrapperList;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <X> X getValue() {
    return (X)container.getValue();
  }

  @Override
  public boolean isClass() {
    return false;
  }

  @Override
  public boolean isField() {
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
      for (IObjectWrapper f : found) {
        System.out.println(">>> " + f);
      }
      throw new IllegalArgumentException("'" + pathExpr + "' matches more than one IObjectWrapper");
    }
  }

  @Override
  public List<IObjectWrapper> getObjectWrappers(String pathExpr) {
    StepPath path;
    try {
      path = new SimplePathParser().parse(pathExpr);
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
  public void setValue(Object value) {
    container.setValue(value);
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
      path = new SimplePathParser().parse(pathExpr);
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

}
