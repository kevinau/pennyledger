package org.pennyledger.form.model;


public interface IArrayModel extends IContainerModel {

  public IObjectModel getMember(int index);

  public IObjectModel[] getAllMembers();

  public void setArraySize(int size);

  public int getArraySize();
  
  public boolean isSimpleArray();

}
