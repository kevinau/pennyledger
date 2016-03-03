package org.pennyledger.form.path;

import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IFieldModel;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectModel;

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
  public boolean matches(IObjectModel model, Trail trail, IObjectVisitable x) {
    x.visit(model);
    trail.visit(x);
    return true;
  }

  @Override
  public boolean matches(IObjectModel model, IFieldVisitable x) {
    if (model.isField()) {
      x.visit((IFieldModel)model);
      return true;
    } else {
      return false;
    }
  }

}
