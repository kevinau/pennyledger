package org.pennyledger.form.model;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.pennyledger.form.type.IType;
import org.pennyledger.form.plan.IValidationMethod;
import org.pennyledger.form.plan.impl.FieldPlan;
import org.pennyledger.util.UserEntryException;
import org.pennyledger.util.UserEntryException.Type;

public class FieldModel extends ObjectModel implements IFieldModel, EffectiveModeListener {

  private static class ErrorInstance {
    private IFieldModel[] models;
    private UserEntryException exception;
  
    private ErrorInstance (IFieldModel[] models, UserEntryException exception) {
      this.models = models;
      this.exception = exception;
    }
  }

  private static AtomicInteger idFactory = new AtomicInteger();

  private final Map<Object, ErrorInstance> validationErrors = new HashMap<Object, ErrorInstance>();

  private final FieldPlan fieldPlan;
  private final IType<Object> type;
  private final int id;

  private Object defaultValue;
  private String defaultSource = "";
  private boolean isComparedValueEqual = true;
  private boolean isComparedSourceEqual = true;
  private boolean allowEntryEvents = false;

  private Object referenceValue;
  private String referenceSource = "";
  
  private ComparisonBasis comparisonBasis = ComparisonBasis.DEFAULT;
  
  private Object parentInstance;
  
  private Object fieldValue = UNKNOWN;
  private String fieldSource = "";
  
  
  @SuppressWarnings("unchecked")
  public FieldModel (IFormModel<?> ownerForm, IContainerModel parentModel, Object parentRef, String name, FieldPlan fieldPlan) {
    super(ownerForm, parentModel, parentRef, name, fieldPlan);
    this.fieldPlan = fieldPlan;
    this.type = (IType<Object>)fieldPlan.getType();
    this.id = idFactory.getAndIncrement();
    
    // Add event listener so this field can react to effective mode changes
    addEffectiveModeListener(this);
    setInitialDefaultValue();
  }

  
  public FieldModel (IFormModel<?> ownerForm, IContainerModel parentModel, Object parentRef, String name, FieldPlan fieldPlan, Object value) {
    this (ownerForm, parentModel, parentRef, name, fieldPlan);
    setValue(value);
  }

  
  @Override
  public FieldPlan getFieldPlan () {
    return fieldPlan;
  }
  
  
  @Override
  public boolean isFieldModel () {
    return true;
  }
  
  
  @Override
  public int getId () {
    return id;
  }
  
  
  /**
   * Add a FieldChangeListener.  
   * //If the conditions are right, fire off an event immediately.
   */
  @Override
  public void addFieldEventListener (FieldEventListener x) {
    super.addEffectiveModeListener(x);
    super.addFieldEventListener(x);
//    
//    if (isEventsActive()) {
//      // Fire all applicable events
//      x.sourceChange(this);
//      if (allowEntryEvents) {
//        x.comparisonBasisChange(this);
//        x.compareShowingChange(this, false);
//        x.compareEqualityChange(this);
//        if (validationErrors.isEmpty()) {
//          x.valueChange(this);
//          x.errorCleared(this);
//        } else {
//          for (ErrorInstance ei : validationErrors.values()) {
//            x.errorNoted(this, ei.exception);
//          }
//        }
//      } else {
//        x.valueChange(this);
//      }
//    }
  }
  
  
  /**
   * Clear an error identified by the sourceRef.  This method should only
   * be called if the Mode for the field allows entry events.
   */
  @Override
  public void clearError (Object sourceRef) {
    ErrorInstance error = validationErrors.remove(sourceRef);
    if (error != null && validationErrors.isEmpty()) {
      fireErrorCleared(this);
    }
  }


  private void clearDependentValidationErrors (Object sourceRef) {
    ErrorInstance error = validationErrors.remove(sourceRef);
    if (error != null) {
      IFieldModel[] mx = error.models;
      if (mx == null) {
        clearError(sourceRef);
      } else {
        for (IFieldModel m : mx) {
          m.clearError(sourceRef);
        }
      }
    }
  }


  /**
   * Note a validation error. This method should only be called if the Mode for
   * the field allows entry events.
   */
 public void noteValidationError (UserEntryException error) {
    IFieldModel[] mx = {this};
    noteValidationError(this, mx, error);
  }


