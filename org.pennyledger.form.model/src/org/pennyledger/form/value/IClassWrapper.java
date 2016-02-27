package org.pennyledger.form.value;

import java.util.Map;

public interface IClassWrapper extends IObjectWrapper {

  public Map<String, IObjectWrapper> getMemberMap();

  public IObjectWrapper getMember (String name);
  
}
