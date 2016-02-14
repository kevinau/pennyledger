package org.pennyledger.form.model;

import org.pennyledger.form.type.IType;
import org.pennyledger.form.plan.impl.ObjectPlan;
import org.pennyledger.util.UserEntryException;
import org.pennyledger.util.UserEntryException.Type;

public interface IFieldModel extends IObjectModel {

  public static class UnknownValue {
    @Override
    public String toString () {
      return "UNKNOWN";
    }
  }

  
  public static final Object UNKNOWN = new UnknownValue();

  
  /**
   * Add an EntryModeListener.
   */
  public void addFieldEventListener(FieldEventListener x);

  
  public void clearError (Object source);
  
  
  //public void fireErrorCleared ();
  
  
  //public void fireErrorNoted (UserEntryException userError);

    
  public ComparisonBasis getCompareBasis();
  
  
  public Object getDefaultValue ();


  public UserEntryException[] getErrors();
  
  
  public ObjectPlan getFieldPlan ();
  
  
  public int getId();
  

  public String getLabel();
  
  
  public Object getReferenceValue ();
  

  public String getSource();
  
    
  public Type getStatus ();
    
  
  public Type getStatus (int order);
 
  
  public String getStatusMessage ();
  
  
  public IType<?> getType();

  
  public Object getValue();
  
  
  public boolean isComparedSourceEqual();
  
  
  public boolean isComparedValueEqual();
  
  
  public boolean isInError();
  
  
  public boolean isOptional();
  
  
  public void noteValidationError (Object source, Exception ex);

  
  public void noteValidationError (Object source, IFieldModel[] mx, UserEntryException userError);
  
 
  public void noteValidationError (Object source, UserEntryException userError);
  
  
  /**
   * Remove an EntryModeListener.
   */
  public void removeFieldEventListener(FieldEventListener x);
  
  
  public void setCompareBasis(ComparisonBasis compareBasis);
  
  
  /**
   * Set the default value for this field.
   * <p>
   * If the comparison basis for this field is DEFAULT, and changing the default
   * value changes the compared-equality, a compareEqualityChange event will be
   * fired.
   */
  public void setDefaultValue (Object value);

  
  public void setLastEntryValue (Object value);

  
  public void setReferenceFromValue();


  /**
   * Set the reference value for this field.
   * <p>
   * If the comparison basis for this field is REFERENCE, and changing the reference
   * value changes the compared-equality, a compareEqualityChange event will be
   * fired.
   */
  public void setReferenceValue (Object value);
  
  
  public void setSourceFromValue (Object value);

  
  public void setValue (Object value);


  public void setValueFromDefault();


  public void setValueFromDefaultOrReference();


  public void setValueFromPrime();


  public void setValueFromReference ();


  public void setValueFromSource (String sv);


  public void setValueFromSource (String sv, FieldEventListener self);


  public void setValueFromSource (String sv, FieldEventListener self, boolean creating);


  public String toEntryString(Object value);


  public void revalidate();

}