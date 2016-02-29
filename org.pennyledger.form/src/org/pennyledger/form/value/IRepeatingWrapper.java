package org.pennyledger.form.value;

public interface IRepeatingWrapper extends IObjectWrapper {
  
  public int getSize();

  public void setRepeatingSize(int size);

  public IObjectWrapper getMember(int i);
  
  public int indexOf (IObjectWrapper child);
  
}
