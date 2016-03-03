package org.pennyledger.form.path;

import org.pennyledger.form.value.IClassModel;
import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectModel;

public class NamedMemberPath extends StepPath implements IPathExpression {

  private final String name;
  
  public NamedMemberPath (StepPath parent, String name) {
    super(parent);
    this.name = name;
  }
  
  @Override
  public void dump(int level) {
    indent (level);
    System.out.println(name);
    super.dump(level + 1);
  }

  @Override
  public boolean matches(IObjectModel model, Trail trail, IObjectVisitable x) {
    if (model.isClass()) {
      IClassModel classWrapper = (IClassModel)model;
      IObjectModel member = classWrapper.getMember(name);
      if (member == null) {
        return false;
      } else {
        return super.matches(member, new Trail(trail, member), x);
      }
    } else {
      return false;
    }
  }

  @Override
  public boolean matches(IObjectModel model, IFieldVisitable x) {
    if (model.isClass()) {
      IClassModel classWrapper = (IClassModel)model;
      IObjectModel member = classWrapper.getMember(name);
      if (member == null) {
        return false;
      } else {
        return super.matches(member, x);
      }
    } else {
      return false;
    }
  }
}
