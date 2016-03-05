package org.pennyledger.form.plan;

import java.lang.annotation.Annotation;

import org.pennyledger.form.type.IType;

public interface IFieldPlan extends IObjectPlan {

  public IType<?> getType();

  public <A extends Annotation> A getAnnotation(Class<A> klass);

  //public Object getStaticDefaultValue();

}
