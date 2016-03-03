package org.pennyledger.form.path;

import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectModel;

public class DescendentPath extends StepPath implements IPathExpression {

  public DescendentPath (StepPath parent) {
    super(parent);
  }

  @Override
  public void dump(int level) {
    indent (level);
    System.out.println("//");
    super.dump(level + 1);
  }

  @Override
  public boolean matches(IObjectModel model, Trail trail, IObjectVisitable x) {
    matchDeep(model, trail, x);
    return true;
  }
  
  private boolean matchDeep(IObjectModel model, Trail trail, IObjectVisitable x) {
    Trail trail2 = new Trail(trail, model);
    super.matches(model, trail2, x);
    
    for (IObjectModel child : model.getChildren()) {
      matchDeep(child, trail2, x);
    }
    return true;
  }
  
  @Override
  public boolean matches(IObjectModel model, IFieldVisitable x) {
    matchDeep(model, x);
    return true;
  }
  
  private boolean matchDeep(IObjectModel model, IFieldVisitable x) {
    super.matches(model, x);
    for (IObjectModel child : model.getChildren()) {
      matchDeep(child, x);
    }
    return true;
  }
  
}
