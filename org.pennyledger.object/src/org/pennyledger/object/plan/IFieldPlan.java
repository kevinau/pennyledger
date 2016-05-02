package org.pennyledger.object.plan;

import java.lang.annotation.Annotation;

import org.pennyledger.object.type.IType;

public interface IFieldPlan<T> extends IObjectPlan {

  /** 
   * The type of this input field.
   */
  public IType<T> getType();

  /**
   * A convenience method that returns an Annotation for this input field.
   */
  public <A extends Annotation> A getAnnotation(Class<A> klass);

  /**
   * Is an empty input field acceptable.  If this is true, an empty input field is acceptable
   * and the resultant field value is <code>null</code>.  If this is false, an empty input field
   * is reported as an error.
   * <p>
   * Note that, it is possible that the input checking of IType does not allow an empty input field.  If
   * this method returns true, the error checking of IType is bypassed and the field value is 
   * set to <code>null</code>.
   * <p>
   * For primitive Java types, this method always returns false.
   */
  public boolean isNullable();

}