  /**
   * Note a validation error identified by sourceRef. This method should only be called if the Mode for
   * the field allows entry events.
   */
  @Override
  public void noteValidationError (Object sourceRef, Exception ex) {
    IFieldModel[] mx = {this};
    UserEntryException userError = new UserEntryException(ex.getMessage());
    noteValidationError(sourceRef, mx, userError);
  }


  /**
   * Note an error for this object.  The error is reported on the model that is associated with this
   * object.
   */
  @Override
  public void noteValidationError (Object sourceRef, UserEntryException userError) {
    IFieldModel[] mx = {this};
    noteValidationError(sourceRef, mx, userError);
  }


  /**
   * Note an error on an array of field models.
   * @param source - the source of the error.  This uniquely identifies this error.  The same object
   * must be used to clear the error.
   * @param models - an array of field models on which the error is noted.
   * @param userError - the error that is noted.
   */
  @Override
  public void noteValidationError (Object sourceRef, IFieldModel[] mx, UserEntryException userError) {
    ErrorInstance error = new ErrorInstance(mx, userError);
    validationErrors.put(sourceRef, error);
    
    if (allowEntryEvents) {
      fireErrorNoted(this, userError);
    }
  }

  
  public void noteConversionError (Object source, UserEntryException userError) {
    IFieldModel[] mx = {this};
    ErrorInstance error = new ErrorInstance(mx, userError);
    validationErrors.put(source, error);
    Object[] sources = validationErrors.keySet().toArray();
    for (Object ss : sources) {
      //if (ss.equals(source)) {
      //  // Leave this conversion error
      //} else
      if (ss instanceof IValidationMethod) {
        clearDependentValidationErrors(ss);
      }
    }
    fireErrorNoted(this, userError);
  }


  @Override
  public Object getDefaultValue() {
    return defaultValue;
  }


  @Override
  public Object getReferenceValue() {
    return referenceValue;
  }


  @Override
  public Object getValue () {
    return fieldValue;
  }
  
  
  @Override
  public boolean isComparedValueEqual() {
    return isComparedValueEqual;
  }


  @Override
  public boolean isComparedSourceEqual() {
    return isComparedSourceEqual;
  }


