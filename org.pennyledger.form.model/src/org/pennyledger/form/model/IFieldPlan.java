package org.pennyledger.form.model;

import org.pennyledger.form.type.IType;

public interface IFieldPlan<S> extends IObjectPlan {

  public IType<S> getType();

  //public Object getStaticDefaultValue();

}
