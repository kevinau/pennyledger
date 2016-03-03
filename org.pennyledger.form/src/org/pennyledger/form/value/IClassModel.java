package org.pennyledger.form.value;

import java.util.Map;

public interface IClassModel extends IObjectModel {

  public Map<String, IObjectModel> getMemberMap();

  public IObjectModel getMember (String name);
  
}
