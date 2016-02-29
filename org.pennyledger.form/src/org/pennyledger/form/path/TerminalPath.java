package org.pennyledger.form.path;

import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IFieldWrapper;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectWrapper;

public class TerminalPath extends StepPath implements IPathExpression {

  public TerminalPath (StepPath parent) {
    super(parent);
  }
  
  @Override
  public void dump(int level) {
    indent (level);
    System.out.println(".");
  }

  @Override
  public boolean matches(IObjectWrapper wrapper, Trail trail, IObjectVisitable x) {
    x.visit(wrapper);
    trail.visit(x);
    return true;
  }

  @Override
  public boolean matches(IObjectWrapper wrapper, IFieldVisitable x) {
    if (wrapper.isField()) {
      x.visit((IFieldWrapper)wrapper);
      return true;
    } else {
      return false;
    }
  }

}
