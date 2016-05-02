/*******************************************************************************
 * Copyright  2012 Kevin Holloway (kholloway@geckosoftware.co.uk).
 *
 * Licensed under the EUPL, Version 1.1 only (the "Licence").  You may not use
 * this work except in compliance with the Licence.  You may obtain a copy of
 * the Licence at: http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 *
 * Contributors:
 *     Kevin Holloway - initial API and implementation
 *******************************************************************************/
package org.pennyledger.object;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormField {
  
  /**
   * The on screen label associated with this data entry field. If not supplied,
   * a default label is calculated from the field name (using camel case
   * conventions to break the name into words).
   */
  String label() default "\u0000";

  
  /**
   * A type that implements IType. This provides the necessary type information
   * for the field. Only the default constructor is used.
   */
  Class<?> type() default Void.class;

  
  /**
   * An optional field length.  This can be used in place of a full type for String 
   * based fields.  This overrides any explicit type information found according to
   * the rules for type() described above.  It is also possible to specify a length
   * on a javax.persistance.Column.  It is an error if both are specified with different
   * values.
   */
  int length() default -1;

  /**
   * An optional regex pattern. This can be used in place of a full type for
   * String based fields.
   */
  String pattern() default "";

  
  /**
   * Change the case of entered text.  This can be used in lieu of a pattern.  If a pattern
   * is specified, this does not need to be set.
   * <p>
   * Note.  This annotation is called "xcase" rather than the more obvious "case" because "case"
   * is a reserved word in Java. 
   */
  TextCase xcase() default TextCase.UNSPECIFIED;
  
  
  /**
   * An optional message that should be associated with the pattern. This can be
   * used in place of a full type for String based fields.
   */
  String targetName() default "";
  
  
  NumberSign sign() default NumberSign.UNSPECIFIED;
  
  
  /**
   * An optional precision. This can be used in place of a full type for numeric
   * fields. It specifies the number of digits in a number. This overrides any
   * explicit type information found according to the rules for type() described
   * above.  It is also possible to specify a precision
   * on a javax.persistance.Column.  It is an error if both are specified with different
   * values.
   */
  int precision() default -1;
  
  
  /**
   * An optional scale. This can be used in place of a full type for numeric
   * fields. It specifies the number of digits that are allowed to the right of
   * the decimal digit. This overrides any explicit type information found
   * according to the rules for type() described above.  It is also possible to specify a scale
   * on a javax.persistance.Column.  It is an error if both are specified with different
   * values.
   */
  int scale() default -1;
  
  long min() default Long.MIN_VALUE;
  
  long max() default Long.MAX_VALUE;
  
//  /**
//   * A field, method or value that returns whether the data entry is being
//   * compared against a prior value. This annotation is also available on data
//   * entry panels ({@see Panel}).
//   */
//  String compareMode() default "";
//
//  /**
//   * A field or method that returns a prior value for the data entry field. The
//   * field or return type must match the type of the data entry field. This
//   * annotation is also available on data entry panels ({@see Panel}).
//   */
//  String priorValue() default "";

  boolean unique() default false;
  
  Nullable nullable() default Nullable.UNSPECIFIED;
  
}
