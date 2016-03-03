package org.pennyledger.form.path;

import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectModel;

public class WildcardPath extends StepPath implements IPathExpression {

  public WildcardPath (StepPath parent) {
    super(parent);
  }
  
  @Override
  public void dump(int level) {
    indent(level);
    System.out.println("*");
    super.dump(level + 1);
  }

  @Override
  public boolean matches(IObjectModel model, Trail trail, IObjectVisitable x) {
    for (IObjectModel child : model.getChildren()) {
      super.matches(child, new Trail(trail, child), x);
    }
    return true;
  }

  @Override
  public boolean matches(IObjectModel model, IFieldVisitable x) {
    for (IObjectModel child : model.getChildren()) {
      super.matches(child, x);
    }
    return true;
  }

}
