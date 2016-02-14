package org.pennyledger.form.model;

import java.util.List;


public interface IListModel extends IContainerModel {

  public IObjectModel getMember(int index);

  public List<IObjectModel> getAllMembers();

  public void setListSize(int size);

  public int getListSize();

  /**
   * Is the list composed of only single fields or is it
   * composed of container fields.
   */
  public boolean isSimpleList();
  
}
