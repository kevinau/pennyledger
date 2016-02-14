package org.pennyledger.form.model;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.plan.IGroupPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.IRuntimeDefaultProvider;
import org.pennyledger.form.plan.IRuntimeFactoryProvider;
import org.pennyledger.form.plan.IRuntimeModeProvider;
import org.pennyledger.form.plan.IRuntimeOccursProvider;
import org.pennyledger.form.plan.IValidationMethod;
import org.pennyledger.form.plan.ValidationMethod;
import org.pennyledger.form.plan.impl.ObjectPlanFactory;
import org.pennyledger.util.DualAccessMap;
import org.pennyledger.util.UserEntryException;

public class GroupModel extends ContainerModel implements IGroupModel {
  
  private IGroupPlan groupPlan;
  private final Map<String, TreeSet<IValidationMethod>> methodValidations = new HashMap<String, TreeSet<IValidationMethod>>();

  private Object instance;
  
  private DualAccessMap<String, IObjectModel> memberModels = new DualAccessMap<String, IObjectModel>();

  
  public class ValidationHandler extends FieldEventAdapter {

    private final TreeSet<IValidationMethod> validationsList;
    private final IFieldModel model;

    private ValidationHandler (TreeSet<IValidationMethod> validationsList, IFieldModel model) {
      this.validationsList = validationsList;
      this.model = model;
    }

    private void noteMethodErrors(IValidationMethod vMethod, UserEntryException userError) {
      String[] dependsOn = vMethod.getDependsOn();
      IFieldModel[] dependsOnModels = new IFieldModel[dependsOn.length];
      int i = 0;
      for (String dependee : dependsOn) {
        IFieldModel dependeeModel = getModelByName(dependee);
        if (dependeeModel != null) {
          dependsOnModels[i++] = dependeeModel;
        }
      }
      if (i < dependsOn.length) {
        dependsOnModels = Arrays.copyOf(dependsOnModels, i);
      }
      for (IFieldModel m : dependsOnModels) {
        m.noteValidationError(vMethod, dependsOnModels, userError);
      }
    }

    private void clearMethodErrors(IValidationMethod vMethod) {
      String[] dependsOn = vMethod.getDependsOn();
      for (String dependee : dependsOn) {
        IFieldModel dependeeModel = getModelByName(dependee);
        if (dependeeModel != null) {
          dependeeModel.clearError(vMethod);
        }
      }
    }

    private boolean preconditionsMet(IValidationMethod vMethod) {
      String[] dependsOn = vMethod.getDependsOn();
      for (String dependee : dependsOn) {
        IFieldModel dependeeModel = getModelByName(dependee);
        if (dependeeModel != null) {
          UserEntryException.Type status = dependeeModel.getStatus(vMethod.getOrder());
          if (status == UserEntryException.Type.ERROR || status == UserEntryException.Type.INCOMPLETE || status == UserEntryException.Type.REQUIRED) {
            return false;
          }
        }
      }
      return true;
    }

    private void doValidations(IFieldModel model) {
      // The validations list is in "getOrder" order.
      Iterator<IValidationMethod> i = validationsList.iterator();
      int maxOrder = 1;
      while (i.hasNext()) {
        IValidationMethod vMethod = i.next();
        maxOrder = vMethod.getOrder();
        if (preconditionsMet(vMethod)) {
          try {
            vMethod.validate(instance);
            clearMethodErrors(vMethod);
          } catch (UserEntryException uex) {
            noteMethodErrors(vMethod, uex);
            break;
          }
        } else {
          // These errors will be checked for again when the preconditions are met.
          clearMethodErrors(vMethod);
        }
      }
      // Complete any validations of the same order.
      while (i.hasNext()) {
        IValidationMethod vMethod = i.next();
        if (vMethod.getOrder() == maxOrder) {
          try {
            vMethod.validate(instance);
            // No error
            clearMethodErrors(vMethod);
            maxOrder = vMethod.getOrder();
          } catch (UserEntryException uex) {
            noteMethodErrors(vMethod, uex);
          }
        } else {
          // Remove any error associated with this validation. The
          // validation has not happened (it will be run later when
          // the above errors are cleared).
          clearMethodErrors(vMethod);
        }
      }
    }

