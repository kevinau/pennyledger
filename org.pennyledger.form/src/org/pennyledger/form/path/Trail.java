package org.pennyledger.form.path;

import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectModel;

public class Trail {

  private final Trail parent;
  private final IObjectModel model;

  public Trail (IObjectModel model) {
    this.parent = null;
    this.model = model;
  }
  
  Trail (Trail parent, IObjectModel model) {
    this.parent = parent;
    this.model = model;
  }
  
  void visit(IObjectVisitable x) {
    if (parent != null) {
      parent.visit(x);
    }
    x.visit(model);
  }
}

