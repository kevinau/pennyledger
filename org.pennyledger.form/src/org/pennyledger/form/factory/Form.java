package org.pennyledger.form.factory;

import java.util.List;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.plan.IClassPlan;
import org.pennyledger.form.plan.impl.ClassPlan;
import org.pennyledger.form.reflect.FormContainerReference;
import org.pennyledger.form.reflect.IContainerReference;
import org.pennyledger.form.value.EffectiveModeListener;
import org.pennyledger.form.value.ErrorListener;
import org.pennyledger.form.value.FieldEventListener;
import org.pennyledger.form.value.IClassModel;
import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IFieldModel;
import org.pennyledger.form.value.IForm;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectModel;
import org.pennyledger.form.value.impl.ClassModel;
import org.pennyledger.util.UserEntryException;

public class Form<T> implements IForm<T> {

  private final EventListenerList<EffectiveModeListener> effectiveModeListeners = new EventListenerList<>();
  private final EventListenerList<ErrorListener> errorListeners = new EventListenerList<>();
  private final EventListenerList<FieldEventListener> fieldEventListeners = new EventListenerList<>();

  private EntryMode entryMode;
  
  private IClassPlan<?> formPlan;
  private IClassModel formModel;
  private IContainerReference container = new FormContainerReference();
  
  public Form () {
    this (EntryMode.ENTRY);
  }

  public Form (EntryMode entryMode) {
    this.entryMode = entryMode;
  }

