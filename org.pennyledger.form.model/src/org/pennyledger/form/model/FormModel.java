package org.pennyledger.form.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.plan.IFormPlan;
import org.pennyledger.form.plan.IGroupPlan;
import org.pennyledger.form.plan.impl.FormPlan;

public class FormModel<T> implements IFormModel<T> {

  private final IFormPlan formPlan;

  private final IGroupPlan rootPlan;
  private final IGroupModel rootModel;
  
  private final Class<T> formClass;
  //private final T instance;
  
  @SuppressWarnings("unchecked")
  public FormModel (T instance) {
    if (instance instanceof Class) {
      throw new IllegalArgumentException("This should not happen");
    }
    formClass = (Class<T>)instance.getClass();
    formPlan = new FormPlan(formClass);
    
    rootPlan = formPlan.getRootPlan();
    rootModel = new GroupModel(this, null, null, rootPlan, formClass, instance);
    rootModel.setEventsActive(true);
  }


  public FormModel (Class<T> formClass) {
    this.formClass = formClass;
    T instance = newInstance(formClass);
    formPlan = new FormPlan(formClass);

    rootPlan = formPlan.getRootPlan();
    rootModel = new GroupModel(this, null, null, rootPlan, formClass, instance);
    rootModel.setEventsActive(true);
  }

  
  @SuppressWarnings("unchecked")
  public FormModel (IFormPlan formPlan) {
    this.formPlan = formPlan;
    this.formClass = (Class<T>)formPlan.getFormClass();
    T instance = newInstance(formClass);
    
    rootPlan = formPlan.getRootPlan();
    rootModel = new GroupModel(this, null, null, rootPlan, formClass, instance);
    rootModel.setEventsActive(true);
  }

  
  private static <X> X newInstance (Class<X> formClass) {
    try {
      Constructor<X> constructor = formClass.getDeclaredConstructor();
      constructor.setAccessible(true);
      X instance = constructor.newInstance();
      return instance;
    } catch (SecurityException ex) {
      throw new RuntimeException(ex);
    } catch (IllegalArgumentException ex) {
      throw new RuntimeException(ex);
    } catch (NoSuchMethodException ex) {
      throw new RuntimeException(ex);
    } catch (InstantiationException ex) {
      throw new RuntimeException(ex);
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    } catch (InvocationTargetException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public T newInstance () {
    try {
      Constructor<T> constructor = formClass.getDeclaredConstructor();
      constructor.setAccessible(true);
      T instance = constructor.newInstance();
      return instance;
    } catch (SecurityException ex) {
      throw new RuntimeException(ex);
    } catch (IllegalArgumentException ex) {
      throw new RuntimeException(ex);
    } catch (NoSuchMethodException ex) {
      throw new RuntimeException(ex);
    } catch (InstantiationException ex) {
      throw new RuntimeException(ex);
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    } catch (InvocationTargetException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public Class<T> getFormClass() {
    return formClass;
  }
  
  @Override
  public String getFormName() {
    return formPlan.getFormName();
  }

  @Override
  public String getIconName() {
    return formPlan.getIconName();
  }

  
  @SuppressWarnings("unchecked")
  @Override
  public <X extends IContainerModel> X getRootModel () {
    return (X)rootModel;
  }

  
  @Override
  public List<IObjectModel> selectObjectModels(String xpathExpr) {
    return rootModel.selectObjectModels(xpathExpr);
  }


  @Override
  public List<IFieldModel> selectFieldModels(String xpathExpr) {
    return rootModel.selectFieldModels(xpathExpr);
  }


  @Override
  public List<IFieldModel> selectAllFieldModels() {
    return rootModel.selectAllFieldModels();
  }


  @Override
  public IFieldModel selectFieldModel(String xpathExpr) {
    return rootModel.selectFieldModel(xpathExpr);
  }


  @Override
  public <E extends IObjectModel> E selectObjectModel(String xpathExpr) {
    return rootModel.selectObjectModel(xpathExpr);
  }
  

  @Override
  public EntryMode getEntryMode() {
    return EntryMode.UNSPECIFIED;
  }


  @Override
  public EffectiveMode getEffectiveMode() {
    return EffectiveMode.ENTRY;
  }

  
  @Override
  public void addEntryModeListener (EffectiveModeListener x) {
    rootModel.addEffectiveModeListener(x);
  }
  
  
  @Override
  public void removeEntryModeListener (EffectiveModeListener x) {
    rootModel.removeEffectiveModeListener(x);
  }
  
  
  /**
   * Add a FieldChangeListener.  If the conditions are right, fire off an event
   * immediately.
   */
  @Override
  public void addFieldEventListener (FieldEventListener x) {
    rootModel.addFieldEventListener(x);
  }
  
  
  @Override
  public void removeFieldEventListener (FieldEventListener x) {
    rootModel.removeFieldEventListener(x);
  }

  
  @Override
  public void addContainerEventListener (ContainerEventListener x) {
    rootModel.addContainerEventListener(x);
  }
  

  @Override
  public void removeContainerEventListener (ContainerEventListener x) {
    rootModel.removeContainerEventListener(x);
  }
  
  
  @Override
  public void dump() {
    rootModel.dump();
  }


  @Override
  public Object getUnderlyingValue(Object parentRef) {
    throw new RuntimeException("This method should not be called");
  }


  @Override
  public void setUnderlyingValue(Object parentRef, Object value) {
    //throw new RuntimeException("This method should not be called");
  }


  @Override
  public void setInstance(T instance) {
    rootModel.setValue(instance);
  }


  @Override
  public void setValueAndReference(T instance) {
    rootModel.setValue(instance);
    walkFields(new IFieldVisitable() {
      @Override
      public boolean visit(IFieldModel model) {
        model.setReferenceFromValue();
        return true;
      }
    });
  }


  @Override
  public void setValueFromDefault () {
    walkFields(new IFieldVisitable() {
      @Override
      public boolean visit(IFieldModel model) {
        model.setValueFromDefault();
        return true;
      }
    });
  }

  
  @Override
  public void setReferenceFromValue () {
    walkFields(new IFieldVisitable() {
      @Override
      public boolean visit(IFieldModel model) {
        model.setReferenceFromValue();
        return true;
      }
    });
  }

  
  @Override
  public T setNewInstanceFromDefaults() {
    T instance = newInstance();
    // This setValue is done to create the model structure
    rootModel.setValue(instance);
    rootModel.walkFields(new IFieldVisitable() {
      @Override
      public boolean visit(IFieldModel model) {
        // This setValue sets any computed default values
        model.setValueFromDefault();
        return true;
      }
    });
    return instance;
  }

  
  @Override
  public void setModes (final EntryMode mode, final ComparisonBasis compareBasis) {
    walkFields(new IFieldVisitable() {
      @Override
      public boolean visit(IFieldModel model) {
        model.setCompareBasis(compareBasis);
        model.setMode(mode);
        return true;
      }
    });
  }
  
  


  @SuppressWarnings("unchecked")
  @Override
  public T getInstance() {
    return (T)rootModel.getValue();
  }


  @Override
  public boolean walkFields(IFieldVisitable x) {
    return rootModel.walkFields(x);
  }

  
  @Override
  public IFieldModel[] getFieldModels () {
    List<IFieldModel> fields = rootModel.selectAllFieldModels();
    return fields.toArray(new IFieldModel[fields.size()]);
  }

  
  @Override
  public void addValidationMethod (String[] fieldNames, IMethodRunnable runnable) {
    rootModel.addValidationMethod(fieldNames, runnable);
  }


  @Override
  public boolean isFieldModel() {
    return false;
  }


  @Override
  public Collection<? extends IObjectModel> getChildren() {
    return Collections.singletonList(rootModel);
  }

}