  @Override 
  public ComparisonBasis getCompareBasis() {
    return comparisonBasis;
  }
  
  
  void setInitialDefaultValue () {
    defaultValue = fieldPlan.getStaticDefaultValue();
    defaultSource = type.toEntryString(defaultValue, null);
  }
  
  
  @Override
  public void setDefaultValue (Object value) {
    boolean defaultWasShowing = false;
    if (comparisonBasis == ComparisonBasis.DEFAULT) {
      defaultWasShowing = (defaultValue == null ? fieldValue == null : defaultValue.equals(fieldValue));
    }
    defaultValue = value;
    defaultSource = type.toEntryString(value, null);
    if (comparisonBasis == ComparisonBasis.DEFAULT) {
      if (defaultWasShowing) {
        setRawValue(value, null, true);
      } else {
        testAndFireSourceEqualityChange(false);
        if (validationErrors.isEmpty()) {
          testAndFireValueEqualityChange();
        }
      }
    }
  }

  
  @Override
  public void setReferenceValue (Object value) {
    boolean referenceWasShowing = false;
    if (comparisonBasis == ComparisonBasis.REFERENCE) {
      referenceWasShowing = (referenceValue == null ? fieldValue == null : referenceValue.equals(fieldValue));
    }
    referenceValue = value;
    referenceSource = type.toEntryString(value, null);
    if (comparisonBasis == ComparisonBasis.REFERENCE) {
      if (referenceWasShowing) {
        setRawValue(value, null, true);
      } else {
        testAndFireSourceEqualityChange(false);
        if (validationErrors.isEmpty()) {
          testAndFireValueEqualityChange();
        }
      }
    }
  }


//  @Override
//  public void fireInitialEvents() {
//    super.fireInitialEvents();
//    if (fieldValue != IFieldModel.UNKNOWN) {
//      fireValueChange(fieldValue, fieldSource, null);
//      fireErrorCleared();
//    } else {
//      fireValueChangeAttempt(fieldSource);
//    }
//  }
  
  
  @Override 
  public String getSource () {
    return fieldSource;
  }
  
  
  @Override
  public void setSourceFromValue (Object value) {
    if (value != UNKNOWN) {
      fieldSource = type.toEntryString(value, null);
      setRawValue(value, null, false);
    }
  }
  
  
  @Override
  public void setValue(Object value) {
    String source = type.toEntryString(value, null);
    fieldSource = source;
    if (value == null ? fieldValue != null : !value.equals(fieldValue)) {
      fireSourceChange(this, null);
    }
    
    testAndFireSourceEqualityChange(true);
    try {
      boolean creating = true;
      Object newValue = type.createFromString(source, null, fieldPlan.isOptional(), creating);
      setRawValue(newValue, null, true);
    } catch (UserEntryException ex) {
      fieldValue = UNKNOWN;
      testAndFireValueEqualityChange();
      noteValidationError (ex);
    }
  }

  
  @Override
  public void resetToInitial () {
    super.resetToInitial();
    setValue (defaultValue);
  }
  
  
  @Override
  public void setValueFromPrime () {
    Object primalValue = fieldPlan.getType().primalValue();
    String source = type.toEntryString(primalValue, null);
    fieldSource = source;
    if (primalValue == null ? fieldValue != null : !primalValue.equals(fieldValue)) {
      fireSourceChange(this, null);
    }
    
    testAndFireSourceEqualityChange(true);
    try {
      boolean creating = true;
      Object newValue = type.createFromString(source, null, fieldPlan.isOptional(), creating);
      setRawValue(newValue, null, true);
    } catch (UserEntryException ex) {
      fieldValue = UNKNOWN;
      testAndFireValueEqualityChange();
      noteValidationError (ex);
    }
  }
  
  
  @Override
  public void setValueFromDefault () {
    String source = type.toEntryString(defaultValue, null);
    fieldSource = source;
    if (defaultValue == null ? fieldValue != null : !defaultValue.equals(fieldValue)) {
      fireSourceChange(this, null);
    }
    
    testAndFireSourceEqualityChange(true);
    try {
      boolean creating = true;
      Object newValue = type.createFromString(source, null, fieldPlan.isOptional(), creating);
      setRawValue(newValue, null, true);
    } catch (UserEntryException ex) {
      fieldValue = UNKNOWN;
      testAndFireValueEqualityChange();
      noteValidationError (ex);
    }
  }
  
  
  @Override
  public void setValueFromReference () {
    String source = type.toEntryString(referenceValue, null);
    fieldSource = source;
    if (referenceValue == null ? fieldValue != null : !referenceValue.equals(fieldValue)) {
      fireSourceChange(this, null);
    }
    
    testAndFireSourceEqualityChange(true);
    try {
      boolean creating = true;
      Object newValue = type.createFromString(source, null, fieldPlan.isOptional(), creating);
      setRawValue(newValue, null, true);
    } catch (UserEntryException ex) {
      fieldValue = UNKNOWN;
      testAndFireValueEqualityChange();
      noteValidationError (ex);
    }
  }
  
  
  @Override
  public void setReferenceFromValue () {
    if (fieldValue != UNKNOWN) {
      setReferenceValue (fieldValue);
    }
  }
  
  
  @Override
  public void setValueFromDefaultOrReference () {
    switch (comparisonBasis) {
    case NONE :
      setValueFromPrime();
      break;
    case DEFAULT :
      setValueFromDefault();
      break;
    case REFERENCE :
      setValueFromReference();
      break;
    }
  }

  
  @Override
  public void setValueFromSource(String source, FieldEventListener self, boolean creating) {
    if (!fieldSource.equals(source)) {
      fieldSource = source;
      fireSourceChange(this, self);
    } else {
      fieldSource = source;
    }
    
    testAndFireSourceEqualityChange(true);
    try {
      Object newValue = type.createFromString(source, null, fieldPlan.isOptional(), creating);
      setRawValue (newValue, self, true);
    } catch (UserEntryException ex) {
      fieldValue = UNKNOWN;
      testAndFireValueEqualityChange();
      // noteConversionError includes the processing of noteValidationError
      noteConversionError (this, ex);
    }
  }

  
  @Override
  public void setValueFromSource(String source, FieldEventListener self) {
    setValueFromSource(source, null, true);
  }