    @Override
    public void valueChange(IFieldModel vmodel) {
      // When a value change occurs, run down the list of validations.
      // Stop when one fails, except that validations of the same order are
      // still run. Validations of a higher order are cleared.
      doValidations(vmodel);
    }

    @Override
    public void errorCleared(IFieldModel vmodel) {
      // When a value is cleared of an error, run down the list of
      // validations as if the value had changed. But do not do this if the
      // errors are being cleared on the same model that is being validated
      if (!model.equals(vmodel)) {
        doValidations(vmodel);
      }
    }
  }
  
  
  public GroupModel (IFormModel<?> ownerForm, IContainerModel parentModel, Object parentRef, IGroupPlan groupPlan, Class<?> klass, Object instance) {
    super (ownerForm, parentModel, parentRef, groupPlan.getPathName(), groupPlan);
    this.groupPlan = groupPlan;
    
    setValue(instance);

//    if (instance != null) {
//      for (IObjectPlan memberPlan : groupPlan.getMemberPlans()) {
//        Object value = getUnderlyingValue(memberPlan.getParentRef());
//        IObjectModel memberModel = memberPlan.createModel(this, memberPlan.getParentRef(), memberPlan.getPathName(), value);
//        memberModels.put(memberPlan.getPathName(), memberModel);
//      }
//      initialProviderCalls(instance);
//      completeProviderMapping();
      ////resetToInitial();
//    }
  }


  @Override
  public void setValue (Object instance) {
    if (instance == null) {
      // The instance is being destroyed, so remove all member models
      for (IObjectModel memberModel : memberModels.values()) {
        memberModel.detach();
      }
      memberModels.clear();
      this.instance = null;
      setUnderlyingValue(null);
      fireContainerDestroy(this);
    } else {
      boolean sameInstance;
      if (this.instance == null) {
        sameInstance = false;
      } else {
        Class<?> thisClass = this.instance.getClass();
        Class<?> newClass = instance.getClass();
        sameInstance = thisClass.equals(newClass);
      }
      this.instance = instance;
      if (sameInstance) {
        // The new instance has the same class as the existing instance, so simply set the member values
        setUnderlyingValue(instance);
        for (IObjectModel memberModel : memberModels.values()) {
          Object v = memberModel.getUnderlyingValue();
          memberModel.setValue(v);
        }
      } else {
        setUnderlyingValue(instance);
        // The new instance is completely different, so build a new set of member models
        memberModels = new DualAccessMap<String, IObjectModel>();
      
        groupPlan = ObjectPlanFactory.buildGroupPlan (groupPlan.getParentRef(), groupPlan.getPathName(), instance.getClass());
        //IGroupPlan groupPlan2 = groupPlan;
        for (IObjectPlan memberPlan : groupPlan.getMemberPlans()) {
          Object value = getUnderlyingValue(memberPlan.getParentRef());
          IObjectModel memberModel = memberPlan.createModel(getOwnerForm(), this, memberPlan.getParentRef(), memberPlan.getPathName(), value);
          memberModels.put(memberPlan.getPathName(), memberModel);
        }
        // The fire container create event precedes the other methods so that UI listeners can create
        // the new container object, and subsequent methods will have a target for the events that
        // are fired.
        fireContainerCreate(this);
        setEventsActive(true);
        
        staticProviderCalls(groupPlan, instance);
        //resetToInitial();
        completeProviderMapping(groupPlan, instance);
      }
    }
  }
  
  
  @Override
  public Object getValue () {
    return instance;
  }
  
  
  @Override
  public void resetToInitial () {
    super.resetToInitial();
    for (IObjectModel member : getChildren()) {
      member.resetToInitial();
    }
  }
  
//  @Override
//  public void setInitialFromPlan () {
//    super.setInitialFromPlan();
//    
//    for (IObjectModel member : getChildren()) {
//      ((ObjectModel)member).setInitialFromPlan();
//    }
//  }

  
  /**
   * Set the initial value and data entry attributes from an instance of the
   * class.
   * <p>
   * This method does not fire mode change events. It is part of the initial
   * setup prior to any data entry. A value and attribute change events will be
   * fired when all values and attributes have been correctly set.
   */
  private void staticProviderCalls(IGroupPlan groupPlan, Object instance) {
    // Set mode from runtime mode providers
    for (IRuntimeModeProvider modeProvider : groupPlan.getRuntimeModeProviders()) {
      EntryMode mode = modeProvider.getEntryMode(instance);
      for (String appliesTo : modeProvider.getAppliesTo()) {
        IObjectModel[] matching = getMatchingMembers(appliesTo);
        for (IObjectModel objModel : matching) {
          ((ObjectModel)objModel).setMode(mode);
        }
      }
    }
    // Set default from runtime default providers
    for (IRuntimeDefaultProvider defaultProvider : groupPlan.getRuntimeDefaultProviders()) {
      Object defaultValue = defaultProvider.getDefaultValue(instance);
      for (String appliesTo : defaultProvider.getAppliesTo()) {
        IObjectModel[] matching = getMatchingMembers(appliesTo);
        for (IObjectModel objModel : matching) {
          if (objModel.isFieldModel()) {
            ((FieldModel)objModel).setDefaultValue(defaultValue);
          }
        }
      }
    }
    // Set array size from runtime size providers
    for (IRuntimeOccursProvider occursProvider : groupPlan.getRuntimeOccursProviders()) {
      int size = occursProvider.getOccurs(instance);
      for (String appliesTo : occursProvider.getAppliesTo()) {
        IObjectModel[] matching = getMatchingMembers(appliesTo);
        for (IObjectModel objModel : matching) {
          if (objModel.isArrayModel()) {
            ((ArrayModel)objModel).setArraySize(size);
          }
        }
      }
    }
  }



