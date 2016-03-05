package org.pennyledger.form.factory;

import java.util.ArrayList;
import java.util.List;

import org.pennyledger.form.Entity;
import org.pennyledger.form.EntryMode;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.impl.ArrayPlan;
import org.pennyledger.form.plan.impl.ClassPlan;
import org.pennyledger.form.plan.impl.EntityPlan;
import org.pennyledger.form.reflect.FormContainerReference;
import org.pennyledger.form.reflect.IContainerReference;
import org.pennyledger.form.value.EffectiveModeListener;
import org.pennyledger.form.value.ErrorListener;
import org.pennyledger.form.value.FieldEventListener;
import org.pennyledger.form.value.IContainerModel;
import org.pennyledger.form.value.IFieldModel;
import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IForm;
import org.pennyledger.form.value.IObjectModel;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.ModelChangeListener;
import org.pennyledger.util.UserEntryException;

public class Form<T> implements IForm<T> {

  private final EventListenerList<EffectiveModeListener> effectiveModeListeners = new EventListenerList<>();
  private final EventListenerList<ErrorListener> errorListeners = new EventListenerList<>();
  private final EventListenerList<FieldEventListener> fieldEventListeners = new EventListenerList<>();
  private final List<ModelChangeListener> modelChangeListeners = new ArrayList<>();

  private EntryMode entryMode;
  
  private IObjectPlan formPlan;
  private IObjectModel formModel;
  private IContainerReference container;
  
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
    if (formClass.isArray()) {
      formPlan = new ArrayPlan(null, null, entityName(formClass), formClass, 0, entryMode);
    } else if (formClass.isAnnotationPresent(Entity.class)) {
      formPlan = new EntityPlan(formClass);
    } else {
      formPlan = new ClassPlan(null, entityName(formClass), formClass, entryMode, false);
    }
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
  public IFieldModel getFieldModel(String pathExpr) {
    return formModel.getFieldModel(pathExpr);
  }

  @Override
  public List<IFieldModel> getFieldModels() {
    return formModel.getFieldModels();
  }

  @Override
  public List<IFieldModel> getFieldModels(String pathExpr) {
    return formModel.getFieldModels(pathExpr);
  }

  @Override
  public IObjectModel getObjectModel(String pathExpr) {
    return formModel.getObjectModel(pathExpr);
  }

  @Override
  public List<IObjectModel> getObjectModels() {
    return formModel.getObjectModels();
  }

  @Override
  public List<IObjectModel> getObjectModels(String pathExpr) {
    return formModel.getObjectModels(pathExpr);
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
      formPlan = new ClassPlan(null, entityName(valueClass), valueClass, entryMode, false);;
    }
    if (formModel == null) {
      container = new FormContainerReference(value);
      formModel = formPlan.buildModel(this, null, container);
      formModel.syncToCurrentValue();
    } else {
      formModel.setValue(value);
    }
  }

  @Override
  public void walkFieldModels(IFieldVisitable x) {
    formModel.walkFieldModels(x);
  }

  @Override
  public void walkFieldModels(String path, IFieldVisitable x) {
    formModel.walkFieldModels(path, x);
  }

  @Override
  public void walkObjectModels(IObjectVisitable x) {
    formModel.walkObjectModels(x);
    
  }

  @Override
  public void walkObjectModels(String path, IObjectVisitable x) {
    formModel.walkObjectModels(path, x);
  }

  @Override
  public IObjectPlan getPlan() {
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

  @Override
  public void fireModelAdded(IContainerModel parent, IObjectModel addedModel) {
    for (ModelChangeListener x : modelChangeListeners) {
      x.modelAdded(parent, addedModel);
    }
  }

  @Override
  public void fireModelRemoved(IContainerModel parent, IObjectModel removedModel) {
    for (ModelChangeListener x : modelChangeListeners) {
      x.modelRemoved(parent, removedModel);
    }
  }

  @Override
  public void addModelChangeListener(ModelChangeListener x) {
    modelChangeListeners.add(x);
  }

  @Override
  public void removeModelChangeListener(ModelChangeListener x) {
    modelChangeListeners.remove(x);
  }

  @Override
  public void fireModelReplaced(IContainerModel parent, IObjectModel removedModel, IObjectModel addedModel) {
    for (ModelChangeListener x : modelChangeListeners) {
      x.modelReplaced(parent, removedModel, addedModel);
    }
  }
}