  @Override
  public void setValueFromSource(String source) {
    setValueFromSource(source, null, true);
  }


  private void setRawValue (Object value, FieldEventListener self, boolean fireValueChangeEvents) {
    // Clear any conversion error, but do not clear validation errors.  Validation
    // checking will not have been done as a consequence of conversion errors,
    // so we don't have to clear validation errors if we clear conversion errors.
    clearError(this);

    // Change the value in the instance
    if (fieldValue == UNKNOWN) {
      // The value is being set when previously it was in error, so let others know
      fieldValue = value;
      setUnderlyingValue(fieldValue);
      if (fireValueChangeEvents) {
        fireValueChange(this, self);
      }
    } else {
      if (value == null ? fieldValue != null : !value.equals(fieldValue)) {
        // Set the new value.
        fieldValue = value;
        setUnderlyingValue(fieldValue);
        // Firing a value change will trigger validation
        if (fireValueChangeEvents) {
          fireValueChange(this, self);
        }
      }
    }
    testAndFireValueEqualityChange();
  }
  
  
  @Override
  public String toEntryString (Object value) {
    return type.toEntryString(value, null);
  }
  
  
  private void testAndFireValueEqualityChange () {
    if (allowEntryEvents) { 
      boolean ce;
      if (validationErrors.isEmpty()) {
        if (fieldValue == UNKNOWN) {
          ce = true;
        } else {
          switch (comparisonBasis) {
          case DEFAULT :
            ce = defaultValue == null ? fieldValue == null : defaultValue.equals(fieldValue);
            break;
          case REFERENCE :
            ce = referenceValue == null ? fieldValue == null : referenceValue.equals(fieldValue);
            break;
          default :
            ce = true;
            break;
          }
        }
      } else {
        ce = true;
      }
      if (isComparedValueEqual != ce) {
        isComparedValueEqual = ce;
        fireCompareEqualityChange(this); 
      }
    }
  }
  
  
  private void testAndFireSourceEqualityChange (boolean isSourceTrigger) {
    if (allowEntryEvents) { 
      boolean cs;
      switch (comparisonBasis) {
      case DEFAULT :
        cs = defaultSource.equals(fieldSource);
        break;
      case REFERENCE :
        cs = referenceSource.equals(fieldSource);
        break;
      default :
        cs = true;
        break;
      }
      if (isComparedSourceEqual != cs) {
        isComparedSourceEqual = cs;
        fireCompareShowingChange(this, isSourceTrigger);
      }
    }
  }
  
  
  @Override
  public void modeChange (IObjectModel model) {
    boolean aee = getEffectiveMode().allowEntryEvents();
    if (allowEntryEvents != aee) {
      allowEntryEvents = aee;
      if (allowEntryEvents) {
        // Fire all initial events
        testAndFireSourceEqualityChange(false);
        testAndFireValueEqualityChange();
//        if (errorStateChange) {
//          if (validationErrors.isEmpty()) {
//            fireErrorCleared();
//          } else {
//            for (ErrorInstance ei : validationErrors.values()) {
//              fireErrorNoted(ei.exception);
//            }
//          }
//        }
      } else {
        // Clear field events
        if (!isComparedSourceEqual) {
          isComparedSourceEqual = true;
          fireCompareShowingChange(this, false);
        }
        if (!isComparedValueEqual) {
          isComparedValueEqual = true;
          fireCompareEqualityChange(this);
        }
        if (!validationErrors.isEmpty()) {
          validationErrors.clear();
          fireErrorCleared(this);
        }
      }
    }
  }
 
  
  @Override
  public void setCompareBasis (ComparisonBasis compareBasis) {

    if (this.comparisonBasis != compareBasis) {
//      // Clear any "before" showing and equality
//      if (isCompareShowing == false) {
//        isCompareShowing = true;
//        fireCompareShowingChange(isCompareShowing, false);
//      }
//      if (isCompareEqual == false) {
//        isCompareEqual = true;
//        fireCompareEqualityChange();
//      }
//      
      this.comparisonBasis = compareBasis;

      // Set the showing and equality based on the new comparison basis
      testAndFireSourceEqualityChange(false);
      testAndFireValueEqualityChange();
    }
  }
  

//  @Override
//  protected void setEffectiveMode (EffectiveMode newEffectiveMode, boolean fireEvents) {
//    EffectiveMode oldEffectiveMode = getEffectiveMode();
//    boolean postProcess = false;
//    if (newEffectiveMode != oldEffectiveMode) {
//      if (newEffectiveMode == EffectiveMode.NA) {
//        // We are changing to NA, and we want to ignore any errors (we still capture them,
//        // but we do not report them).  Ditto default showing and reference showing.
//        
//        // Clear errors
//        if (!validationErrors.isEmpty()) {
//          fireErrorCleared();
//        }
////        if (isCompareShowing) {
////          fireCompareShowingChange(false, false);
////        }
//      }
//      if (oldEffectiveMode == EffectiveMode.NA) {
//        postProcess = true;
//      }
//    }
//    super.setEffectiveMode(newEffectiveMode, fireEvents);
//    if (postProcess) {
//      // We are changing from NA to something else, so report any errors.
//      Collection<ErrorInstance> errors = validationErrors.values();
//      for (ErrorInstance error : errors) {
//        fireErrorNoted(error.exception);
//      }
////      if (isCompareShowing) {
////        fireCompareShowingChange(true, false);
////      }
//    }
//  }
  
  
  @Override
  public boolean isInError () {
    EffectiveMode effectiveMode = getEffectiveMode();
    if (effectiveMode == EffectiveMode.NA) {
      return false;
    } else {
      Collection<ErrorInstance> errors = validationErrors.values();
      for (ErrorInstance error : errors) {
        UserEntryException ev = error.exception;
        Type type = ev.getType();
        if (type.isFatal()) {
          return true;
        }
      }
      return false;
    }
  }
  
  
  @Override
  public Type getStatus() {
    EffectiveMode effectiveMode = getEffectiveMode();
    if (effectiveMode == EffectiveMode.NA) {
      return Type.OK;
    } else {
      int incompleteCount = 0;
      int requiredCount = 0;
      int warningCount = 0;
      Collection<ErrorInstance> errors = validationErrors.values();
      for (ErrorInstance error : errors) {
        UserEntryException ev = error.exception;
        Type type = ev.getType();
        switch (type) {
        case ERROR:
          return Type.ERROR;
        case INCOMPLETE:
          incompleteCount++;
          break;
        case REQUIRED:
          requiredCount++;
          break;
        case WARNING:
          warningCount++;
          break;
        case OK :
          break;
        }
      }
      if (incompleteCount > 0) {
        return Type.INCOMPLETE;
      }
      if (requiredCount > 0) {
        return Type.REQUIRED;
      }
      if (warningCount > 0) {
        return Type.WARNING;
      }
      return Type.OK;
    }
  }  
  
  
  @Override
  public void fireInitialFieldEvents (FieldEventListener x, boolean isSourceTrigger) {
    x.sourceChange(this);

    if (allowEntryEvents) {
      x.comparisonBasisChange(this);
      x.compareShowingChange(this, isSourceTrigger);
      x.compareEqualityChange(this);
      if (validationErrors.isEmpty()) {
        x.valueChange(this);
        x.errorCleared(this);
      } else {
        for (ErrorInstance ei : validationErrors.values()) {
          x.errorNoted(this, ei.exception);
        }
      }
    } else {
      x.valueChange(this);
    }
  }

  
  @Override
  public Type getStatus (int order) {
    int incompleteCount = 0;
    int requiredCount = 0;
    int warningCount = 0;
    for (Map.Entry<Object, ErrorInstance> entry : validationErrors.entrySet()) {
      if (entry.getKey() instanceof IValidationMethod) {
        IValidationMethod vMethod = (IValidationMethod)entry.getKey();
        if (vMethod.getOrder() >= order) {
          // Ignore errors that will be re-checked later, and, if no longer an error,
          // it will be cleared.
          continue;
        }
      }
      UserEntryException ev = entry.getValue().exception;
      Type type = ev.getType();
      switch (type) {
      case ERROR :
        return Type.ERROR;
      case INCOMPLETE :
        incompleteCount++;
        break;
      case REQUIRED :
        requiredCount++;
        break;
      case WARNING :
        warningCount++;
        break;
      case OK :
        break;
      }
    }
    if (incompleteCount > 0) {
      return Type.INCOMPLETE;
    }
    if (requiredCount > 0) {
      return Type.REQUIRED;
    }
    if (warningCount > 0) {
      return Type.WARNING;
    }
    return Type.OK;
  }
  
