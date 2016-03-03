package org.pennyledger.form.path;

import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IFieldWrapper;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectWrapper;

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
  public boolean matches(IObjectWrapper wrapper, Trail trail, IObjectVisitable x) {
    if (next != null) {
      return next.matches(wrapper, trail, x);
    } else {
      //trail.visit(x);
      x.visit(wrapper);
      return true;
    }
  }

  @Override
  public boolean matches(IObjectWrapper model, IFieldVisitable x) {
    if (next != null) {
      return next.matches(model, x);
    } else {
      if (model.isField()) {
        x.visit((IFieldWrapper)model);
      }
      return true;
    }
  }

  @Override
  public boolean matches(IObjectWrapper model, IObjectVisitable x) {
    if (next != null) {
      return next.matches(model, x);
    } else {
      x.visit((IFieldWrapper)model);
      return true;
    }
  }

}
