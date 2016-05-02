package org.pennyledger.object.plan;

public interface IRuntimeProvider {

  /**
   * Get a list of XPaths expressions that identify the fields that this plan
   * applies to. All matching fields will use the same getDefaultValue method.
   * The list should never be empty, but there is no problem if it is. The
   * XPaths here are relative to the control which contains the
   * IDefaultProviderMethod.
   * 
   * @return list of XPath expressions
   */
  public String[] getAppliesTo();


  public boolean appliesTo (String name);
  

  /**
   * Get a list of field names that the getDefaultValue method depends on. Some
   * implementations may compute this from the code of the getDefaultValue
   * method, others will specify it explicitly. The names here are relative to
   * the control which contains the IDefaultProviderMethod.
   * 
   * @return list of field names
   */
  public String[] getDependsOn();

  /**
   * Does this provider return an initial mode, or does it only return a 
   * mode during runtime.  A provider that returns a mode at runtime requires
   * an instance value.
   * 
   * @return
   */
  public boolean isRuntime();
  
}