  /*
   * TODO This needs to be changed to be presentation agnostic.
   */
  @Override
  public String getStatusMessage () {
    int n = validationErrors.size();
    if (n == 0) {
      return "";
    } else if (n == 1) {
      UserEntryException ex = validationErrors.values().iterator().next().exception;
//      return ex.getType().getPrefix() + ": " + ex.getMessage();
      return ex.getMessage();
    } else {
      StringBuilder buffer = new StringBuilder();
      buffer.append("<html>");
      Type tx = null;
      for (ErrorInstance error : validationErrors.values()) {
        UserEntryException ex = error.exception;
        if (tx != ex.getType()) {
          if (tx != null) {
            buffer.append("<br>\n");
          }
//          tx = ex.getType();
//          buffer.append(tx.getPrefix());
//          buffer.append(": ");
        }
        buffer.append("<br>- ");
        buffer.append(ex.getMessage());
      }
      buffer.append("</html>");
      return buffer.toString();
    }
  }


  @Override
  public UserEntryException[] getErrors () {
    int n = validationErrors.size();
    UserEntryException[] errors = new UserEntryException[n];
    int i = 0;
    for (ErrorInstance error : validationErrors.values()) {
      errors[i++] = error.exception;
    }
    return errors;
  }


  @Override
  public void removeFieldEventListener(FieldEventListener x) {
    super.removeFieldEventListener(x);
    super.removeEffectiveModeListener(x);
    // Notify listeners if this model was in error
    for (Object source : validationErrors.values()) {
      clearError(source);
    }
  }


