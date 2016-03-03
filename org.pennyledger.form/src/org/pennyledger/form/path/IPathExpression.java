package org.pennyledger.form.path;

import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectModel;

public interface IPathExpression {

  public void dump(int level);

  public default void dump() {
    dump(0);
  }

  public default void indent(int level) {
    for (int i = 0; i < level; i++) {
      System.out.print("  ");
    }
  }

  public boolean matches(IObjectModel model, Trail trail, IObjectVisitable x);

  public boolean matches(IObjectModel model, IFieldVisitable x);

  public boolean matches(IObjectModel model, IObjectVisitable x);

}
