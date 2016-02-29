package org.pennyledger.form.path;

import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectWrapper;

public class Trail {

  private final Trail parent;
  private final IObjectWrapper wrapper;

  public Trail (IObjectWrapper wrapper) {
    this.parent = null;
    this.wrapper = wrapper;
  }
  
  Trail (Trail parent, IObjectWrapper wrapper) {
    this.parent = parent;
    this.wrapper = wrapper;
  }
  
  void visit(IObjectVisitable x) {
    if (parent != null) {
      parent.visit(x);
    }
    x.visit(wrapper);
  }
}

