package org.pennyledger.form.path;

import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IFieldModel;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectModel;

public class StepPath implements IPathExpression {

  private final StepPath parent;
  protected IPathExpression next = null;
  
  public StepPath (StepPath parent) {
    this.parent = parent;
    this.parent.next = this;
  }
    
  public StepPath () {
    this.parent = null;
  }
    
  @Override
  public void dump (int level) {
    indent(level);
    System.out.println("/");
    if (next != null) {
      next.dump(level + 1);
    }
  }
  
  @Override
  public boolean matches(IObjectModel model, Trail trail, IObjectVisitable x) {
    if (next != null) {
      return next.matches(model, trail, x);
    } else {
      //trail.visit(x);
      x.visit(model);
      return true;
    }
  }

  @Override
  public boolean matches(IObjectModel model, IFieldVisitable x) {
    if (next != null) {
      return next.matches(model, x);
    } else {
      if (model.isField()) {
        x.visit((IFieldModel)model);
      }
      return true;
    }
  }

  @Override
  public boolean matches(IObjectModel model, IObjectVisitable x) {
    if (next != null) {
      return next.matches(model, x);
    } else {
      x.visit((IFieldModel)model);
      return true;
    }
  }

}
