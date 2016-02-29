package org.pennyledger.form.path;

import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectWrapper;

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
  public boolean matches(IObjectWrapper wrapper, Trail trail, IObjectVisitable x) {
    matchDeep(wrapper, trail, x);
    return true;
  }
  
  private boolean matchDeep(IObjectWrapper wrapper, Trail trail, IObjectVisitable x) {
    Trail trail2 = new Trail(trail, wrapper);
    super.matches(wrapper, trail2, x);
    
    for (IObjectWrapper child : wrapper.getChildren()) {
      matchDeep(child, trail2, x);
    }
    return true;
  }
  
  @Override
  public boolean matches(IObjectWrapper wrapper, IFieldVisitable x) {
    matchDeep(wrapper, x);
    return true;
  }
  
  private boolean matchDeep(IObjectWrapper wrapper, IFieldVisitable x) {
    super.matches(wrapper, x);
    for (IObjectWrapper child : wrapper.getChildren()) {
      matchDeep(child, x);
    }
    return true;
  }
  
}
