package org.pennyledger.form.plan;

import org.pennyledger.form.type.IType;

public interface IFieldPlan extends IObjectPlan {

  public boolean isOptional();

  public IType<?> getType ();

}