  private IFieldModel getModelByName (String name) {
    IFieldModel model = (IFieldModel)getMember(name);
    return model;
  }

  
  private interface IObjectModelRunnable {
    public void run (IObjectModel model); 
  }
  
  
  private interface IFieldModelRunnable {
    public void run (IFieldModel model); 
  }
  
  
  private interface IContainerModelRunnable {
    public void run (IContainerModel model); 
  }
  
  
  private interface IArrayModelRunnable {
    public void run (IArrayModel model); 
  }
  
  
  private void applyToObjectNodes (String[] appliesToList, IObjectModelRunnable runnable) {
    for (String appliesTo : appliesToList) {
      IObjectModel[] matching = getMatchingMembers(appliesTo);
      for (IObjectModel model : matching) {
        runnable.run(model);
      }
    }
  }

  
  private void applyToFieldNodes (String[] appliesToList, IFieldModelRunnable runnable) {
    for (String appliesTo : appliesToList) {
      IObjectModel[] matching = getMatchingMembers(appliesTo);
      for (IObjectModel model : matching) {
        runnable.run((IFieldModel)model);
      }
    }
  }
  
  
  private void applyToContainerNodes (String[] appliesToList, IContainerModelRunnable runnable) {
    for (String appliesTo : appliesToList) {
      IObjectModel[] matching = getMatchingMembers(appliesTo);
      for (IObjectModel model : matching) {
        runnable.run((IContainerModel)model);
      }
    }
  }


