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
package org.pennyledger.form;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormObject {
  
  /**
   * The on screen label associated with this data entry field. If not supplied,
   * a default label is calculated from the field name (using camel case
   * conventions to break the name into words).
   */
  String label() default "\u0000";

  /**
   * The initial data entry "mode" for the data
   * entry field. The "mode" is one of:
   * <ul>
   * <li>Inherit.  The data entry field is inherited from its parent panel
   * or form.</li>
   * <li>Hidden. The data entry field is not shown, and occupies no space on
   * the form.</li>
   * <li>View only. No changes are allowed.</li>
   * <li>Edit. A normal data entry field.</li>
   * </ul>
   * The entry mode of a field can be overridden by a entry mode method that
   * returns the entry mode for a collection of named fields.
   */
  EntryMode mode() default EntryMode.UNSPECIFIED;

  
  /**
   * Whether this field can be empty.  If nullable is true, an empty
   * data entry field is accepted and assigned a <code>null</code> value.  If 
   * nullable is false, an empty data entry field is only accepted if the 
   * field validation allows it, and even the, the field value is not not 
   * assigned a <code>null</code> value.  Some examples will make this clear:
   * <table>
   * <tr>
   * <th>Field type</th>
   * <th>nullable=true</th>
   * <th>nullable=false</th>
   * </tr>
   * <tr>
   * <td>Date</td>
   * <td>Empty data entry is allowed.  If empty, the resultant field value is <code>null</code></td>
   * <td>Empty data entry is not allowed.  The resultant field value is always a valid Date object</td>
   * </tr>
   * <tr>
   * <td>String</td>
   * <td>Empty data entry is allowed.  If empty, the resultant field value is <code>null</code></td>
   * <td>Empty data entry is only allowed if a zero length field is allowed.  If data entry
   * is empty, the field value is the zero length string "" (it is not <code>null</code>)</td>
   * </tr>
   * <tr>
   * <td>Integer</td>
   * <td>Empty data entry is allowed.  If empty, the resultant field value is <code>null</code></td>
   * <td>Empty data entry is not allowed.  The field value is always a valid Integer object</td>
   * </tr>
   * <tr>
   * <td>int</td>
   * <td><i>Not allowed.  An <code>int</code> field can never be <code>null</code></td>
   * <td>Empty data entry is not allowed.  The field value is an int value</td>
   * </tr>
   * </table>
   */
  boolean nullable() default true;
  
}
