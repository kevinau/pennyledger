package org.pennyledger.form.plan;

import org.pennyledger.form.type.IType;

public interface IFieldPlan extends IObjectPlan {

  public IType<?> getType();

  //public Object getStaticDefaultValue();

}