  private void applyToArrayNodes (String[] appliesToList, IArrayModelRunnable runnable) {
    for (String appliesTo : appliesToList) {
      IObjectModel[] matching = getMatchingMembers(appliesTo);
      for (IObjectModel model : matching) {
        runnable.run((IArrayModel)model);
      }
    }
  }
  
  
  private void completeProviderMapping (IGroupPlan groupPlan, final Object instance) {
    // Add default values that are dependent on other fields
    for (final IRuntimeDefaultProvider defaultProvider : groupPlan.getRuntimeDefaultProviders()) {
      String[] dependsOnList = defaultProvider.getDependsOn();
      for (String dependsOn : dependsOnList) {
        IFieldModel model = getModelByName(dependsOn);
        if (model != null) {
          model.addFieldEventListener(new FieldEventAdapter() {
            @Override
            public void valueChange(IFieldModel model) {
              final Object value = defaultProvider.getDefaultValue(instance);
              String[] appliesToList = defaultProvider.getAppliesTo();
              applyToFieldNodes(appliesToList, new IFieldModelRunnable() {
                public void run(IFieldModel applyToModel) {
                  applyToModel.setDefaultValue(value);
                }
              });
            }
          });
        }
      }
    }

    // Add factory providers that are dependent on other fields
    for (final IRuntimeFactoryProvider factoryProvider : groupPlan.getRuntimeFactoryProviders()) {
      String[] dependsOnList = factoryProvider.getDependsOn();
      for (String dependsOn : dependsOnList) {
        IFieldModel model = getModelByName(dependsOn);
        if (model != null) {
          model.addFieldEventListener(new FieldEventAdapter() {
            @Override
            public void valueChange(IFieldModel model) {
              final Object value = factoryProvider.createNewValue(instance);
              String[] appliesToList = factoryProvider.getAppliesTo();
              applyToContainerNodes(appliesToList, new IContainerModelRunnable() {
                public void run(IContainerModel applyToModel) {
                  applyToModel.setValue(value);
                }
              });
            }
          });
        }
      }
    }

    // Add field modes that are dependent on other fields
    for (final IRuntimeModeProvider modeProvider : groupPlan.getRuntimeModeProviders()) {
      String[] dependsOnList = modeProvider.getDependsOn();
      for (String dependsOn : dependsOnList) {
        IFieldModel model = getModelByName(dependsOn);
        if (model != null) {
          model.addFieldEventListener(new FieldEventAdapter() {
            @Override
            public void valueChange(IFieldModel model) {
              final EntryMode mode = modeProvider.getEntryMode(instance);
              String[] appliesToList = modeProvider.getAppliesTo();
              applyToObjectNodes(appliesToList, new IObjectModelRunnable() {
                public void run(IObjectModel applyToModel) {
                  applyToModel.setMode(mode);
                }
              });
            }
          });
        }
      }
    }

    // Add an array size that is dependent on other fields
    for (final IRuntimeOccursProvider occursProvider : groupPlan.getRuntimeOccursProviders()) {
      String[] dependsOnList = occursProvider.getDependsOn();
      for (String dependsOn : dependsOnList) {
        IFieldModel model = getModelByName(dependsOn);
        if (model != null) {
          model.addFieldEventListener(new FieldEventAdapter() {
            @Override
            public void valueChange(IFieldModel model) {
              final int size = occursProvider.getOccurs(instance);
              String[] appliesToList = occursProvider.getAppliesTo();
              applyToArrayNodes(appliesToList, new IArrayModelRunnable() {
                public void run(IArrayModel applyToModel) {
                  applyToModel.setArraySize(size);
                }
              });
            }
          });
        }
      }
    }

    // Add validation that is dependent on other fields.
    // First, build a list of validation methods for each field.
    for (final IValidationMethod validationMethod : groupPlan.getValidationMethods()) {
      String[] dependsOnList = validationMethod.getDependsOn();
      for (String dependsOn : dependsOnList) {
        TreeSet<IValidationMethod> validationsList = methodValidations.get(dependsOn);
        if (validationsList == null) {
          validationsList = new TreeSet<IValidationMethod>();
          methodValidations.put(dependsOn, validationsList);
        }
        validationsList.add(validationMethod);
      }
    }

    // Then, for each field that properly names a field model, add event handling to do the validation.
    for (String dependsOn : methodValidations.keySet()) {
      final TreeSet<IValidationMethod> validationsList = methodValidations.get(dependsOn);
      final IFieldModel model = getModelByName(dependsOn);
      if (model != null) {
        model.addFieldEventListener(new ValidationHandler(validationsList, model));
      }
    }
  }
  
  
  @Override
  public void addValidationMethod (String[] fieldNames, IMethodRunnable runnable) {
    for (String fieldName : fieldNames) {
      TreeSet<IValidationMethod> validationsList = methodValidations.get(fieldName);
      if (validationsList == null) {
        validationsList = new TreeSet<IValidationMethod>();
        methodValidations.put(fieldName, validationsList);
        final IFieldModel model = getModelByName(fieldName);
        if (model != null) {
          model.addFieldEventListener(new ValidationHandler(validationsList, model));
        }
      }
      IValidationMethod validationMethod = new ValidationMethod(fieldNames, runnable, validationsList.size() + 1);
      validationsList.add(validationMethod);
    }
  }

  
  @Override
  public Collection<IObjectModel> getChildren() {
    return memberModels.values();
  }

  
  @SuppressWarnings("unchecked")
  @Override 
  public <X extends IObjectModel> X getMember (String name) {
    return (X)memberModels.get(name);
  }

  
  /**
   * Return an array of object models that match the "applies to" 
   * string.
   * <p>
   * The current implementation of this method treats the "applies to"
   * string as a simple field name and returns an array of 0 or 1 
   * object models, depending on whether the "applies to" argument
   * identifies zero or one member.
   * @param appliesTo
   * @return
   */
  @Override 
  public IObjectModel[] getMatchingMembers (String appliesTo) {
    List<IObjectModel> members = selectObjectModels(appliesTo);
    IObjectModel[] mx = new IObjectModel[members.size()];
    return members.toArray(mx);
  }


