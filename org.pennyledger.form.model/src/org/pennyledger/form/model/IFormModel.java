package org.pennyledger.form.model;

import org.pennyledger.form.EntryMode;


public interface IFormModel<T extends Object> extends IBaseModel {

  public static final String ROOT_NAME = "form";
  
  /**
   * Gets the name of the form.  This is often used as a tab identifier.
   * <p>
   * The name of the form is obtained as follows:
   * <ol>
   * <li>If there is a method that is marked with \@FormName, that method is
   * called and, if the result is not null, use that.</li>
   * <li>Otherwise, if the \@Form annotation has a name attribute, use that.</li>
   * <li>Otherwise, derive a name from the class name, replacing camel case with
   * sentence case (and a single space between the words).</li>
   * </ol>
   * 
   * @return the name of the form.  It is never <code>null</code>.
   */
  public String getFormName();

  
  /**
   * Gets the name of an resource that contains an icon or image associated with
   * this form.
   * 
   * @return the name of a resource, or null if the form does not specify an icon.
   */
  public String getIconName();


  ////////public MemberGroup getGroupPlan(Class<?> klass);


  public <X extends IContainerModel> X getRootModel();

  
  public void addEntryModeListener (EffectiveModeListener x);


  public void removeEntryModeListener (EffectiveModeListener x);


  public void addFieldEventListener (FieldEventListener x);


  public void removeFieldEventListener (FieldEventListener x);


  public void setInstance (T instance);
  
  
  public void setValueAndReference (T instance);
  
  
  public void setValueFromDefault ();
  

  public void setReferenceFromValue();


  public T getInstance ();


  public Class<T> getFormClass();


  public T newInstance();


  public T setNewInstanceFromDefaults();


  public void setModes (EntryMode mode, ComparisonBasis compareBasis);

    
  public IFieldModel[] getFieldModels();


  public void addValidationMethod(String[] fieldNames, IMethodRunnable runnable);


  public void addContainerEventListener(ContainerEventListener x);


  public void removeContainerEventListener(ContainerEventListener x);

}