  @Override
  public void setLastEntryValue(Object value) {
    Field lastEntryField = fieldPlan.getLastEntryField();
    if (lastEntryField != null) {
      try {
        lastEntryField.set(parentInstance, value);
      } catch (IllegalArgumentException ex) {
        throw new RuntimeException(ex);
      } catch (IllegalAccessException ex) {
        throw new RuntimeException(ex);
      }
    }
  }


  @Override
  public void dump(int level) {
    for (int i = 0; i < level; i++) {
      System.out.print("  ");
    }
    System.out.println(getPathName() + " = " + getValue() + " [" + super.getEffectiveMode() + " " + fieldPlan + "]");
  }


  @Override
  public Collection<IObjectModel> getChildren() {
    return Collections.emptyList();
  }


  @Override
  protected void accumulateFields (List<IFieldModel> list) {
    list.add(this);
  }
  
  
  @Override
  public Object getUnderlyingValue(Object parentRef) {
    throw new RuntimeException("This method should not be called");
  }


  @Override
  public void setUnderlyingValue(Object parentRef, Object value) {
    throw new RuntimeException("This method should not be called");
  }

  
  @Override
  public boolean walkFields (IFieldVisitable x) {
    return x.visit(this);
  }


  @Override
  public String getLabel() {
    return fieldPlan.getStaticLabel();
  }

  @Override
  public IType<?> getType() {
    return fieldPlan.getType();
  }
  
  
  @Override
  public boolean isOptional () {
    return fieldPlan.isOptional();
  }
  
  
  @Override
  public void revalidate () {
    fireValueChange(this, null);
  }
  
  
  @Override
  public void setEventsActive (boolean fireEvents) {
    super.setEventsActive(fireEvents);
    if (fireEvents) {
      fireSourceChange(this, null);

      if (allowEntryEvents) {
        fireComparisonBasisChange(this);
        fireCompareShowingChange(this, false);
        fireCompareEqualityChange(this);
        if (validationErrors.isEmpty()) {
          fireValueChange(this, null);
          fireErrorCleared(this);
        } else {
          for (ErrorInstance ei : validationErrors.values()) {
            fireErrorNoted(this, ei.exception);
          }
        }
      } else {
        fireValueChange(this, null);
      }

    }
  }
  
}