  /**
   * Returns the group plan used to construct this model.  This method is only used for testing.  It should not be
   * called by application code.
   */
  public IGroupPlan getPlan () {
    return groupPlan;
  }
  
  
//  @Override
//  public void fireInitialEvents() {
//    // TODO Auto-generated method stub
//  }


  public Object getFieldValue () {
    return instance;
//    try {
//      return field.get(instance);
//    } catch (IllegalArgumentException ex) {
//      throw new RuntimeException(ex);
//    } catch (IllegalAccessException ex) {
//      throw new RuntimeException(ex);
//    }
  }
  
  
  @Override
  public void dump(int level) {
    for (int i = 0; i < level; i++) {
      System.out.print("  ");
    }
    if (getFieldValue() == null) {
      System.out.println(getPathName() + " " + getEffectiveMode() + " = null");
    } else {
      System.out.println(getPathName() + " " + getEffectiveMode() + " {");
    }
    for (IObjectModel model : getChildren()) {
      model.dump(level + 1);
    }
    for (int i = 0; i < level; i++) {
      System.out.print("  ");
    }
    System.out.println("}");
  }


  @Override
  public void fireInitialFieldEvents (FieldEventListener x, boolean isSourceTrigger) {
    for (IObjectModel model : getChildren()) {
      model.fireInitialFieldEvents(x, isSourceTrigger);
    }
  }


  @Override
  public Object getUnderlyingValue(Object parentRef) {
    Field field = (Field)parentRef;
    field.setAccessible(true);
    try {
      return field.get(instance);
    } catch (IllegalArgumentException ex) {
      throw new RuntimeException(ex);
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }


  @Override
  public void setUnderlyingValue(Object parentRef, Object value) {
    Field field = (Field)parentRef;
    field.setAccessible(true);
    try {
      field.set(instance, value);
    } catch (IllegalArgumentException ex) {
      throw new RuntimeException(ex);
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }


  @Override
  public String getLabel() {
    return groupPlan.getStaticLabel();
  }

  
  @Override
  public Object getKey(IObjectModel child) {
    Object key = memberModels.getKey(child);
    if (key != null) {
      return key;
    }
    throw new IllegalArgumentException(child.toString());
  }

}
