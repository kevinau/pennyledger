package org.pennyledger.form.path;

import java.util.List;

import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectModel;

public class IndexedElementPath extends StepPath implements IPathExpression {

  /**
   * This index is 1 based, to conform with the xpath specification.
   */
  private int index;
  
  public IndexedElementPath (StepPath parent, int index) {
    super(parent);
    this.index = index;
  }
  
  public IndexedElementPath (StepPath parent, String s) {
    this (parent, Integer.parseInt(s));
  }

  @Override
  public void dump(int level) {
    indent(level);
    System.out.println("[" + index + "]");
    super.dump(level + 1);
  }

  @Override
  public boolean matches(IObjectModel model, Trail trail, IObjectVisitable x) {
    List<IObjectModel> children = model.getChildren();
    int n = children.size();
    if (n > 0 && (index - 1) < n) {
      IObjectModel elem = children.get(index - 1);
      return super.matches(elem, new Trail(trail, elem), x);
    } else {
      return false;
    }
  }
  
  @Override
  public boolean matches(IObjectModel model, IFieldVisitable x) {
    List<IObjectModel> children = model.getChildren();
    int n = children.size();
    if (n > 0 && (index - 1) < n) {
      IObjectModel elem = children.get(index - 1);
      return super.matches(elem, x);
    } else {
      return false;
    }
  }
  
}
