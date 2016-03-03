package org.pennyledger.form.path;

import java.util.List;

import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectModel;

public class LastElementPath extends StepPath implements IPathExpression {

  public LastElementPath (StepPath parent) {
    super(parent);
  }
  
  @Override
  public void dump(int level) {
    indent (level);
    System.out.println("[last]");
    super.dump(level + 1);
  }

  @Override
  public boolean matches(IObjectModel model, Trail trail, IObjectVisitable x) {
    List<IObjectModel> children = model.getChildren();
    int n = children.size();
    if (n > 0) {
      IObjectModel last = children.get(n - 1);
      return super.matches(last, new Trail(trail, last), x);
    } else {
      return false;
    }
  }

  @Override
  public boolean matches(IObjectModel model, IFieldVisitable x) {
    List<IObjectModel> children = model.getChildren();
    int n = children.size();
    if (n > 0) {
      IObjectModel last = children.get(n - 1);
      return super.matches(last, x);
    } else {
      return false;
    }
  }

}