  public Form (Class<?> formClass) {
    this (formClass, EntryMode.ENTRY);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Form (Class<?> formClass, EntryMode entryMode) {
    this.entryMode = entryMode;
    formPlan = new ClassPlan(null, entityName(formClass), formClass, entryMode);;
  }

  protected static String entityName (Class<?> entityClass) {
    String klassName = entityClass.getSimpleName();
    return Character.toLowerCase(klassName.charAt(0)) + klassName.substring(1);
  }
  
  
  @Override
  public void addEffectiveModeListener(String pathExpr, EffectiveModeListener x) {
    effectiveModeListeners.add(pathExpr, x);
  }

  @Override
  public void addFieldEventListener(String pathExpr, FieldEventListener x) {
    fieldEventListeners.add(pathExpr, x);
  }

  @Override
  public void addErrorListener(String pathExpr, ErrorListener x) {
    errorListeners.add(pathExpr, x);
  }

//  private void buildClassPlan (Class<?> klass) {
//    IClassPlan<?> plan = classPlans.get(klass);
//    if (plan == null) {
//      plan = new ClassPlan(klass);
//      classPlans.put(klass, plan);
//    }
//  }

  @Override
  public void fireValueEqualityChange(IFieldModel model) {
    fieldEventListeners.fireFieldEvents(model, new IFieldEvent<FieldEventListener>() {
      @Override
      public void eventFired(IFieldModel model, FieldEventListener listener) {
        listener.valueEqualityChange(model);
      }
    });
  }

  @Override
  public void fireSourceEqualityChange(IFieldModel model, boolean isDataTrigger) {
    fieldEventListeners.fireFieldEvents(model, new IFieldEvent<FieldEventListener>() {
      @Override
      public void eventFired(IFieldModel model, FieldEventListener listener) {
        listener.sourceEqualityChange(model, isDataTrigger);
      }
    });
  }

  @Override
  public void fireComparisonBasisChange(IFieldModel model) {
    fieldEventListeners.fireFieldEvents(model, new IFieldEvent<FieldEventListener>() {
      @Override
      public void eventFired(IFieldModel model, FieldEventListener listener) {
        listener.equalityBasisChange(model);
      }
    });
  }

  @Override
  public void fireEffectiveModeChange(IObjectModel model) {
    effectiveModeListeners.fireObjectEvents(model, new IObjectEvent<EffectiveModeListener>() {
      @Override
      public void eventFired(IObjectModel model, EffectiveModeListener listener) {
        listener.modeChange(model);
      }
    });
  }

  @Override
  public void fireErrorCleared(IObjectModel model) {
    errorListeners.fireObjectEvents(model, new IObjectEvent<ErrorListener>() {
      @Override
      public void eventFired(IObjectModel model, ErrorListener listener) {
        listener.errorCleared(model);
      }
    });
  }

  @Override
  public void fireErrorNoted(IObjectModel model, UserEntryException ex) {
    errorListeners.fireObjectEvents(model, new IObjectEvent<ErrorListener>() {
      @Override
      public void eventFired(IObjectModel model, ErrorListener listener) {
        listener.errorNoted(model, ex);
      }
    });
  }

  @Override
  public void fireSourceChange(IFieldModel model) {
    fieldEventListeners.fireFieldEvents(model, new IFieldEvent<FieldEventListener>() {
      @Override
      public void eventFired(IFieldModel model, FieldEventListener listener) {
        listener.sourceChange(model);
      }
    });
  }
  
  @Override
  public void fireValueChange(IFieldModel model) {
    fieldEventListeners.fireFieldEvents(model, new IFieldEvent<FieldEventListener>() {
      @Override
      public void eventFired(IFieldModel model, FieldEventListener listener) {
        listener.valueChange(model);
      }
    });
  }

  @Override
  public IFieldModel getFieldWrapper(String pathExpr) {
    return formModel.getFieldWrapper(pathExpr);
  }

  @Override
  public List<IFieldModel> getFieldWrappers() {
    return formModel.getFieldWrappers();
  }

  @Override
  public List<IFieldModel> getFieldWrappers(String pathExpr) {
    return formModel.getFieldWrappers(pathExpr);
  }

  @Override
  public IObjectModel getObjectWrapper(String pathExpr) {
    return formModel.getObjectWrapper(pathExpr);
  }

  @Override
  public List<IObjectModel> getObjectWrappers() {
    return formModel.getObjectWrappers();
  }

  @Override
  public List<IObjectModel> getObjectWrappers(String pathExpr) {
    return formModel.getObjectWrappers(pathExpr);
  }

  @Override
  public T getValue() {
    if (formModel == null) {
      return null;
    } else {
      return formModel.getValue();
    }
  }

  @Override
  public void removeEffectiveModeListener(String pathExpr, EffectiveModeListener x) {
    effectiveModeListeners.remove(pathExpr, x);
  }

  @Override
  public void removeFieldEventListener(String pathExpr, FieldEventListener x) {
    fieldEventListeners.remove(pathExpr, x);
  }

  @Override
  public void removeErrorListener(String pathExpr, ErrorListener x) {
    errorListeners.add(pathExpr, x);
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public void setValue(Object value) {
    if (formPlan == null) {
      Class<?> valueClass = value.getClass();
      formPlan = new ClassPlan(null, entityName(valueClass), valueClass, entryMode);;
    }
    if (formModel == null) {
      formModel = new ClassModel(null, container, formPlan);
    }
    formModel.setValue(value);
  }

  @Override
  public void walkFieldWrappers(IFieldVisitable x) {
    formModel.walkFieldWrappers(x);
  }

  @Override
  public void walkFieldWrappers(String path, IFieldVisitable x) {
    formModel.walkFieldWrappers(path, x);
  }

  @Override
  public void walkObjectWrappers(IObjectVisitable x) {
    formModel.walkObjectWrappers(x);
    
  }

  @Override
  public void walkObjectWrappers(String path, IObjectVisitable x) {
    formModel.walkObjectWrappers(path, x);
  }

  @Override
  public IClassPlan<?> getPlan() {
    return formPlan;
  }
  
  @Override
  public void dump() {
    System.out.println("Form");
    if (formPlan != null) {
      formPlan.dump(1);
      if (formModel != null) {
        formModel.dump(1);
      }
    }
  }
}
