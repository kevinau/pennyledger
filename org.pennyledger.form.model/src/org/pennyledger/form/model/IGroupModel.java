package org.pennyledger.form.model;



public interface IGroupModel extends IContainerModel {

  public <X extends IObjectModel> X getMember(String name);

  public IObjectModel[] getMatchingMembers(String appliesTo);

  public void addValidationMethod(String[] fieldNames, IMethodRunnable runnable);

  //public IGroupPlan getPlan();

}
