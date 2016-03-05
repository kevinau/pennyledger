package org.pennyledger.form.value;

public interface IRepeatingModel extends IContainerModel {
  
  public int getSize();

  public void setRepeatingSize(int size);

  public IObjectModel getMember(int i);
  
  public int indexOf (IObjectModel child);
  
}
